package org.rss.service;

import java.io.IOException;

import org.common.spring.CommonContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rss.spring.RssContextConfig;
import org.rss.spring.RssPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.SimpleTwitterAccount;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;

import com.sun.syndication.io.FeedException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class, TwitterConfig.class, TwitterLiveConfig.class, RssContextConfig.class, RssPersistenceJPAConfig.class })
public class TweetRssServiceLiveTest {

    @Autowired
    private TweetRssService tweetRssService;

    // tests

    @Test
    public final void whenTweetingFromRssFeed_theenNoExceptions() throws IOException, IllegalArgumentException, FeedException {
        tweetRssService.tweetFromRss("http://feeds.feedburner.com/FeedForMkyong", SimpleTwitterAccount.BestOfJava.name());
        tweetRssService.tweetFromRss("http://feeds.feedburner.com/FeedForMkyong", SimpleTwitterAccount.BestOfJava.name());
    }

}
