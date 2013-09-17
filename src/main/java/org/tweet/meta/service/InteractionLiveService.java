package org.tweet.meta.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.keyval.persistence.dao.IKeyValJpaDAO;
import org.keyval.persistence.model.KeyVal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;
import org.tweet.meta.TwitterUserSnapshot;
import org.tweet.meta.component.TwitterInteractionValuesRetriever;
import org.tweet.meta.util.TweetIsRetweetPredicate;
import org.tweet.meta.util.TweetPassesLevel1Predicate;
import org.tweet.meta.util.TweetPassesLevel2Predicate;
import org.tweet.twitter.component.DiscouragedExpressionRetriever;
import org.tweet.twitter.service.TweetMentionService;
import org.tweet.twitter.service.TweetService;
import org.tweet.twitter.service.live.TwitterReadLiveService;
import org.tweet.twitter.util.TweetUtil;
import org.tweet.twitter.util.TwitterInteraction;
import org.tweet.twitter.util.TwitterInteractionWithValue;

import com.google.api.client.util.Preconditions;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

@Service
public class InteractionLiveService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TwitterReadLiveService twitterReadLiveService;

    @Autowired
    TweetService tweetService;

    @Autowired
    TweetMentionService tweetMentionService;

    @Autowired
    TwitterInteractionValuesRetriever twitterInteractionValuesRetriever;

    @Autowired
    DiscouragedExpressionRetriever discouragedExpressionRetriever;

    @Autowired
    IKeyValJpaDAO keyValApi;

    public InteractionLiveService() {
        super();
    }

    // API

    /**
     * - live <br/>
     * <b>1. THE DATA</b> <br/>
     * - calculate all the data that goes into calculating the type of interaction and the score <br/>
     * - at this stage, no decision about either the best type of interaction, or the score of that interaction is made <br/>
     * 
     * <b>2. The SCORES</b> <br/>
     * - calculate the scores for each type of interaction <br/>
     * 
     * <b>3. The INTERACTION</b> <br/>
     * - based on these scores, decide what the interaction should be  <br/>
     */
    public final TwitterInteractionWithValue determineBestInteraction(final Tweet tweet, final String twitterAccount) {
        Preconditions.checkState(tweet.getRetweetedStatus() == null);

        // 1. THE DATA

        final String tweetUrl = "https://twitter.com/" + tweet.getFromUser() + "/status/" + tweet.getId();
        final TwitterProfile user = tweet.getUser();
        final String userHandle = tweet.getFromUser();
        final String text = TweetUtil.getText(tweet);

        final TwitterUserSnapshot userSnapshot = analyzeUserInteractionsLive(user, userHandle);

        final List<Float> valueOfMentions = analyzeValueOfMentionsLive(tweet.getText(), twitterAccount);

        final boolean tweetAlreadyMentionsTheAuthor = text.contains("@" + tweet.getFromUser());

        // determine any other hard requirements

        final boolean shouldNotMention = tweetAlreadyMentionsTheAuthor;
        final boolean shouldNotRetweet = isTweetToPopular(tweet);

        // 2. THE SCORES

        float valueWithinMentions = valueOfMentions(valueOfMentions);
        float valueOfMention = calculateUserMentionInteractionScore(userSnapshot, user);
        float valueOfRetweet = calculateUserRetweetInteractionScore(userSnapshot, user);

        // + scores (augment scores with some uumf based on how popular the tweet was to begin with)
        final int addToScoreBasedOnHowPopularTheRetweetIs = (int) Math.log(tweet.getRetweetCount() * tweet.getRetweetCount());
        valueWithinMentions = valueWithinMentions + addToScoreBasedOnHowPopularTheRetweetIs;
        valueOfMention = valueOfMention + addToScoreBasedOnHowPopularTheRetweetIs;
        valueOfRetweet = valueOfRetweet + addToScoreBasedOnHowPopularTheRetweetIs;

        // newest
        final String author = tweet.getFromUser();
        valueOfMention = modifyValueBasedOnHistory(author, twitterAccount, valueOfMention);
        valueOfRetweet = modifyValueBasedOnHistory(author, twitterAccount, valueOfRetweet);

        // new
        boolean foundDiscouraged = false;
        final List<String> hashtags = tweetService.getHashtags(tweet);
        final List<String> discouraged = discouragedExpressionRetriever.discouraged(twitterAccount);
        for (final String hashtag : hashtags) {
            if (discouraged.contains(hashtag)) {
                foundDiscouraged = true;
                break;
            }
        }
        if (foundDiscouraged) {
            final int percentageToDecrease = 75;
            valueWithinMentions = valueWithinMentions * percentageToDecrease / 100;
            valueOfMention = valueOfMention * percentageToDecrease / 100;
            valueOfRetweet = valueOfRetweet * percentageToDecrease / 100;
        }

        // 3. THE INTERACTION

        // deal with None

        if (!passEliminatoryChecksBasedOnUser(user) || !passEliminatoryChecksBasedOnUserStats(userSnapshot, userHandle)) {
            logger.info("No value in interacting with the user= {} - should not retweet tweet= {}", tweet.getFromUser(), text);
            return new TwitterInteractionWithValue(TwitterInteraction.None, valueWithinMentions);
        }

        // determine the interaction

        final float maxValueOfInteraction = NumberUtils.max(valueWithinMentions, valueOfMention, valueOfRetweet);
        if (maxValueOfInteraction == valueWithinMentions) { // tweet as is - already contains valuable mentions
            logger.debug("Best value in interacting with the MENTIONS inside the tweet via a TWEET; tweet= {}\n- url= {}", tweet.getText(), tweetUrl); // debug - OK
            return new TwitterInteractionWithValue(TwitterInteraction.None, valueWithinMentions);
        } else if (maxValueOfInteraction == valueOfMention) { // mention is the best value
            logger.debug("Best value in interacting with the USER via a MENTION; tweet= {}\n- url= {}", tweet.getText(), tweetUrl); // debug - OK
            if (shouldNotMention) {
                return new TwitterInteractionWithValue(TwitterInteraction.None, valueWithinMentions);
            }
            return new TwitterInteractionWithValue(TwitterInteraction.Mention, valueOfMention);
        } else { // retweet is the best value
            logger.debug("Best value in interacting with the author USER via a RETWEET; tweet= {}\n- url= {}", tweet.getText(), tweetUrl); // debug - OK
            if (shouldNotRetweet) {
                return new TwitterInteractionWithValue(TwitterInteraction.None, valueOfRetweet);
            }
            return new TwitterInteractionWithValue(TwitterInteraction.Retweet, valueOfRetweet);
        }
    }

    /**
     * - live
     */
    private final List<Float> analyzeValueOfMentionsLive(final String text, final String twitterAccount) {
        final List<Float> mentionsAnalyzed = Lists.newArrayList();
        final List<String> mentions = tweetMentionService.extractMentions(text);
        for (final String mentionedUser : mentions) {
            final TwitterInteractionWithValue interactionWithAuthor = determineBestInteractionWithAuthorLive(mentionedUser, twitterAccount);
            mentionsAnalyzed.add(interactionWithAuthor.getVal());
        }

        return mentionsAnalyzed;
    }

    /**
     * - local
     */
    private final float valueOfMentions(final List<Float> valueOfMentions) {
        float score = 0;
        for (final float valueOfPotentialMention : valueOfMentions) {
            score += valueOfPotentialMention;
        }
        return score;
    }

    // util

    final float modifyValueBasedOnHistory(final float valueToModify, final int valueOfAuthorInteraction) {
        if (valueOfAuthorInteraction < -9) {
            return 0;
        }

        return valueToModify + (valueOfAuthorInteraction * 10.0f * valueToModify / 100.0f);
    }

    /**
     * - <b>live</b>: interacts with the twitter API <br/>
     * - <b>local</b>: everything else
     */
    final TwitterInteractionWithValue determineBestInteractionWithAuthorLive(final String userHandle, final String twitterAccount) {
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        if (user == null) {
            return new TwitterInteractionWithValue(TwitterInteraction.None, 0);
        }
        return decideBestInteractionWithAuthorLive(user, userHandle, twitterAccount);
    }

    /**
     * - <b>live</b>: interacts with the twitter API <br/>
     * - <b>local</b>: everything else
     */
    TwitterInteractionWithValue decideBestInteractionWithAuthorLive(final TwitterProfile user, final String userHandle, final String twitterAccount) {
        if (!passEliminatoryChecksBasedOnUser(user)) {
            return new TwitterInteractionWithValue(TwitterInteraction.None, 0);
        }

        final TwitterUserSnapshot userSnapshot = analyzeUserInteractionsLive(user, userHandle);
        if (!passEliminatoryChecksBasedOnUserStats(userSnapshot, userHandle)) {
            return new TwitterInteractionWithValue(TwitterInteraction.None, 0);
        }

        return decideAndScoreBestInteractionWithUser(userSnapshot, user, twitterAccount);
    }

    private final boolean passEliminatoryChecksBasedOnUser(final TwitterProfile user) {
        if (!isWorthInteractingWithBasedOnLanguage(user)) {
            return false;
        }
        if (!isWorthInteractingWithBasedOnFollowerCount(user)) {
            return false;
        }
        if (!isWorthInteractingWithBasedOnTweetsCount(user)) {
            return false;
        }

        return true;
    }

    /**
     * Minor Requirements for considering an interaction with this account valuable
     */
    private final boolean passEliminatoryChecksBasedOnUserStats(final TwitterUserSnapshot userSnapshot, final String userHandleForLog) {
        final float goodRetweetPercentage = userSnapshot.getGoodRetweetPercentage();
        if (goodRetweetPercentage < twitterInteractionValuesRetriever.getMinRetweetsPercentageOfValuableUser()) {
            logger.info("Should not interact with user= {} \n- reason: the percentage of retweets is to small= {}%", userHandleForLog, goodRetweetPercentage);
            return false;
        }

        final float mentionsOutsideOfRetweetsPercentage = userSnapshot.getMentionsOutsideOfRetweetsPercentage();
        if (goodRetweetPercentage + mentionsOutsideOfRetweetsPercentage < twitterInteractionValuesRetriever.getMinRetweetsPlusMentionsOfValuableUser()) {
            logger.info("Should not interact with user= {} \n- reason: the number of retweets+mentions percentage is to small= {}%", userHandleForLog, (goodRetweetPercentage + mentionsOutsideOfRetweetsPercentage));
            return false;
        }

        final float retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage = userSnapshot.getRetweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage();
        if (retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage < twitterInteractionValuesRetriever.getMinSmallAccountRetweetsPercentage()) {
            logger.info("Should not interact with user= {} \n- reason: the percentage of good retweets of small accounts is simply to low= {}%", userHandleForLog, retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage);
            return false;
        }

        // @formatter:off
        final String analysisResult = new StringBuilder().append("\n")
            .append("{} profile: ").append("\n")
            .append("{}% - good retweets - out of which: ").append("{}% of small accounts, ").append("{}% of non followed accounts, ").append("\n")
            .append("{}% - retweets of self mentions (out of all tweets) ").append("\n")
            .append("{}% - mentions (outside of retweets) ").append("\n")
            .append("=> worth interacting with")
            .toString();
        logger.info(analysisResult, 
            userHandleForLog, 
            goodRetweetPercentage, retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage, userSnapshot.getRetweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage(), 
            userSnapshot.getRetweetsOfSelfMentionsPercentage(), 
            mentionsOutsideOfRetweetsPercentage 
        );
        // @formatter:on

        return true;
    }

    final TwitterInteractionWithValue decideAndScoreBestInteractionWithUser(final TwitterUserSnapshot userSnapshot, final TwitterProfile user, final String twitterAccount) {
        float mentionScore = calculateUserMentionInteractionScore(userSnapshot, user);
        float retweetScore = calculateUserRetweetInteractionScore(userSnapshot, user);

        // newest
        final String author = user.getScreenName();
        mentionScore = modifyValueBasedOnHistory(author, twitterAccount, mentionScore);
        retweetScore = modifyValueBasedOnHistory(author, twitterAccount, retweetScore);

        //
        final float retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage = userSnapshot.getRetweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage();
        if (retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage < 1) { // if they don't retweet any accounts they don't follow - no dice
            return new TwitterInteractionWithValue(TwitterInteraction.None, 0);
        }
        final float retweetsOfSelfMentionsPercentage = userSnapshot.getRetweetsOfSelfMentionsPercentage();
        if (retweetsOfSelfMentionsPercentage < 1) { // if they don't retweet self mentions at all, then no point in mentioning
            return new TwitterInteractionWithValue(TwitterInteraction.Retweet, retweetScore);
        }

        if (mentionScore > retweetScore) {
            return new TwitterInteractionWithValue(TwitterInteraction.Mention, mentionScore);
        } else {
            return new TwitterInteractionWithValue(TwitterInteraction.Retweet, retweetScore);
        }
    }

    private final float modifyValueBasedOnHistory(final String author, final String twitterAccount, final float valueToModify) {
        float result = valueToModify;
        final String keyOfAuthorInteractionHistory = "interaction." + twitterAccount + "." + author;
        final KeyVal authorInteractionHistory = keyValApi.findByKey(keyOfAuthorInteractionHistory);
        if (authorInteractionHistory != null) {
            final int valueOfAuthorInteraction = Integer.valueOf(authorInteractionHistory.getValue());
            logger.info("Based on the interaction history with twitterAccount= {}, all values are modified with= {}", author, valueOfAuthorInteraction);

            // valueWithinMentions is not affected
            result = modifyValueBasedOnHistory(valueToModify, valueOfAuthorInteraction);
        }
        result = Math.max(0, result);
        return result;
    }

    /**
     * <b>SCORING</b> <br/>
     * - <b>probability of a retweet</b> = 80% <br/>
     *  -- how many good retweets of non-large accounts <br/>
     *  -- how many retweets of self mentions <br/>
     * - <b>value of the retweet</b> = 20% <br/>
     *  -- how many followers does the account have <br/>
     *  <br/>
     *  
     *  <b>PROBABILITY EXAMPLES</b> <br/>
     *  - good retweets of non-large accounts = 66% | retweets of self mentions = 8%  => 5.28 <br/>
     *  - good retweets of non-large accounts = 50% | retweets of self mentions = 28%  => 14  <br/>
     *  <br/>
     *  
     *  <b>FULL EXAMPLES</b> <br/>
     *  - probability=4% value= ln(500)    = 6.21  => score=  50 <br/>
     *  - probability=4%  value= ln(10 000) = 9.21  => score= 400 <br/>
     *  - probability=1%  value= ln(75 000) = 11.22 => score= 750 <br/>
     */
    final float calculateUserMentionInteractionScore(final TwitterUserSnapshot userSnapshot, final TwitterProfile user) {
        // 1. the data - likelihood

        final float retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage = userSnapshot.getRetweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage();
        final float retweetsOfSelfMentionsPercentage = userSnapshot.getRetweetsOfSelfMentionsPercentage();
        final float finalProbabilityOfBackInteraction = retweetsOfSelfMentionsPercentage * retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage / 100.0f;

        // 2. the data - value

        // the follower count of the user should increase the overall interaction score
        final float finalValueOfBackInteraction;
        if (user.getFollowersCount() > 0) {
            finalValueOfBackInteraction = (float) Math.log(user.getFollowersCount());
        } else {
            finalValueOfBackInteraction = 0;
        }

        // the calculation
        // - note: the final value of a back interaction is correct, but we decrease it here a bit (ln(300) = 5.x)
        final float valueOfInteractionPartOfScore;
        if (finalValueOfBackInteraction == 0) {
            valueOfInteractionPartOfScore = 0;
        } else {
            valueOfInteractionPartOfScore = (float) Math.log(finalValueOfBackInteraction);
        }

        final float mentionScore = valueOfInteractionPartOfScore * finalProbabilityOfBackInteraction * 3.5f;
        return mentionScore;
    }

    final float calculateUserRetweetInteractionScore(final TwitterUserSnapshot userSnapshot, final TwitterProfile user) {
        // 1. the data - likelihood

        final float retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage = userSnapshot.getRetweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage();
        final float finalProbabilityOfBackInteraction;
        if (retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage == 0) {
            finalProbabilityOfBackInteraction = 0;
        } else {
            finalProbabilityOfBackInteraction = (float) Math.log(retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage); // of course an guess - but with retweeting - there's no better option yet
        }
        // 2. the data - value

        // the follower count of the user should increase the overall interaction score (not by much, but still)
        final float finalValueOfBackInteraction;
        if (user.getFollowersCount() > 0) {
            finalValueOfBackInteraction = (float) Math.log(user.getFollowersCount());
        } else {
            finalValueOfBackInteraction = 0;
        }

        // the calculation
        // - note: the final value of a back interaction is correct, but we decrease it here a bit (ln(300) = 5.x)
        final float valueOfInteractionPartOfScore;
        if (finalValueOfBackInteraction == 0) {
            valueOfInteractionPartOfScore = 0;
        } else {
            valueOfInteractionPartOfScore = (float) Math.log(finalValueOfBackInteraction);
        }

        final float retweetScore = valueOfInteractionPartOfScore * finalProbabilityOfBackInteraction;
        return retweetScore;
    }

    /**
     * - <b>live</b>: interacts with the twitter API <br/>
     */
    private final TwitterUserSnapshot analyzeUserInteractionsLive(final TwitterProfile user, final String userHandle) {
        final int pagesToAnalyze = twitterInteractionValuesRetriever.getPagesToAnalyze();
        final List<Tweet> tweetsOfAccount = twitterReadLiveService.listTweetsOfAccountMultiRequestRaw(userHandle, pagesToAnalyze);

        final int goodRetweets = countGoodRetweets(tweetsOfAccount);
        final float goodRetweetsPercentage = (goodRetweets * 100.0f) / (pagesToAnalyze * 200);

        final int goodRetweetsOfSmallAccounts;
        final float retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage;
        if (goodRetweets > 0) {
            goodRetweetsOfSmallAccounts = countRetweetsOfNonLargeAccounts(tweetsOfAccount);
            retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage = (goodRetweetsOfSmallAccounts * 100.0f) / goodRetweets;
        } else {
            goodRetweetsOfSmallAccounts = 0;
            retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage = 0;
        }

        final int retweetsOfSelfMentions = countRetweetsOfTweetsThatMentionsSelf(tweetsOfAccount, userHandle);
        final float retweetsOfSelfMentionsPercentage = (retweetsOfSelfMentions * 100.0f) / (pagesToAnalyze * 200);

        final int mentions = countMentionsOutsideOfRetweets(tweetsOfAccount);
        final float mentionsPercentage = (mentions * 100.0f) / (pagesToAnalyze * 200);

        final int retweetsOfNonFollowedUsers;
        final float retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage;
        if (goodRetweets > 0) {
            retweetsOfNonFollowedUsers = countRetweetsOfAccountsTheyDoNotFollow(tweetsOfAccount, user);
            if (retweetsOfNonFollowedUsers > 0) {
                retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage = (retweetsOfNonFollowedUsers * 100.0f) / goodRetweets;
            } else {
                retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage = 0;
            }
        } else {
            retweetsOfNonFollowedUsers = 0;
            retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage = 0;
        }

        return new TwitterUserSnapshot(goodRetweetsPercentage, retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage, retweetsOfSelfMentionsPercentage, mentionsPercentage, retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage);
    }

    private final boolean isWorthInteractingWithBasedOnLanguage(final TwitterProfile user) {
        final String languageOfUser = user.getLanguage();
        if (TweetUtil.acceptedUserLang.contains(languageOfUser)) {
            return true;
        }

        logger.info("Should not interact with user= {} because user language is= {}", user.getScreenName(), languageOfUser);
        return false;
    }

    private final boolean isWorthInteractingWithBasedOnFollowerCount(final TwitterProfile user) {
        final int followersCount = user.getFollowersCount();
        if (followersCount > twitterInteractionValuesRetriever.getMinFolowersOfValuableUser()) {
            return true;
        }

        logger.info("Should not interact with user= {} because the followerCount is to small= {}", user.getScreenName(), followersCount);
        return false;
    }

    private final boolean isWorthInteractingWithBasedOnTweetsCount(final TwitterProfile user) {
        final int tweetsCount = user.getStatusesCount();
        if (tweetsCount > twitterInteractionValuesRetriever.getMinTweetsOfValuableUser()) {
            return true;
        }

        logger.info("Should not interact with user= {} because the tweetsCount is to small= {}", user.getScreenName(), tweetsCount);
        return false;
    }

    // local counts

    /**
     * - local
     */
    private final int countGoodRetweets(final List<Tweet> tweetsOfAccount) {
        final Collection<Tweet> retweets = Collections2.filter(tweetsOfAccount, new TweetIsRetweetPredicate());
        final Collection<Tweet> retweetsPassingLevel1 = Collections2.filter(retweets, new TweetPassesLevel1Predicate(tweetService));
        final Collection<Tweet> retweetsPassingLevel2 = Collections2.filter(retweetsPassingLevel1, new TweetPassesLevel2Predicate(tweetService));

        int count = 0;
        for (final Tweet tweet : tweetsOfAccount) {
            if (isTweetGoodRetweet(tweet)) {
                count++;
            }
        }

        if (count != retweetsPassingLevel2.size()) {
            // TODO: temp - remove soon
            logger.error("If this happens - something's wrong");
        }
        return count;
    }

    /**
     * - local
     */
    private final int countRetweetsOfNonLargeAccounts(final List<Tweet> tweetsOfAccount) {
        int count = 0;
        for (final Tweet tweet : tweetsOfAccount) {
            if (tweet.isRetweet()) {
                if (isTweetGoodRetweet(tweet) && tweet.getRetweetedStatus().getUser().getFollowersCount() < twitterInteractionValuesRetriever.getLargeAccountDefinition()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * - local
     */
    private int countRetweetsOfTweetsThatMentionsSelf(final List<Tweet> tweetsOfAccount, final String userHandle) {
        int count = 0;
        final String userHandleAsMentioned;
        if (userHandle.startsWith("@")) {
            userHandleAsMentioned = userHandle;
        } else {
            userHandleAsMentioned = "@" + userHandle;
        }
        for (final Tweet tweet : tweetsOfAccount) {
            if (tweet.isRetweet()) {
                if (tweetMentionService.extractMentions(tweet.getText()).contains(userHandleAsMentioned)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * - local
     */
    private boolean isTweetGoodRetweet(final Tweet tweet) {
        if (!tweet.isRetweet()) {
            return false;
        }

        if (!tweetService.passesLevel1Checks(tweet, null)) {
            return false;
        }

        final String text = TweetUtil.getText(tweet);
        if (!tweetService.passesLevel2Checks(text)) {
            return false;
        }

        return true;
    }

    /**
     * - local
     */
    private final int countMentionsOutsideOfRetweets(final List<Tweet> tweetsOfAccount) {
        int count = 0;
        for (final Tweet tweet : tweetsOfAccount) {
            if (!tweet.isRetweet() && TweetUtil.getText(tweet).contains("@")) {
                Preconditions.checkState(tweet.getRetweetedStatus() == null);
                count++;
            }
        }
        return count;
    }

    /**
     * - live
     */
    private final int countRetweetsOfAccountsTheyDoNotFollow(final List<Tweet> tweetsOfAccount, final TwitterProfile account) {
        final int pages = 2;
        if (account.getFriendsCount() > (pages * 5000)) {
            return -1;
        }
        final Collection<Tweet> retweets = Collections2.filter(tweetsOfAccount, new TweetIsRetweetPredicate());
        final Collection<Tweet> retweetsPassingLevel1 = Collections2.filter(retweets, new TweetPassesLevel1Predicate(tweetService));
        final Collection<Tweet> retweetsPassingLevel2 = Collections2.filter(retweetsPassingLevel1, new TweetPassesLevel2Predicate(tweetService));

        final Collection<Long> originalUserIds = Collections2.transform(retweetsPassingLevel2, new Function<Tweet, Long>() {
            @Override
            public final Long apply(final Tweet input) {
                return input.getRetweetedStatus().getFromUserId();
            }
        });

        final Set<Long> friendIds = twitterReadLiveService.getFriendIds(account, pages);

        int count = 0;
        for (final long userIdOfRetweet : originalUserIds) {
            if (!friendIds.contains(userIdOfRetweet)) {
                count++;
            }
        }
        return count;
    }

    /**
     * - local
     */
    private final boolean isTweetToPopular(final Tweet tweet) {
        final boolean hasLessRtsThanTheTooPopularThreshold = tweet.getRetweetCount() < twitterInteractionValuesRetriever.getMaxRetweetsForTweet();
        return !hasLessRtsThanTheTooPopularThreshold;
    }

}
