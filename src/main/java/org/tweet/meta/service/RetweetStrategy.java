package org.tweet.meta.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Component;
import org.tweet.twitter.service.TwitterLiveService;

@Component
public final class RetweetStrategy {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterLiveService twitterLiveService;

    public RetweetStrategy() {
        super();
    }

    // API

    public final boolean shouldRetweetRandomized(final Tweet tweet) {
        if (!shouldRetweet(tweet)) {
            return false;
        }
        if (Math.random() < 0.75) {
            return true;
        }

        return false;
    }

    public final boolean shouldRetweet(final Tweet tweet) {
        final boolean hasLessRtsThanTheTooPopularThreshold = tweet.getRetweetCount() < 15;
        if (!hasLessRtsThanTheTooPopularThreshold) {
            logger.info("Far to popular tweet= {} - no point in retweeting...rt= {}", tweet.getText(), tweet.getRetweetCount());
            return false;
        }

        final TwitterProfile user = tweet.getUser();
        final String text = tweet.getText();
        final String userHandle = tweet.getFromUser();

        if (!isUserWorthInteractingWith(user, userHandle)) {
            logger.info("Should not retweet tweet= {} because it's not worth interacting with the user= {}", text, userHandle);
            return false;
        }

        return hasLessRtsThanTheTooPopularThreshold;
    }

    public final boolean isUserWorthInteractingWith(final TwitterProfile user, final String userHandle) {
        final String languageOfUser = user.getLanguage();
        if (!languageOfUser.equals("en")) {
            logger.info("Should not interact with user= {} because user language is= {}", userHandle, languageOfUser);
            return false;
        }
        final int followersCount = user.getFollowersCount();
        if (followersCount < 300) {
            logger.info("Should not interact with user= {} because the followerCount is to small= {}", userHandle, followersCount);
            return false;
        }
        final int tweetsCount = user.getStatusesCount();
        if (tweetsCount < 300) {
            logger.info("Should not interact with user= {} because the tweetsCount is to small= {}", userHandle, tweetsCount);
            return false;
        }

        // final int followingCount = user.getFriendsCount();

        final List<Tweet> tweetsOfAccount = twitterLiveService.listTweetsOfAccount(userHandle, 200);
        final int retweets = countRetweets(tweetsOfAccount);
        final int mentions = countMentions(tweetsOfAccount);
        if (retweets < 6) {
            // TODO: put back to info when I get some emails about this
            logger.error("Should not interact with user= {} - the number of retweets (out of the last 200 tweets) is to small= {}", userHandle, retweets);
            return false;
        }
        if (retweets + mentions < 12) {
            // TODO: put back to info when I get some emails about this
            logger.error("Should not interact with user= {} - the number of retweets+mentions (out of the last 200 tweets) is to small= {}", userHandle, retweets);
            return false;
        }

        return true;
    }

    // util

    private final int countRetweets(final List<Tweet> tweetsOfAccount) {
        int count = 0;
        for (final Tweet tweet : tweetsOfAccount) {
            if (tweet.isRetweet()) {
                count++;
            }
        }
        return count;
    }

    private final int countMentions(final List<Tweet> tweetsOfAccount) {
        int count = 0;
        for (final Tweet tweet : tweetsOfAccount) {
            if (tweet.getText().contains("@")) {
                count++;
            }
        }
        return count;
    }

}
