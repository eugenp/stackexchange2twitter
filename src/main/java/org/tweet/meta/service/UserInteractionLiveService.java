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
    public boolean isUserWorthInteractingWith(final TwitterProfile user, final String userHandle) {
        if (!isWorthInteractingWithBasedOnLanguage(user)) {
            return false;
        }
        if (!isWorthInteractingWithBasedOnFollowerCount(user)) {
            return false;
        }
        if (!isWorthInteractingWithBasedOnTweetsCount(user)) {
            return false;
        }

        // final int followingCount = user.getFriendsCount();
        final TwitterUserSnapshot userSnapshot = analyzeUserInteractions(user, userHandle);

        final int goodRetweetsPercentage = userSnapshot.getGoodRetweetPercentage();
        if (goodRetweetsPercentage < twitterInteractionValuesRetriever.getMinRetweetsPercentageOfValuableUser()) {
            logger.info("Should not interact with user= {} - the percentage of retweets is to small= {}%", userHandle, goodRetweetsPercentage);
            return false;
        }

        final int largeAccountRetweetsPercentage = userSnapshot.getRetweetsOfLargeAccountsPercentage();
        if (largeAccountRetweetsPercentage > twitterInteractionValuesRetriever.getLargeAccountRetweetsMaxPercentage()) {
            logger.info("Should not interact with user= {} - the percentage of retweets of very large accounts is simply to high= {}", userHandle, largeAccountRetweetsPercentage);
            return false;
        }

        final int mentionsPercentage = userSnapshot.getMentionsPercentage();
        if (goodRetweetsPercentage + mentionsPercentage < twitterInteractionValuesRetriever.getMinRetweetsPlusMentionsOfValuableUser()) {
            logger.info("Should not interact with user= {} - the number of retweets+mentions percentage is to small= {}", userHandle, (goodRetweetsPercentage + mentionsPercentage));
            return false;
        }

        logger.info("User= {} has \n{} good retweets \n{}% of retweets of large accounts, \n{} mentions - worth interacting with", userHandle, goodRetweetsPercentage, largeAccountRetweetsPercentage, mentionsPercentage);
        return true;
    }

    public final boolean isUserWorthInteractingWith(final String userHandle) {
        final TwitterProfile user = twitterLiveService.getProfileOfUser(userHandle);
        return isUserWorthInteractingWith(user, userHandle);
    }

    // util

    private final TwitterUserSnapshot analyzeUserInteractions(final TwitterProfile user, final String userHandle) {
        final int pagesToAnalyze = 1;
        final List<Tweet> tweetsOfAccount = twitterLiveService.listTweetsOfAccountMultiRequestRaw(userHandle, pagesToAnalyze);

        final int goodRetweets = countGoodRetweets(tweetsOfAccount);
        final int goodRetweetsPercentage = (goodRetweets * 100) / (pagesToAnalyze * 200);

        final int retweetsOfLargeAccounts = countRetweetsOfLargeAccounts(tweetsOfAccount);
        final int retweetsOfLargeAccountsPercentage = (retweetsOfLargeAccounts * 100) / (pagesToAnalyze * 200);

        final int retweetsOfSelfMentions = countRetweetsOfTweetsThatMentionsSelf(tweetsOfAccount, userHandle);
        final int retweetsOfSelfMentionsPercentage = (retweetsOfSelfMentions * 100) / goodRetweets;

        final int mentions = countMentions(tweetsOfAccount);
        final int mentionsPercentage = (mentions * 100) / (pagesToAnalyze * 200);

        return new TwitterUserSnapshot(goodRetweetsPercentage, retweetsOfLargeAccountsPercentage, retweetsOfSelfMentionsPercentage, mentionsPercentage);
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
    final int countMentions(final List<Tweet> tweetsOfAccount) {
        int count = 0;
        for (final Tweet tweet : tweetsOfAccount) {
            if (TweetUtil.getText(tweet).contains("@")) {
                count++;
            }
        }
        return count;
    }

}
