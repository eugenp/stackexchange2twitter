package org.tweet.meta.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;
import org.tweet.twitter.service.TweetService;
import org.tweet.twitter.service.live.TwitterReadLiveService;
import org.tweet.twitter.util.TweetUtil;

@Service
public class UserInteractionService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterReadLiveService twitterLiveService;

    @Autowired
    private TweetService tweetService;

    public UserInteractionService() {
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

        final List<Tweet> tweetsOfAccount = twitterLiveService.listTweetsOfAccountMultiRequestRaw(userHandle, 1);
        final int retweets = countGoodRetweets(tweetsOfAccount);
        final int retweetsOfVeryLargeAccounts = countRetweetsOfVeryLargeAccounts(tweetsOfAccount);
        final int mentions = countMentions(tweetsOfAccount);
        if (retweets < 6) {
            logger.info("Should not interact with user= {} - the number of retweets (out of the last 200 tweets) is to small= {}", userHandle, retweets);
            return false;
        }
        final int percentageOfRetweetsOfVeryLargeAccounts = (retweetsOfVeryLargeAccounts * 100) / retweets;
        if (percentageOfRetweetsOfVeryLargeAccounts > 90) {
            logger.info("Should not interact with user= {} - the percentage of retweets of very large accounts is simply to high= {}", userHandle, percentageOfRetweetsOfVeryLargeAccounts);
            return false;
        }
        if (retweets + mentions < 12) {
            logger.info("Should not interact with user= {} - the number of retweets+mentions (out of the last 200 tweets) is to small= {}", userHandle, retweets);
            return false;
        }

        logger.info("User= {} has {} retweets ({}% of large accounts) and {} mentions - worth interacting with", userHandle, retweets, percentageOfRetweetsOfVeryLargeAccounts, mentions);
        return true;
    }

    public final boolean isUserWorthInteractingWith(final String userHandle) {
        final TwitterProfile user = twitterLiveService.getProfileOfUser(userHandle);
        return isUserWorthInteractingWith(user, userHandle);
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
        if (followersCount > 300) {
            return true;
        }

        logger.info("Should not interact with user= {} because the followerCount is to small= {}", user.getScreenName(), followersCount);
        return false;
    }

    private boolean isWorthInteractingWithBasedOnTweetsCount(final TwitterProfile user) {
        final int tweetsCount = user.getStatusesCount();
        if (tweetsCount > 300) {
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
    final int countRetweetsOfVeryLargeAccounts(final List<Tweet> tweetsOfAccount) {
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
