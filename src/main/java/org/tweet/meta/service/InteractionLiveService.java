package org.tweet.meta.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;
import org.tweet.meta.TwitterUserSnapshot;
import org.tweet.meta.component.TwitterInteractionValuesRetriever;
import org.tweet.twitter.service.TweetMentionService;
import org.tweet.twitter.service.TweetService;
import org.tweet.twitter.service.live.TwitterReadLiveService;
import org.tweet.twitter.util.TweetUtil;
import org.tweet.twitter.util.TwitterInteraction;
import org.tweet.twitter.util.TwitterInteractionWithValue;

import com.google.api.client.util.Preconditions;
import com.google.common.collect.Lists;

@Service
public class InteractionLiveService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TwitterReadLiveService twitterLiveService;

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

    public TwitterInteractionWithValue decideBestInteractionWithTweetNotAuthorLive(final Tweet tweet) {
        final List<TwitterInteractionWithValue> valueOfMentions = analyzeValueOfMentions(tweet.getText());
        final boolean containsValuableMentions = containsValuableMentionsLive(valueOfMentions);
        if (containsValuableMentions) {
            final String tweetUrl = "https://twitter.com/" + tweet.getFromUser() + "/status/" + tweet.getId();
            logger.error("(temp-error)More value in tweeting as is - the tweet has valuable mentions: {}\n- url= {}", tweet.getText(), tweetUrl);
            final TwitterInteractionWithValue totalValueOfMentions = valueOfMentions(valueOfMentions);
            return totalValueOfMentions;
            // doesn't matter if it's popular or not - mention
        }

        if (isTweetToPopular(tweet)) {
            final String tweetUrl = "https://twitter.com/" + tweet.getFromUser() + "/status/" + tweet.getId();
            logger.info("Far to popular tweet= {} - no point in retweeting...rt= {}; link= {}", TweetUtil.getText(tweet), tweet.getRetweetCount(), tweetUrl);
            return new TwitterInteractionWithValue(TwitterInteraction.None, 0);
        }

        return new TwitterInteractionWithValue(TwitterInteraction.Retweet, 0);
    }

    private final boolean containsValuableMentionsLive(final List<TwitterInteractionWithValue> analyzeValueOfMentions) {
        for (final TwitterInteractionWithValue valueOfMention : analyzeValueOfMentions) {
            if (valueOfMention.getTwitterInteraction().equals(TwitterInteraction.Mention)) {
                return true;
            }
        }

        return false;
    }

    private final List<TwitterInteractionWithValue> analyzeValueOfMentions(final String text) {
        final List<TwitterInteractionWithValue> mentionsAnalyzed = Lists.newArrayList();
        final List<String> mentions = tweetMentionService.extractMentions(text);
        for (final String mentionedUser : mentions) {
            final TwitterInteractionWithValue interactionWithAuthor = decideBestInteractionWithAuthorLive(mentionedUser);
            mentionsAnalyzed.add(interactionWithAuthor);
        }

        return mentionsAnalyzed;
    }

    private final TwitterInteractionWithValue valueOfMentions(final List<TwitterInteractionWithValue> valueOfMentions) {
        int score = 0;
        for (final TwitterInteractionWithValue twitterInteractionWithValue : valueOfMentions) {
            if (twitterInteractionWithValue.getTwitterInteraction().equals(TwitterInteraction.Mention)) {
                // only mentions matter - retweets do have score, but this is calculating the value, IF the tweet is mentioned, so retweet doesn't come into this
                score += twitterInteractionWithValue.getVal();
            }
        }
        return new TwitterInteractionWithValue(TwitterInteraction.Mention, score);
    }

    // - with author

    /**
     * - <b>live</b>: interacts with the twitter API <br/>
     * - <b>local</b>: everything else
     */
    public final TwitterInteractionWithValue decideBestInteractionWithAuthorLive(final String userHandle) {
        final TwitterProfile user = twitterLiveService.getProfileOfUser(userHandle);
        return decideBestInteractionWithAuthorLive(user, userHandle);
    }

    /**
     * - <b>live</b>: interacts with the twitter API <br/>
     * - <b>local</b>: everything else
     */
    public TwitterInteractionWithValue decideBestInteractionWithAuthorLive(final TwitterProfile user, final String userHandle) {
        if (!isWorthInteractingWithBasedOnLanguage(user)) {
            return new TwitterInteractionWithValue(TwitterInteraction.None, 0);
        }
        if (!isWorthInteractingWithBasedOnFollowerCount(user)) {
            return new TwitterInteractionWithValue(TwitterInteraction.None, 0);
        }
        if (!isWorthInteractingWithBasedOnTweetsCount(user)) {
            return new TwitterInteractionWithValue(TwitterInteraction.None, 0);
        }

        // minor requirements for considering an interaction with this account valuable

        final TwitterUserSnapshot userSnapshot = analyzeUserInteractionsLive(user, userHandle);
        final int goodRetweetsPercentage = userSnapshot.getGoodRetweetPercentage();
        if (goodRetweetsPercentage < twitterInteractionValuesRetriever.getMinRetweetsPercentageOfValuableUser()) {
            logger.info("Should not interact with user= {} \n- reason: the percentage of retweets is to small= {}%", userHandle, goodRetweetsPercentage);
            return new TwitterInteractionWithValue(TwitterInteraction.None, 0);
        }

        final int mentionsOutsideOfRetweetsPercentage = userSnapshot.getMentionsOutsideOfRetweetsPercentage();
        if (goodRetweetsPercentage + mentionsOutsideOfRetweetsPercentage < twitterInteractionValuesRetriever.getMinRetweetsPlusMentionsOfValuableUser()) {
            logger.info("Should not interact with user= {} \n- reason: the number of retweets+mentions percentage is to small= {}%", userHandle, (goodRetweetsPercentage + mentionsOutsideOfRetweetsPercentage));
            return new TwitterInteractionWithValue(TwitterInteraction.None, 0);
        }

        final int largeAccountRetweetsPercentage = userSnapshot.getRetweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage();
        if (largeAccountRetweetsPercentage > twitterInteractionValuesRetriever.getMaxLargeAccountRetweetsPercentage()) {
            logger.info("Should not interact with user= {} \n- reason: the percentage of retweets of very large accounts is simply to high= {}%", userHandle, largeAccountRetweetsPercentage);
            return new TwitterInteractionWithValue(TwitterInteraction.None, 0);
        }

        logger.info("\n{} profile: \n{}% - good retweets - {}% of large accounts, \n{}% - retweets of self mentions \n{}% - mentions (outside of retweets)\n=> worth interacting with", userHandle, goodRetweetsPercentage, largeAccountRetweetsPercentage,
                userSnapshot.getRetweetsOfSelfMentionsPercentage(), mentionsOutsideOfRetweetsPercentage);

        return decideBestInteractionWithUser(userSnapshot);
    }

    /**
     * - <b>live</b>: interacts with the twitter API <br/>
     * - <b>local</b>: everything else
     */
    public final boolean isUserWorthInteractingWithLive(final TwitterProfile user, final String userHandle) {
        final TwitterInteraction bestInteractionWithUser = decideBestInteractionWithAuthorLive(user, userHandle).getTwitterInteraction();
        if (bestInteractionWithUser.equals(TwitterInteraction.None)) {
            return false;
        }
        return true;
    }

    // util

    /*test only*/final boolean isUserWorthInteractingWith(final String userHandle) {
        final TwitterProfile user = twitterLiveService.getProfileOfUser(userHandle);
        return isUserWorthInteractingWithLive(user, userHandle);
    }

    /**
     * - <b>live</b>: interacts with the twitter API <br/>
     */
    final TwitterUserSnapshot analyzeUserInteractionsLive(final TwitterProfile user, final String userHandle) {
        final int pagesToAnalyze = twitterInteractionValuesRetriever.getPagesToAnalyze();
        final List<Tweet> tweetsOfAccount = twitterLiveService.listTweetsOfAccountMultiRequestRaw(userHandle, pagesToAnalyze);

        final int goodRetweets = countGoodRetweets(tweetsOfAccount);
        final int goodRetweetsPercentage = (goodRetweets * 100) / (pagesToAnalyze * 200);

        final int retweetsOfLargeAccountsOutOfAllGoodRetweets = countRetweetsOfLargeAccounts(tweetsOfAccount);
        final int retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage = (retweetsOfLargeAccountsOutOfAllGoodRetweets * 100) / goodRetweets;

        final int retweetsOfSelfMentions = countRetweetsOfTweetsThatMentionsSelf(tweetsOfAccount, userHandle);
        final int retweetsOfSelfMentionsPercentage = (retweetsOfSelfMentions * 100) / (pagesToAnalyze * 200);

        final int mentions = countMentionsOutsideOfRetweets(tweetsOfAccount);
        final int mentionsPercentage = (mentions * 100) / (pagesToAnalyze * 200);

        return new TwitterUserSnapshot(goodRetweetsPercentage, retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage, retweetsOfSelfMentionsPercentage, mentionsPercentage);
    }

    final TwitterInteractionWithValue decideBestInteractionWithUser(final TwitterUserSnapshot userSnapshot) {
        final int goodRetweetPercentage = userSnapshot.getGoodRetweetPercentage(); // it doesn't tell anything about the best way to interact with the account, just that the account is worth interacting with
        // final int mentionsOutsideOfRetweetsPercentage = userSnapshot.getMentionsOutsideOfRetweetsPercentage(); // the account (somehow) finds content and mentions it - good, but no help
        final int retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage = userSnapshot.getRetweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage(); // this also doesn't decide anything about how to best interact with the account
        final int retweetsOfSelfMentionsPercentage = userSnapshot.getRetweetsOfSelfMentionsPercentage();

        // good = 12%
        // large = 60%
        // good and not large = 12 - (60*12/100)
        final int goodRetweetsOfNonLargeAccountsPercentage = goodRetweetPercentage - (retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage * goodRetweetPercentage / 100);

        final int score = goodRetweetsOfNonLargeAccountsPercentage + retweetsOfSelfMentionsPercentage * 3;
        if (retweetsOfSelfMentionsPercentage > 5) {
            // the account likes to retweet tweets that mention it
            return new TwitterInteractionWithValue(TwitterInteraction.Mention, score);
        }
        return new TwitterInteractionWithValue(TwitterInteraction.Retweet, score);
    }

    //

    private boolean isWorthInteractingWithBasedOnLanguage(final TwitterProfile user) {
        final String languageOfUser = user.getLanguage();
        if (languageOfUser.equals("en")) {
            return true;
        }

        logger.info("Should not interact with user= {} because user language is= {}", user.getScreenName(), languageOfUser);
        return false;
    }

    private boolean isWorthInteractingWithBasedOnFollowerCount(final TwitterProfile user) {
        final int followersCount = user.getFollowersCount();
        if (followersCount > twitterInteractionValuesRetriever.getMinFolowersOfValuableUser()) {
            return true;
        }

        logger.info("Should not interact with user= {} because the followerCount is to small= {}", user.getScreenName(), followersCount);
        return false;
    }

    private boolean isWorthInteractingWithBasedOnTweetsCount(final TwitterProfile user) {
        final int tweetsCount = user.getStatusesCount();
        if (tweetsCount > twitterInteractionValuesRetriever.getMinTweetsOfValuableUser()) {
            return true;
        }

        logger.info("Should not interact with user= {} because the tweetsCount is to small= {}", user.getScreenName(), tweetsCount);
        return false;
    }

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
                if (isTweetGoodRetweet(tweet) && tweet.getRetweetedStatus().getUser().getFollowersCount() > 5000) {
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
        if (!tweetService.isTweetWorthRetweetingByText(text)) {
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
     * - local
     */
    private boolean isTweetToPopular(final Tweet tweet) {
        final boolean hasLessRtsThanTheTooPopularThreshold = tweet.getRetweetCount() < twitterInteractionValuesRetriever.getMaxRetweetsForTweet();
        return !hasLessRtsThanTheTooPopularThreshold;
    }

}
