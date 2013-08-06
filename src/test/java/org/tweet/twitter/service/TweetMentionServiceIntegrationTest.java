package org.tweet.twitter.service;

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

}
