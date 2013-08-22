package org.tweet.meta.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;
import org.tweet.meta.TwitterUserSnapshot;
import org.tweet.meta.component.TwitterInteractionValuesRetriever;
import org.tweet.meta.util.TweetIsRetweetPredicate;
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

    public InteractionLiveService() {
        super();
    }

    // API

    // - with tweet

    /*
     * Process: 
     * 1. THE DATA
     * - calculate all the data that goes into calculating the type of interaction and the score
     * - at this stage, no decision about either the best type of interaction, or the score of that interaction is made
     * 2. The SCORES of each type of interaction
     * - 
     * TODO: sort 
     * determine the best interaction (with EVERYTHING) along with a score
     * - then, if that interaction is MENTION - add the mention and change the interaction to NONE
     * Interaction Scoring: 
     * - value of RETWEETING the USER (without taking the TWEET into account) - UR
     * - value of MENTIONING the USER (without taking the TWEET into account) - UM
     * - value of TWEETING the TWEET (as is, without adding a MENTION) - T
     * - value of adding a MENTION to the TWEET and then TWEETING - TM
    */

    public final TwitterInteractionWithValue determineBestInteractionRaw(final Tweet tweet) {
        Preconditions.checkState(tweet.getRetweetedStatus() == null);

        // data

        final String tweetUrl = "https://twitter.com/" + tweet.getFromUser() + "/status/" + tweet.getId();
        final TwitterProfile user = tweet.getUser();
        final String userHandle = tweet.getFromUser();
        final String text = TweetUtil.getText(tweet);

        final TwitterUserSnapshot userSnapshot = analyzeUserInteractionsLive(user, userHandle);

        final List<TwitterInteractionWithValue> valueOfMentions = analyzeValueOfMentionsLive(tweet.getText());

        final boolean tweetAlreadyMentionsTheAuthor = text.contains("@" + tweet.getFromUser());

        final int valueWithinMentions = valueOfMentions(valueOfMentions);
        final int valueOfMention = calculateUserMentionInteractionScore(userSnapshot, user);
        final int valueOfRetweet = calculateUserRetweetInteractionScore(userSnapshot, user);

        // deal with None

        if (!passEliminatoryChecksBasedOnUser(user) || !passEliminatoryChecksBasedOnUserStats(userSnapshot, userHandle)) {
            logger.info("No value in interacting with the user= {} - should not retweet tweet= {}", tweet.getFromUser(), text);
            return new TwitterInteractionWithValue(TwitterInteraction.None, valueWithinMentions);
        }

        // determine any other hard requirements
        final boolean shouldNotMention = tweetAlreadyMentionsTheAuthor;
        final boolean shouldNotRetweet = isTweetToPopular(tweet);

        // determine the interaction
        final int maxValueOfInteraction = NumberUtils.max(valueWithinMentions, valueOfMention, valueOfRetweet);
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

    TwitterInteractionWithValue decideBestInteractionWithTweetNotAuthorLive(final Tweet tweet) {
        Preconditions.checkState(tweet.getRetweetedStatus() == null);

        final List<TwitterInteractionWithValue> valueOfMentions = analyzeValueOfMentionsLive(tweet.getText());
        final boolean containsValuableMentions = containsValuableMentions(valueOfMentions);
        if (containsValuableMentions) {
            final String tweetUrl = "https://twitter.com/" + tweet.getFromUser() + "/status/" + tweet.getId();
            logger.debug("Tweet does contain valuable mention(s): {}\n- url= {}", tweet.getText(), tweetUrl); // debug - OK
            final int scoreOfMentions = valueOfMentions(valueOfMentions);
            return new TwitterInteractionWithValue(TwitterInteraction.Mention, scoreOfMentions);
            // doesn't matter if it's popular or not - mention
        }

        // if a tweet already has a lot of retweets, the value of another retweet decreases
        if (isTweetToPopular(tweet)) {
            final String tweetUrl = "https://twitter.com/" + tweet.getFromUser() + "/status/" + tweet.getId();
            logger.info("Far to popular tweet= {} - no point in retweeting...rt= {}; link= {}", tweet.getText(), tweet.getRetweetCount(), tweetUrl);
            return new TwitterInteractionWithValue(TwitterInteraction.None, 0);
        }

        return new TwitterInteractionWithValue(TwitterInteraction.Retweet, 0);
    }

    private final boolean containsValuableMentions(final List<TwitterInteractionWithValue> analyzeValueOfMentions) {
        for (final TwitterInteractionWithValue bestInteractionWithTheAuthorMentioned : analyzeValueOfMentions) {
            if (bestInteractionWithTheAuthorMentioned.getTwitterInteraction().equals(TwitterInteraction.Mention)) {
                return true;
            }
        }

        return false;
    }

    /**
     * - live
     */
    private final List<TwitterInteractionWithValue> analyzeValueOfMentionsLive(final String text) {
        final List<TwitterInteractionWithValue> mentionsAnalyzed = Lists.newArrayList();
        final List<String> mentions = tweetMentionService.extractMentions(text);
        for (final String mentionedUser : mentions) {
            final TwitterInteractionWithValue interactionWithAuthor = decideBestInteractionWithAuthorLive(mentionedUser);
            mentionsAnalyzed.add(interactionWithAuthor);
        }

        return mentionsAnalyzed;
    }

    /**
     * - local
     */
    private final int valueOfMentions(final List<TwitterInteractionWithValue> valueOfMentions) {
        int score = 0;
        for (final TwitterInteractionWithValue twitterInteractionWithValue : valueOfMentions) {
            if (twitterInteractionWithValue.getTwitterInteraction().equals(TwitterInteraction.Mention)) {
                // only mentions matter - retweets do have score, but this is calculating the value, IF the tweet is mentioned, so retweet doesn't come into this
                score += twitterInteractionWithValue.getVal();
            }
        }
        return score;
    }

    // - with author

    /**
     * - <b>live</b>: interacts with the twitter API <br/>
     * - <b>local</b>: everything else
     */
    public final TwitterInteractionWithValue decideBestInteractionWithAuthorLive(final String userHandle) {
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        return decideBestInteractionWithAuthorLive(user, userHandle);
    }

    // util

    /**
     * - <b>live</b>: interacts with the twitter API <br/>
     * - <b>local</b>: everything else
     */
    TwitterInteractionWithValue decideBestInteractionWithAuthorLive(final TwitterProfile user, final String userHandle) {
        if (!passEliminatoryChecksBasedOnUser(user)) {
            return new TwitterInteractionWithValue(TwitterInteraction.None, 0);
        }

        final TwitterUserSnapshot userSnapshot = analyzeUserInteractionsLive(user, userHandle);
        if (!passEliminatoryChecksBasedOnUserStats(userSnapshot, userHandle)) {
            return new TwitterInteractionWithValue(TwitterInteraction.None, 0);
        }

        return decideAndScoreBestInteractionWithUser(userSnapshot, user);
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
        final int goodRetweetPercentage = userSnapshot.getGoodRetweetPercentage();
        if (goodRetweetPercentage < twitterInteractionValuesRetriever.getMinRetweetsPercentageOfValuableUser()) {
            logger.info("Should not interact with user= {} \n- reason: the percentage of retweets is to small= {}%", userHandleForLog, goodRetweetPercentage);
            return false;
        }

        final int mentionsOutsideOfRetweetsPercentage = userSnapshot.getMentionsOutsideOfRetweetsPercentage();
        if (goodRetweetPercentage + mentionsOutsideOfRetweetsPercentage < twitterInteractionValuesRetriever.getMinRetweetsPlusMentionsOfValuableUser()) {
            logger.info("Should not interact with user= {} \n- reason: the number of retweets+mentions percentage is to small= {}%", userHandleForLog, (goodRetweetPercentage + mentionsOutsideOfRetweetsPercentage));
            return false;
        }

        final int retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage = userSnapshot.getRetweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage();
        if (retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage > twitterInteractionValuesRetriever.getMaxLargeAccountRetweetsPercentage()) {
            logger.info("Should not interact with user= {} \n- reason: the percentage of retweets of very large accounts is simply to high= {}%", userHandleForLog, retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage);
            return false;
        }

        final int goodRetweetsOfNonLargeAccountsOutOfAllGoodRetweetsPercentage = goodRetweetPercentage - (retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage * goodRetweetPercentage / 100);
        if (goodRetweetsOfNonLargeAccountsOutOfAllGoodRetweetsPercentage < 3) {
            logger.info("Should not interact with user= {} \n- reason: the percentage of good retweets on non-large accounts is to low= {}%", userHandleForLog, goodRetweetsOfNonLargeAccountsOutOfAllGoodRetweetsPercentage);
            return false;
        }

        // @formatter:off
        final String analysisResult = new StringBuilder().append("\n")
            .append("{} profile: ").append("\n")
            .append("{}% - good retweets - out of which: ").append("{}% of large accounts, ").append("{}% of non followed accounts, ").append("\n")
            .append("{}% - retweets of self mentions (out of all tweets) ").append("\n")
            .append("{}% - mentions (outside of retweets) ").append("\n")
            .append("=> worth interacting with")
            .toString();
        logger.info(analysisResult, 
            userHandleForLog, 
            goodRetweetPercentage, retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage, userSnapshot.getRetweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage(), 
            userSnapshot.getRetweetsOfSelfMentionsPercentage(), 
            mentionsOutsideOfRetweetsPercentage 
        );
        // @formatter:on

        return true;
    }

    final TwitterInteractionWithValue decideAndScoreBestInteractionWithUser(final TwitterUserSnapshot userSnapshot, final TwitterProfile user) {
        final int mentionScore = calculateUserMentionInteractionScore(userSnapshot, user);
        final int retweetScore = calculateUserRetweetInteractionScore(userSnapshot, user);

        final int retweetsOfSelfMentionsPercentage = userSnapshot.getRetweetsOfSelfMentionsPercentage();
        if (retweetsOfSelfMentionsPercentage < 1) { // if they don't retweet self mentions at all, then no point in mentioning
            return new TwitterInteractionWithValue(TwitterInteraction.Retweet, retweetScore);
        }
        if (mentionScore > 16) {
            return new TwitterInteractionWithValue(TwitterInteraction.Mention, mentionScore);
        }
        return new TwitterInteractionWithValue(TwitterInteraction.Retweet, retweetScore);
    }

    final int calculateUserMentionInteractionScore(final TwitterUserSnapshot userSnapshot, final TwitterProfile user) {
        final int goodRetweetPercentage = userSnapshot.getGoodRetweetPercentage(); // it doesn't tell anything about the best way to interact with the account, just that the account is worth interacting with
        // final int mentionsOutsideOfRetweetsPercentage = userSnapshot.getMentionsOutsideOfRetweetsPercentage(); // the account (somehow) finds content and mentions it - good, but no help
        final int retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage = userSnapshot.getRetweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage(); // this also doesn't decide anything about how to best interact with the account

        // the follower count of the user should increase the overall interaction score (not by much, but still)
        final int addFollowersCountToMentionScore;
        if (user.getFollowersCount() > 0) {
            addFollowersCountToMentionScore = (int) Math.log(user.getFollowersCount());
        } else {
            addFollowersCountToMentionScore = 0;
        }
        twitterInteractionValuesRetriever.getLargeAccountDefinition();

        final int retweetsOfSelfMentionsPercentage = userSnapshot.getRetweetsOfSelfMentionsPercentage();
        // ex: good = 12%; large = 60%; good and not large = 12 - (60*12/100)
        final int goodRetweetsOfNonLargeAccountsOutOfAllGoodRetweetsPercentage = goodRetweetPercentage - (retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage * goodRetweetPercentage / 100);

        final int mentionScore = goodRetweetsOfNonLargeAccountsOutOfAllGoodRetweetsPercentage + (retweetsOfSelfMentionsPercentage * 3) + addFollowersCountToMentionScore;
        return mentionScore;
    }

    final int calculateUserRetweetInteractionScore(final TwitterUserSnapshot userSnapshot, final TwitterProfile user) {
        final int goodRetweetPercentage = userSnapshot.getGoodRetweetPercentage(); // it doesn't tell anything about the best way to interact with the account, just that the account is worth interacting with
        final int retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage = userSnapshot.getRetweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage(); // this also doesn't decide anything about how to best interact with the account

        // the follower count of the user should increase the overall interaction score (not by much, but still)
        final int addFollowersCountToScore;
        if (user.getFollowersCount() > 0) {
            addFollowersCountToScore = (int) Math.log(user.getFollowersCount());
        } else {
            addFollowersCountToScore = 0;
        }
        final int addPartOfFollowerCountToRetweetScore = addFollowersCountToScore * twitterInteractionValuesRetriever.getRetweetScoreFollowersPercentage() / 100;

        // ex: good = 12%; large = 60%; good and not large = 12 - (60*12/100)
        final int goodRetweetsOfNonLargeAccountsOutOfAllGoodRetweetsPercentage = goodRetweetPercentage - (retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage * goodRetweetPercentage / 100);

        final int retweetScore = (goodRetweetsOfNonLargeAccountsOutOfAllGoodRetweetsPercentage * 75 / 100) + addPartOfFollowerCountToRetweetScore;
        return retweetScore;
    }

    /**
     * - <b>live</b>: interacts with the twitter API <br/>
     */
    private final TwitterUserSnapshot analyzeUserInteractionsLive(final TwitterProfile user, final String userHandle) {
        final int pagesToAnalyze = twitterInteractionValuesRetriever.getPagesToAnalyze();
        final List<Tweet> tweetsOfAccount = twitterReadLiveService.listTweetsOfAccountMultiRequestRaw(userHandle, pagesToAnalyze);

        final int goodRetweets = countGoodRetweets(tweetsOfAccount);
        final int goodRetweetsPercentage = (goodRetweets * 100) / (pagesToAnalyze * 200);

        final int retweetsOfLargeAccountsOutOfAllGoodRetweets;
        final int retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage;
        if (goodRetweets > 0) {
            retweetsOfLargeAccountsOutOfAllGoodRetweets = countRetweetsOfLargeAccounts(tweetsOfAccount);
            retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage = (retweetsOfLargeAccountsOutOfAllGoodRetweets * 100) / goodRetweets;
        } else {
            retweetsOfLargeAccountsOutOfAllGoodRetweets = 0;
            retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage = 0;
        }

        final int retweetsOfSelfMentions = countRetweetsOfTweetsThatMentionsSelf(tweetsOfAccount, userHandle);
        final int retweetsOfSelfMentionsPercentage = (retweetsOfSelfMentions * 100) / (pagesToAnalyze * 200);

        final int mentions = countMentionsOutsideOfRetweets(tweetsOfAccount);
        final int mentionsPercentage = (mentions * 100) / (pagesToAnalyze * 200);

        final int retweetsOfNonFollowedUsers;
        final int retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage;
        if (goodRetweets > 0) {
            retweetsOfNonFollowedUsers = countRetweetsOfAccountsTheyDoNotFollow(tweetsOfAccount, user);
            if (retweetsOfNonFollowedUsers > 0) {
                retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage = (retweetsOfNonFollowedUsers * 100) / goodRetweets;
            } else {
                retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage = 0;
            }
        } else {
            retweetsOfNonFollowedUsers = 0;
            retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage = 0;
        }

        return new TwitterUserSnapshot(goodRetweetsPercentage, retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage, retweetsOfSelfMentionsPercentage, mentionsPercentage, retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage);
    }

    private final boolean isWorthInteractingWithBasedOnLanguage(final TwitterProfile user) {
        final String languageOfUser = user.getLanguage();
        if (languageOfUser.equals("en")) {
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
        int count = 0;
        for (final Tweet tweet : tweetsOfAccount) {
            if (isTweetGoodRetweet(tweet)) {
                count++;
            }
        }
        return count;
    }

    /**
     * - local
     */
    private final int countRetweets(final List<Tweet> tweetsOfAccount) {
        int count = 0;
        for (final Tweet tweet : tweetsOfAccount) {
            if (tweet.isRetweet()) {
                count++;
            }
        }
        return count;
    }

    /**
     * - local
     */
    private final int countRetweetsOfLargeAccounts(final List<Tweet> tweetsOfAccount) {
        int count = 0;
        for (final Tweet tweet : tweetsOfAccount) {
            if (tweet.isRetweet()) {
                if (isTweetGoodRetweet(tweet) && tweet.getRetweetedStatus().getUser().getFollowersCount() > twitterInteractionValuesRetriever.getLargeAccountDefinition()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * - local
     */
    private final int countRetweetsOfTweetsThatMentionsSelf(final List<Tweet> tweetsOfAccount, final String userHandle) {
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
        final String text = tweet.getRetweetedStatus().getText();
        if (!tweetService.isTweetWorthRetweetingByTextWithLink(text)) {
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
        final Collection<Long> originalUserIds = Collections2.transform(retweets, new Function<Tweet, Long>() {
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
