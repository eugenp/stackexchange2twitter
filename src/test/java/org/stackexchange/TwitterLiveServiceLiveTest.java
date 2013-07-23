package org.stackexchange;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.spring.StackexchangeContextConfig;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.TwitterConfig;
import org.tweet.twitter.service.TwitterReadLiveService;
import org.tweet.twitter.service.TwitterTemplateCreator;
import org.tweet.twitter.service.TwitterWriteLiveService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TwitterConfig.class, StackexchangeContextConfig.class })
@Ignore("by default, there should be no component that is not deployed in production, configured to tweet")
public class TwitterLiveServiceLiveTest {

    @Autowired
    private TwitterReadLiveService twitterReadLiveService;
    @Autowired
    protected TwitterWriteLiveService twitterWriteLiveService;

    @Autowired
    private TwitterTemplateCreator twitterCreator;

    // tests

    @Test
    public final void whenTweeting_thenNoExceptions() {
        twitterWriteLiveService.tweet(TwitterAccountEnum.BestBash.name(), "What are Unity's keyboard and mouse shortcuts?");
    }

}
