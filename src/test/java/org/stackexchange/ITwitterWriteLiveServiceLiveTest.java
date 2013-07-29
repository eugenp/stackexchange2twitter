package org.stackexchange;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.spring.StackexchangeContextConfig;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TwitterTemplateCreator;
import org.tweet.twitter.service.live.ITwitterWriteLiveService;
import org.tweet.twitter.service.live.TwitterReadLiveService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TwitterConfig.class, StackexchangeContextConfig.class })
@ActiveProfiles({ SpringProfileUtil.LIVE, SpringProfileUtil.WRITE_PRODUCTION })
@Ignore("by default, there should be no component that is not deployed in production, configured to tweet")
public class ITwitterWriteLiveServiceLiveTest {

    @Autowired
    private TwitterReadLiveService twitterReadLiveService;
    @Autowired
    protected ITwitterWriteLiveService twitterWriteLiveService;

    @Autowired
    private TwitterTemplateCreator twitterCreator;

    // tests

    @Test
    public final void whenTweeting_thenNoExceptions() {
        twitterWriteLiveService.tweet(TwitterAccountEnum.BestBash.name(), "What are Unity's keyboard and mouse shortcuts?");
    }

}
