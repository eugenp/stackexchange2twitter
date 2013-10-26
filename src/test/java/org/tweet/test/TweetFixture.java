package org.tweet.test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.util.Date;

import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.test.util.ReflectionTestUtils;

public final class TweetFixture {

    private TweetFixture() {
        throw new AssertionError();
    }

    // API

    public static Tweet createTweet(final int retweetCount) {
        return createTweet(retweetCount, "en", "en");
    }

    public static Tweet createTweet(final int retweetCount, final String userLanguage, final String tweetLanguage) {
        final Tweet tweet = new Tweet(0l, randomAlphabetic(6), new Date(), null, null, null, 0l, tweetLanguage, null);
        final TwitterProfile user = createTwitterProfile();
        ReflectionTestUtils.setField(user, "language", userLanguage);
        ReflectionTestUtils.setField(user, "screenName", randomAlphabetic(6));
        tweet.setUser(user);
        tweet.setFromUser(user.getName());
        tweet.setRetweetCount(retweetCount);
        return tweet;
    }

    public static TwitterProfile createTwitterProfile() {
        final TwitterProfile twitterProfile = new TwitterProfile(0l, randomAlphabetic(6), randomAlphabetic(6), null, null, null, null, new Date());
        ReflectionTestUtils.setField(twitterProfile, "followersCount", 500);
        return twitterProfile;
    }

}
