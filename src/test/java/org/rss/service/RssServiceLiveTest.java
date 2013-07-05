package org.rss.service;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rss.spring.RssContextConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sun.syndication.io.FeedException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RssContextConfig.class })
public class RssServiceLiveTest {

    @Autowired
    private RssService rssService;

    // tests

    @Test
    public final void whenConsumingAnRssFeed_theenFeedsAreExtracted() throws IOException, IllegalArgumentException, FeedException {
        final List<Pair<String, String>> feeds = rssService.extractTitlesAndLinks("http://feeds.feedburner.com/baeldung");
        assertThat(feeds, not(empty()));
    }

}
