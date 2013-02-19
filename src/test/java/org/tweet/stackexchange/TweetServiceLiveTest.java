package org.tweet.stackexchange;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.ContextConfig;
import org.tweet.spring.PersistenceJPAConfig;
import org.tweet.spring.StackexchangeConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.twitter.service.TwitterService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TwitterConfig.class, ContextConfig.class, PersistenceJPAConfig.class, StackexchangeConfig.class })
@Ignore("by default, there should be no component that is not deployed in production, configured to tweet")
public class TweetServiceLiveTest {

    @Autowired
    private TwitterService twitterService;

    // tests

    @Test
    public final void whenTweeting_thenNoExceptions() {
        twitterService.tweet("First programatic tweet with Spring Social");
    }

}
