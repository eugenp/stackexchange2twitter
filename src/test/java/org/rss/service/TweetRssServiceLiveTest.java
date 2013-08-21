package org.rss.service;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.common.spring.CommonServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rss.spring.RssContextConfig;
import org.rss.spring.RssPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;

import com.sun.syndication.io.FeedException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {//@formatter:off
    // org.common
    CommonServiceConfig.class, 
    
    // org.tweet
    TwitterConfig.class, 
    TwitterLiveConfig.class, 
    
    // org.rss
    RssContextConfig.class, 
    RssPersistenceJPAConfig.class 
}) //@formatter:on
@ActiveProfiles({ SpringProfileUtil.LIVE, SpringProfileUtil.WRITE, SpringProfileUtil.DEV })
public class TweetRssServiceLiveTest {

    @Autowired
    private TweetRssLiveService tweetRssService;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoException() {
        assertNotNull(tweetRssService);
    }

    @Test
    public final void whenTweetingFromRssFeed1_theenNoExceptions() throws IOException, IllegalArgumentException, FeedException {
        tweetRssService.tweetFromRss("http://feeds.feedburner.com/FeedForMkyong", TwitterAccountEnum.BestOfJava.name());
    }

    @Test
    public final void whenTweetingFromRssFeed2_theenNoExceptions() throws IOException, IllegalArgumentException, FeedException {
        tweetRssService.tweetFromRss("http://feeds.feedburner.com/SpringSourceTeamBlog", TwitterAccountEnum.SpringAtSO.name());
    }

}
