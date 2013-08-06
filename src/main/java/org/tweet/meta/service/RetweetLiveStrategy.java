package org.tweet.meta.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Component;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.util.TweetUtil;

@Component
@Profile(SpringProfileUtil.LIVE)
public final class RetweetLiveStrategy {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserInteractionLiveService userInteractionLiveService;

    public RetweetLiveStrategy() {
        super();
    }

    // API

    public final boolean shouldRetweet(final Tweet tweet) {
        if (isTweetToPopular(tweet)) {
            final String tweetUrl = "https://twitter.com/" + tweet.getFromUser() + "/status/" + tweet.getId();
            logger.info("Far to popular tweet= {} - no point in retweeting...rt= {}; link= {}", TweetUtil.getText(tweet), tweet.getRetweetCount(), tweetUrl);
            return false;
        }

        final TwitterProfile user = tweet.getUser();
        final String userHandle = tweet.getFromUser();
        if (!userInteractionLiveService.isUserWorthInteractingWith(user, userHandle)) {
            final String text = TweetUtil.getText(tweet);
            logger.info("Should not retweet tweet= {} because it's not worth interacting with the user= {}", text, userHandle);
            return false;
        }

        return true;
    }

    private final boolean isTweetToPopular(final Tweet tweet) {
        final boolean hasLessRtsThanTheTooPopularThreshold = tweet.getRetweetCount() < 15;
        return hasLessRtsThanTheTooPopularThreshold;
    }

}
