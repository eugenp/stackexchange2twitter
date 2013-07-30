package org.tweet.twitter.service;

import org.common.spring.CommonContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.TwitterConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class, TwitterConfig.class })
// @ActiveProfiles(SpringProfileUtil.LIVE)
public class TweetServiceIntegrationTest {

    @Autowired
    private TweetService instance;

    // tests

    @Test
    public final void whenContextIsInitialized_thenNoExceptions() {
        //
    }

    // process

    @Test
    public final void whenTextIsNotProcessedCorrectly_thenNoExceptions() {
        instance.preValidityProcess("SearchHub: Join @ErikHatcher at the #Lucene/Solr Meetup in São Paulo, June 11th. Don't miss out! http://wp.me/p3cNDk-2vx");
    }

}
