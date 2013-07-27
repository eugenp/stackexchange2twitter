package org.tweet.twitter.service;

import org.common.spring.CommonContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.live.TwitterReadLiveService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class, TwitterConfig.class, TwitterLiveConfig.class })
@ActiveProfiles(SpringProfileUtil.LIVE)
public class TweetServiceLiveTest {

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
        System.out.println("Lenght of full Tweet= " + tweet.getText().length());
    }

}
