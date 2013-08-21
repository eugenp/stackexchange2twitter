package org.tweet.twitter.service;

import static org.junit.Assert.assertFalse;

import org.common.spring.CommonServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.TwitterTag;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.live.TwitterReadLiveService;
import org.tweet.twitter.util.TweetUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonServiceConfig.class, TwitterConfig.class, TwitterLiveConfig.class })
@ActiveProfiles(SpringProfileUtil.LIVE)
public class TweetServiceReadOnlyLiveTest {

    @Autowired
    private TweetService instance;

    @Autowired
    private TwitterReadLiveService twitterReadLiveService;

    // tests

    @Test
    public final void whenContextIsInitialized_thenNoExceptions() {
        //
    }

    @Test
    public final void whenTweetIsCheckedForHashtags_thenCorrectlyIdentified() {
        final Tweet tweet = twitterReadLiveService.findOne(361041593211428864l);
        final int hashtagCount = instance.countHashtags(tweet);
        final int characterLenghtOfHashTags = instance.getCharacterLenghtOfHashTags(tweet);
        System.out.println("Number of Hashtags= " + hashtagCount);
        System.out.println("Character Lenght of Hashtags= " + characterLenghtOfHashTags);
        System.out.println("Lenght of full Tweet= " + TweetUtil.getText(tweet).length());
    }

    // checks

    @Test
    public final void givenTweetShouldNotBeRetweetedByHashtagsScenario1_whenChecking_thenNo() {
        final Tweet tweet = twitterReadLiveService.findOne(361420950450872320l);
        final boolean should = instance.isTweetWorthRetweetingByNumberOfHashtags(tweet);
        assertFalse(should);
    }

    @Test
    public final void givenTweetShouldNotBeRetweetedScenario2_whenChecking_thenNo() {
        final Tweet tweet = twitterReadLiveService.findOne(361244545452740608l);
        final boolean should = instance.isTweetWorthRetweetingByRawTweet(tweet, TwitterTag.ios.name());
        assertFalse(should);
    }

    @Test
    public final void givenTweetShouldNotBeRetweetedScenario3_whenChecking_thenNo() {
        final Tweet tweet = twitterReadLiveService.findOne(361230305383821312l);
        final boolean should1 = instance.isTweetWorthRetweetingByRawTweet(tweet, TwitterTag.ipad.name());
        final boolean should2 = instance.isTweetWorthRetweetingByTextWithLink(TweetUtil.getText(tweet));
        assertFalse(should1 && should2);
    }

    @Test
    public final void givenTweetShouldNotBeRetweetedScenario4_whenChecking_thenNo() {
        final Tweet tweet = twitterReadLiveService.findOne(361259715596021760l);
        final boolean should1 = instance.isTweetWorthRetweetingByRawTweet(tweet, TwitterTag.ipad.name());
        final boolean should2 = instance.isTweetWorthRetweetingByTextWithLink(TweetUtil.getText(tweet));
        assertFalse(should1 && should2);
    }

    @Test
    public final void givenTweetShouldNotBeRetweetedScenario5_whenChecking_thenNo() {
        final Tweet tweet = twitterReadLiveService.findOne(361566503348367360l);
        final boolean should1 = instance.isTweetWorthRetweetingByRawTweet(tweet, TwitterTag.ios.name());
        final boolean should2 = instance.isTweetWorthRetweetingByTextWithLink(TweetUtil.getText(tweet));
        assertFalse(should1 && should2);
    }

    @Test
    public final void givenTweetShouldNotBeRetweetedScenario6_whenChecking_thenNo() {
        final Tweet tweet = twitterReadLiveService.findOne(362449590617796608l);
        final boolean should1 = instance.isTweetWorthRetweetingByRawTweet(tweet, TwitterTag.ios.name());
        final boolean should2 = instance.isTweetWorthRetweetingByTextWithLink(TweetUtil.getText(tweet));
        assertFalse(should1 && should2);
    }

    @Test
    public final void givenTweetShouldNotBeRetweetedScenario7_whenChecking_thenNo() {
        final Tweet tweet = twitterReadLiveService.findOne(363035168212135937l);
        final boolean should1 = instance.isTweetWorthRetweetingByRawTweet(tweet, TwitterTag.ios.name());
        final boolean should2 = instance.isTweetWorthRetweetingByTextWithLink(TweetUtil.getText(tweet));
        assertFalse(should1 && should2);
    }

    @Test
    public final void givenTweetShouldNotBeRetweetedScenario8_whenChecking_thenNo() {
        final Tweet tweet = twitterReadLiveService.findOne(364362722223210497l);
        final boolean should1 = instance.isTweetWorthRetweetingByRawTweet(tweet, TwitterTag.ios.name());
        final boolean should2 = instance.isTweetWorthRetweetingByTextWithLink(TweetUtil.getText(tweet));
        assertFalse(should1 && should2);
    }

    @Test
    public final void givenTweetShouldNotBeRetweetedScenario9_whenChecking_thenNo() {
        final Tweet tweet = twitterReadLiveService.findOne(365243842020114432l);
        final boolean should1 = instance.isTweetWorthRetweetingByRawTweet(tweet, TwitterTag.python.name());
        final boolean should2 = instance.isTweetWorthRetweetingByTextWithLink(TweetUtil.getText(tweet));
        assertFalse(should1 && should2);
    }

    @Test
    public final void givenTweetShouldNotBeRetweetedScenario10_whenChecking_thenNo() {
        final Tweet tweet = twitterReadLiveService.findOne(365735944185319424l);
        final boolean should1 = instance.isTweetWorthRetweetingByRawTweet(tweet, TwitterTag.python.name());
        final boolean should2 = instance.isTweetWorthRetweetingByTextWithLink(TweetUtil.getText(tweet));
        assertFalse(should1 && should2);
    }

    @Test
    public final void givenTweetShouldNotBeRetweetedScenario11_whenChecking_thenNo() {
        final Tweet tweet = twitterReadLiveService.findOne(333395066590736385l);
        final boolean should1 = instance.isTweetWorthRetweetingByRawTweet(tweet, TwitterTag.macbook.name());
        final boolean should2 = instance.isTweetWorthRetweetingByTextWithLink(TweetUtil.getText(tweet));
        assertFalse(should1 && should2);
    }

    @Test
    public final void givenTweetShouldNotBeRetweetedScenario12_whenChecking_thenNo() {
        final Tweet tweet = twitterReadLiveService.findOne(366884798591021057l);
        final boolean should1 = instance.isTweetWorthRetweetingByRawTweet(tweet, TwitterTag.math.name());
        final boolean should2 = instance.isTweetWorthRetweetingByTextWithLink(TweetUtil.getText(tweet));
        assertFalse(should1 && should2);
    }

}
