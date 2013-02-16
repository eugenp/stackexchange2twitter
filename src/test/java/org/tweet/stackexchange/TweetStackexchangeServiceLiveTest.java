package org.tweet.stackexchange;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.ContextConfig;
import org.tweet.spring.PersistenceJPAConfig;
import org.tweet.spring.SocialConfig;
import org.tweet.spring.StackexchangeConfig;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SocialConfig.class, ContextConfig.class, PersistenceJPAConfig.class, StackexchangeConfig.class })
public class TweetStackexchangeServiceLiveTest {

    @Autowired
    private TweetStackexchangeService tweetStackexchangeService;

    // tests

    @Test
    public final void whenTweeting_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetStackExchangeTopQuestion();
    }

}
