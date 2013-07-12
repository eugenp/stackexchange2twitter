package org.tweet.twitter.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TweetServiceUnitTest {

    private TweetService instance;

    @Before
    public final void before() {
        instance = new TweetService();
    }

    // tests

    @Test
    public final void whenTweetIsCheckedForBeingRetweet_thenNoExceptions() {
        instance.isRetweet(randomAlphabetic(120));
    }

    @Test
    public final void givenTweet1_whenTweetIsCheckedForBeingRetweet_thenIsRetweet() {
        assertTrue(instance.isRetweet("RT @DefuseSec: This is the worst crypto I've ever seen. http://www.cryptofails.com/2013/07/osticket-fail-open-fail-often.html \u2026 #php #cryptofails"));
    }

    @Test
    public final void givenTweet2_whenTweetIsCheckedForBeingRetweet_thenIsRetweet() {
        assertTrue(instance.isRetweet("RT@DefuseSec: This is the worst crypto I've ever seen. http://www.cryptofails.com/2013/07/osticket-fail-open-fail-often.html \u2026 #php #cryptofails"));
    }

    @Test
    public final void givenTweet3_whenTweetIsCheckedForBeingRetweet_thenIsRetweet() {
        assertTrue(instance.isRetweet("OMG. RT @DefuseSec: This is the worst crypto I've ever seen. http://www.cryptofails.com/2013/07/osticket-fail-open-fail-often.html \u2026 #php #cryptofails"));
    }

    @Test
    public final void givenTweet4_whenTweetIsCheckedForBeingRetweet_thenIsRetweet() {
        assertTrue(instance.isRetweet("RT@WPGaze Top 20 #WordPress Themes for Entertainment Blogs http://www.wpgaze.com/top-20-wordpress-themes-for-entertainment-blogs.html \u2026 #WordPress #WPGaze #Themes #Entertainment #Blogs"));
    }

}
