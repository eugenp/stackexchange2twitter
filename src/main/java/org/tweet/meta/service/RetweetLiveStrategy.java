package org.tweet.meta.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Component;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.live.TwitterReadLiveService;
import org.tweet.twitter.util.TweetUtil;

@Component
@Profile(SpringProfileUtil.LIVE)
public final class RetweetLiveStrategy {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterReadLiveService twitterLiveService;

    public RetweetLiveStrategy() {
        super();
    }

    // API

    public final boolean shouldRetweet(final Tweet tweet) {
        final boolean hasLessRtsThanTheTooPopularThreshold = tweet.getRetweetCount() < 15;
        if (!hasLessRtsThanTheTooPopularThreshold) {
            final String tweetUrl = "https://twitter.com/" + tweet.getFromUser() + "/status/" + tweet.getId();
            logger.info("Far to popular tweet= {} - no point in retweeting...rt= {}; link= {}", TweetUtil.getText(tweet), tweet.getRetweetCount(), tweetUrl);
            return false;
        }

        final TwitterProfile user = tweet.getUser();
        final String text = TweetUtil.getText(tweet);
        final String userHandle = tweet.getFromUser();

        if (!isUserWorthInteractingWith(user, userHandle)) {
            logger.info("Should not retweet tweet= {} because it's not worth interacting with the user= {}", text, userHandle);
            return false;
        }

        return hasLessRtsThanTheTooPopularThreshold;
    }

    public final boolean isUserWorthInteractingWith(final String userHandle) {
        final TwitterProfile user = twitterLiveService.getProfileOfUser(userHandle);
        return isUserWorthInteractingWith(user, userHandle);
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

        final List<Tweet> tweetsOfAccount = twitterLiveService.listTweetsOfAccountRaw(userHandle, 200);
        final int retweets = countRetweets(tweetsOfAccount);
        final int mentions = countMentions(tweetsOfAccount);
        if (retweets < 6) {
            logger.info("Should not interact with user= {} - the number of retweets (out of the last 200 tweets) is to small= {}", userHandle, retweets);
            return false;
        }
        if (retweets + mentions < 12) {
            logger.info("Should not interact with user= {} - the number of retweets+mentions (out of the last 200 tweets) is to small= {}", userHandle, retweets);
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
            if (TweetUtil.getText(tweet).contains("@")) {
                count++;
            }
        }
        return count;
    }

}
