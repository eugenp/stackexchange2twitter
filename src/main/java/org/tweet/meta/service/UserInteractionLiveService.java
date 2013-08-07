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

import com.google.api.client.util.Preconditions;

@Service
public class UserInteractionLiveService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TwitterReadLiveService twitterLiveService;

    @Autowired
    TweetService tweetService;

    @Autowired
    TweetMentionService tweetMentionService;

    @Autowired
    TwitterInteractionValuesRetriever twitterInteractionValuesRetriever;

    public UserInteractionLiveService() {
        super();
    }

    // API

    // - with tweet

    public TwitterInteraction decideBestInteractionWithTweetNotAuthor(final Tweet tweet) {
        final boolean containsValuableMentions = containsValuableMentions(tweet.getText());
        if (containsValuableMentions) {
            // doesn't matter if it's popular or not - mention
            return TwitterInteraction.Mention;
        }

        if (isTweetToPopular(tweet)) {
            final String tweetUrl = "https://twitter.com/" + tweet.getFromUser() + "/status/" + tweet.getId();
            logger.info("Far to popular tweet= {} - no point in retweeting...rt= {}; link= {}", TweetUtil.getText(tweet), tweet.getRetweetCount(), tweetUrl);
            return TwitterInteraction.None;
        }

        return TwitterInteraction.Retweet;
    }

    public final boolean containsValuableMentions(final String text) {
        final List<String> mentions = tweetMentionService.extractMentions(text);
        for (final String mentionedUser : mentions) {
            if (decideBestInteractionWithAuthorLive(mentionedUser).equals(TwitterInteraction.Mention)) {
                logger.error("(temp-error)More value in tweeting as is - the tweet has valuable mentions: {}", text);
                return true;
            }
        }

        // retweet is a catch-all default - TODO: now we need to decide if the tweet itself has more value tweeted than retweeted
        return false;
    }

    // - with author

    /**
     * - <b>live</b>: interacts with the twitter API <br/>
     * - <b>local</b>: everything else
     */
    public final TwitterInteraction decideBestInteractionWithAuthorLive(final String userHandle) {
        final TwitterProfile user = twitterLiveService.getProfileOfUser(userHandle);
        return decideBestInteractionWithAuthorLive(user, userHandle);
    }

    /**
     * - <b>live</b>: interacts with the twitter API <br/>
     * - <b>local</b>: everything else
     */
    public TwitterInteraction decideBestInteractionWithAuthorLive(final TwitterProfile user, final String userHandle) {
        if (!isWorthInteractingWithBasedOnLanguage(user)) {
            return TwitterInteraction.None;
        }
        if (!isWorthInteractingWithBasedOnFollowerCount(user)) {
            return TwitterInteraction.None;
        }
        if (!isWorthInteractingWithBasedOnTweetsCount(user)) {
            return TwitterInteraction.None;
        }

        // minor requirements for considering an interaction with this account valuable

        final TwitterUserSnapshot userSnapshot = analyzeUserInteractionsLive(user, userHandle);
        final int goodRetweetsPercentage = userSnapshot.getGoodRetweetPercentage();
        if (goodRetweetsPercentage < twitterInteractionValuesRetriever.getMinRetweetsPercentageOfValuableUser()) {
            logger.info("Should not interact with user= {} \n- reason: the percentage of retweets is to small= {}%", userHandle, goodRetweetsPercentage);
            return TwitterInteraction.None;
        }

        final int mentionsOutsideOfRetweetsPercentage = userSnapshot.getMentionsOutsideOfRetweetsPercentage();
        if (goodRetweetsPercentage + mentionsOutsideOfRetweetsPercentage < twitterInteractionValuesRetriever.getMinRetweetsPlusMentionsOfValuableUser()) {
            logger.info("Should not interact with user= {} \n- reason: the number of retweets+mentions percentage is to small= {}%", userHandle, (goodRetweetsPercentage + mentionsOutsideOfRetweetsPercentage));
            return TwitterInteraction.None;
        }

        final int largeAccountRetweetsPercentage = userSnapshot.getRetweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage();
        if (largeAccountRetweetsPercentage > twitterInteractionValuesRetriever.getMaxLargeAccountRetweetsPercentage()) {
            logger.info("Should not interact with user= {} \n- reason: the percentage of retweets of very large accounts is simply to high= {}%", userHandle, largeAccountRetweetsPercentage);
            return TwitterInteraction.None;
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
        final TwitterInteraction bestInteractionWithUser = decideBestInteractionWithAuthorLive(user, userHandle);
        if (bestInteractionWithUser.equals(TwitterInteraction.None)) {
            return false;
        }
        return true;
    }

    /*test only*/final boolean isUserWorthInteractingWith(final String userHandle) {
        final TwitterProfile user = twitterLiveService.getProfileOfUser(userHandle);
        return isUserWorthInteractingWithLive(user, userHandle);
    }

    /**
     * - <b>live</b>: interacts with the twitter API <br/>
     */
    final TwitterUserSnapshot analyzeUserInteractionsLive(final TwitterProfile user, final String userHandle) {
        final int pagesToAnalyze = 2;
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

    // util

    private final TwitterInteraction decideBestInteractionWithUser(final TwitterUserSnapshot userSnapshot) {
        // userSnapshot.getGoodRetweetPercentage(); - it doesn't tell anything about the best way to interact with the account, just that the account is worth interacting with
        // userSnapshot.getMentionsOutsideOfRetweetsPercentage(); - the account (somehow) finds content and mentions it - good, but no help
        // userSnapshot.getRetweetsOfLargeAccountsPercentage(); - this also doesn't decide anything about how to best interact with the account
        final int retweetsOfSelfMentionsPercentage = userSnapshot.getRetweetsOfSelfMentionsPercentage();
        if (retweetsOfSelfMentionsPercentage > 5) {
            // the account likes to retweet tweets that mention it
            return TwitterInteraction.Mention;
        }

        return TwitterInteraction.Retweet;
    }

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
    final int countGoodRetweets(final List<Tweet> tweetsOfAccount) {
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
    final int countRetweets(final List<Tweet> tweetsOfAccount) {
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
    final int countRetweetsOfLargeAccounts(final List<Tweet> tweetsOfAccount) {
        int count = 0;
        for (final Tweet tweet : tweetsOfAccount) {
            if (tweet.isRetweet()) {
                if (tweet.getRetweetedStatus().getUser().getFollowersCount() > 5000) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * - local
     */
    final int countRetweetsOfTweetsThatMentionsSelf(final List<Tweet> tweetsOfAccount, final String userHandle) {
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
    boolean isTweetGoodRetweet(final Tweet tweet) {
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
    final int countMentionsOutsideOfRetweets(final List<Tweet> tweetsOfAccount) {
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
    public boolean isTweetToPopular(final Tweet tweet) {
        final boolean hasLessRtsThanTheTooPopularThreshold = tweet.getRetweetCount() < twitterInteractionValuesRetriever.getMaxRetweetsForTweet();
        return !hasLessRtsThanTheTooPopularThreshold;
    }
    // TODO: make private back soon

}
