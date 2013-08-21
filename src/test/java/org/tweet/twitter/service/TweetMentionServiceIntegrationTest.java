package org.tweet.twitter.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.common.spring.CommonServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.TwitterConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonServiceConfig.class, TwitterConfig.class })
// @ActiveProfiles(SpringProfileUtil.LIVE)
public class TweetMentionServiceIntegrationTest {

    @Autowired
    private TweetMentionService instance;

    // tests

    @Test
    public final void whenContextIsInitialized_thenNoExceptions() {
        //
    }

    @Test
    public final void whenAddingMentionToTweet1_thenCorrect() {
        final String expected = "some text here in the tweet - via @user1";
        final String actual = instance.addMention("user1", "some text here in the tweet");
        assertThat(expected, equalTo(actual));
    }

    @Test
    public final void whenAddingMentionToTweet2_thenCorrect() {
        final String expected = "Very cool walkthrough on building a #LightSwitch #HTML5 scheduling/calendar app from @ADefWebserver - http://t.co/U5hKuLAfxN (@BethMassi)";
        final String actual = instance.addMention("BethMassi", "Very cool walkthrough on building a #LightSwitch #HTML5 scheduling/calendar app from @ADefWebserver - http://t.co/U5hKuLAfxN");
        assertThat(expected, equalTo(actual));
    }

}
