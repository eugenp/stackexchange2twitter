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

    @SuppressWarnings("unused")
    final boolean shouldRetweet(final Tweet tweet) {
        final boolean hasLessRtsThanTheTooPopularThreshold = tweet.getRetweetCount() < 15;

        final TwitterProfile user = tweet.getUser();
        final String language = user.getLanguage();
        if (!language.equals("en")) {
            logger.info("Should not retweet tweet= {} because the language is= {}", tweet.getText(), language);
            return false;
        }
        final int followersCount = user.getFollowersCount();
        if (followersCount < 300) {
            logger.info("Should not retweet tweet= {} because for the user= {} the followerCount is to small= {}", tweet.getText(), user.getName(), followersCount);
            return false;
        }
        final int tweetsCount = user.getStatusesCount();
        if (tweetsCount < 300) {
            logger.info("Should not retweet tweet= {} because for the user= {} the tweetsCount is to small= {}", tweet.getText(), user.getName(), tweetsCount);
            return false;
        }

        final int followingCount = user.getFriendsCount();

        final List<Tweet> tweetsOfAccount = twitterLiveService.listTweetsOfAccount(tweet.getFromUser(), 200);
        final int retweets = countRetweets(tweetsOfAccount);
        if (retweets < 10) {
            logger.info("Should not retweet because for the user= {} the number of retweets (out of the last 200 tweets) is to small= {}\n -- tweet= {} ", user.getName(), retweets, tweet.getText());
            return false;
        }
        return hasLessRtsThanTheTooPopularThreshold;
    }

    private int countRetweets(final List<Tweet> tweetsOfAccount) {
        int count = 0;
        for (final Tweet tweet : tweetsOfAccount) {
            if (tweet.isRetweet()) {
                count++;
            }
        }
        return count;
    }

}
