package org.tweet.stackexchange;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.api.constants.Site;
import org.tweet.spring.ContextConfig;
import org.tweet.spring.PersistenceJPAConfig;
import org.tweet.spring.StackexchangeConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.stackexchange.util.SimpleTwitterAccount;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TwitterConfig.class, ContextConfig.class, PersistenceJPAConfig.class, StackexchangeConfig.class })
public class TweetStackexchangeServiceLiveTest {

    @Autowired
    private TweetStackexchangeService tweetStackexchangeService;

    // tests

    @Test
    public final void whenTweeting_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySite(Site.stackoverflow, SimpleTwitterAccount.SpringAtSO.name());
    }

    @Test
    public final void whenTweetingByTag_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionByTag(Site.stackoverflow, SimpleTwitterAccount.SpringAtSO.name(), "spring");
    }

}
