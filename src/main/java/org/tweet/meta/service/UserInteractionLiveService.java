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
import org.tweet.twitter.util.TwitterAccountInteraction;

import com.google.api.client.util.Preconditions;

@Service
public class UserInteractionLiveService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterReadLiveService twitterLiveService;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private TweetMentionService tweetMentionService;

    @Autowired
    private TwitterInteractionValuesRetriever twitterInteractionValuesRetriever;

    public UserInteractionLiveService() {
        super();
    }

    // API

    /**
     * - <b>live</b>: interacts with the twitter API <br/>
     * - <b>local</b>: everything else
     */
    public final TwitterAccountInteraction decideBestInteractionWithUser(final TwitterProfile user, final String userHandle) {
        if (!isWorthInteractingWithBasedOnLanguage(user)) {
            return TwitterAccountInteraction.None;
        }
        if (!isWorthInteractingWithBasedOnFollowerCount(user)) {
            return TwitterAccountInteraction.None;
        }
        if (!isWorthInteractingWithBasedOnTweetsCount(user)) {
            return TwitterAccountInteraction.None;
        }

        // minor requirements for considering an interaction with this account valuable

        final TwitterUserSnapshot userSnapshot = analyzeUserInteractionsLive(user, userHandle);
        final int goodRetweetsPercentage = userSnapshot.getGoodRetweetPercentage();
        if (goodRetweetsPercentage < twitterInteractionValuesRetriever.getMinRetweetsPercentageOfValuableUser()) {
            logger.info("Should not interact with user= {} \n- reason: the percentage of retweets is to small= {}%", userHandle, goodRetweetsPercentage);
            return TwitterAccountInteraction.None;
        }

        final int mentionsPercentage = userSnapshot.getMentionsOutsideOfRetweetsPercentage();
        if (goodRetweetsPercentage + mentionsPercentage < twitterInteractionValuesRetriever.getMinRetweetsPlusMentionsOfValuableUser()) {
            logger.info("Should not interact with user= {} \n- reason: the number of retweets+mentions percentage is to small= {}%", userHandle, (goodRetweetsPercentage + mentionsPercentage));
            return TwitterAccountInteraction.None;
        }

        final int largeAccountRetweetsPercentage = userSnapshot.getRetweetsOfLargeAccountsPercentage();
        if (largeAccountRetweetsPercentage > twitterInteractionValuesRetriever.getMaxLargeAccountRetweetsPercentage()) {
            logger.info("Should not interact with user= {} \n- reason: the percentage of retweets of very large accounts is simply to high= {}%", userHandle, largeAccountRetweetsPercentage);
            return TwitterAccountInteraction.None;
        }

        logger.info("\n{} profile: \n{}% - good retweets \n{}% - retweets of large accounts, \n{}% - retweets of self mentions \n{}% - mentions (outside of retweets)\n=> worth interacting with", userHandle, goodRetweetsPercentage,
                largeAccountRetweetsPercentage, userSnapshot.getRetweetsOfSelfMentionsPercentage(), mentionsPercentage);

        return decideBestInteractionWithUser(userSnapshot);
    }

    private final TwitterAccountInteraction decideBestInteractionWithUser(final TwitterUserSnapshot userSnapshot) {
        // userSnapshot.getGoodRetweetPercentage(); - it doesn't tell anything about the best way to interact with the account, just that the account is worth interacting with
        // userSnapshot.getMentionsOutsideOfRetweetsPercentage(); - the account (somehow) finds content and mentions it - good, but no help
        // userSnapshot.getRetweetsOfLargeAccountsPercentage(); - this also doesn't decide anything about how to best interact with the account
        final int retweetsOfSelfMentionsPercentage = userSnapshot.getRetweetsOfSelfMentionsPercentage();
        if (retweetsOfSelfMentionsPercentage > 4) {
            // the account likes to retweet tweets that mention it
            return TwitterAccountInteraction.Mention;
        }

        return TwitterAccountInteraction.Retweet;
    }

    /**
     * - <b>live</b>: interacts with the twitter API <br/>
     * - <b>local</b>: everything else
     */
    public final boolean isUserWorthInteractingWith(final TwitterProfile user, final String userHandle) {
        final TwitterAccountInteraction bestInteractionWithUser = decideBestInteractionWithUser(user, userHandle);
        if (bestInteractionWithUser.equals(TwitterAccountInteraction.None)) {
            return false;
        }
        return true;
    }

    public final boolean isUserWorthInteractingWith(final String userHandle) {
        final TwitterProfile user = twitterLiveService.getProfileOfUser(userHandle);
        return isUserWorthInteractingWith(user, userHandle);
    }

    /**
     * - <b>live</b>: interacts with the twitter API <br/>
     */
    public final TwitterUserSnapshot analyzeUserInteractionsLive(final TwitterProfile user, final String userHandle) {
        final int pagesToAnalyze = 2;
        final List<Tweet> tweetsOfAccount = twitterLiveService.listTweetsOfAccountMultiRequestRaw(userHandle, pagesToAnalyze);

        final int goodRetweets = countGoodRetweets(tweetsOfAccount);
        final int goodRetweetsPercentage = (goodRetweets * 100) / (pagesToAnalyze * 200);

        final int retweetsOfLargeAccounts = countRetweetsOfLargeAccounts(tweetsOfAccount);
        final int retweetsOfLargeAccountsPercentage = (retweetsOfLargeAccounts * 100) / (pagesToAnalyze * 200);

        final int retweetsOfSelfMentions = countRetweetsOfTweetsThatMentionsSelf(tweetsOfAccount, userHandle);
        final int retweetsOfSelfMentionsPercentage = (retweetsOfSelfMentions * 100) / (pagesToAnalyze * 200);

        final int mentions = countMentionsOutsideOfRetweets(tweetsOfAccount);
        final int mentionsPercentage = (mentions * 100) / (pagesToAnalyze * 200);

        return new TwitterUserSnapshot(goodRetweetsPercentage, retweetsOfLargeAccountsPercentage, retweetsOfSelfMentionsPercentage, mentionsPercentage);
    }

    // util

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
        final String userHandleAsMentioned = "@" + userHandle;
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

}
