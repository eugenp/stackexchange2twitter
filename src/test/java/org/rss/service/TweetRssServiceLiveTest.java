package org.rss.service;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rss.spring.RssContextConfig;
import org.rss.spring.RssPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.SimpleTwitterAccount;

import com.sun.syndication.io.FeedException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RssContextConfig.class, RssPersistenceJPAConfig.class })
public class TweetRssServiceLiveTest {

    @Autowired
    private TweetRssService tweetRssService;

    // tests

    @Test
    public final void whenTweetingFromRssFeed_theenNoExceptions() throws IOException, IllegalArgumentException, FeedException {
        tweetRssService.tweetFromRssInternal("http://feeds.feedburner.com/baeldung", SimpleTwitterAccount.BestOfJava.name());
    }

}
