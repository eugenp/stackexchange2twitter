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

    /**
     * TODO: check if it's already a RT, if so, consider going for the original tweet - especially since getText will already do that
     */
    public final boolean shouldRetweet(final Tweet tweet) {
        if (isTweetToPopular(tweet)) {
            final String tweetUrl = "https://twitter.com/" + tweet.getFromUser() + "/status/" + tweet.getId();
            logger.info("Far to popular tweet= {} - no point in retweeting...rt= {}; link= {}", TweetUtil.getText(tweet), tweet.getRetweetCount(), tweetUrl);
            return false;
        }

        if (!isAuthorOfTweetWorthInteractingWith(tweet)) {
            final String text = TweetUtil.getText(tweet);
            logger.info("Should not retweet tweet= {} because it's not worth interacting with the user= {}", text, tweet.getFromUser());
            return false;
        }

        return true;
    }

    // util

    private final boolean isTweetToPopular(final Tweet tweet) {
        final boolean hasLessRtsThanTheTooPopularThreshold = tweet.getRetweetCount() < 15;
        return hasLessRtsThanTheTooPopularThreshold;
    }

    private final boolean isAuthorOfTweetWorthInteractingWith(final Tweet tweet) {
        final TwitterProfile user = tweet.getUser();
        final String userHandle = tweet.getFromUser();

        return userInteractionLiveService.isUserWorthInteractingWith(user, userHandle);
    }

}
