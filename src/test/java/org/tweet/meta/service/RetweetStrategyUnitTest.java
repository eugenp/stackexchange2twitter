package org.tweet.meta.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertFalse;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.test.util.ReflectionTestUtils;

public final class RetweetStrategyUnitTest {

    private RetweetStrategy instance;

    // fixtures

    @Before
    public final void before() {
        this.instance = new RetweetStrategy();
    }

    // tests

    @Test
    public final void whenCheckingIfTweetIsToBeRetweeted_thenNoExceptions() {
        final Tweet tweet = createTweet(10);
        instance.shouldRetweet(tweet);
    }

    @Test
    public final void givenTweetWith100Rts_whenCheckingIfItShouldBeRetweeted_thenNo() {
        final Tweet tweet = createTweet(100);
        assertFalse(instance.shouldRetweet(tweet));
    }

    //

    private final Tweet createTweet(final int retweetCount) {
        final Tweet tweet = new Tweet(0l, randomAlphabetic(6), new Date(), null, null, null, 0l, "en", null);
        final TwitterProfile user = new TwitterProfile(0l, randomAlphabetic(6), randomAlphabetic(6), null, null, null, null, new Date());
        ReflectionTestUtils.setField(user, "language", "en");
        tweet.setUser(user);
        tweet.setRetweetCount(retweetCount);
        return tweet;
    }

}
