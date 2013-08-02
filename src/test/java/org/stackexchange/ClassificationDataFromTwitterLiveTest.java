package org.stackexchange;

import java.io.IOException;
import java.util.List;

import org.common.spring.CommonServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.spring.StackexchangeContextConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TwitterTemplateCreator;
import org.tweet.twitter.service.live.TwitterReadLiveService;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {//@formatter:off
    CommonServiceConfig.class, 
    
    TwitterConfig.class, 
    TwitterLiveConfig.class, 
    
    StackexchangeContextConfig.class 
}) //@formatter:on
@ActiveProfiles(SpringProfileUtil.LIVE)
public class ClassificationDataFromTwitterLiveTest {

    @Autowired
    private TwitterReadLiveService twitterService;

    @Autowired
    private TwitterTemplateCreator twitterCreator;

    // tests

    // list tweets

    @Test
    public final void whenListingTweets_thenNoExceptions() throws JsonProcessingException, IOException {
        final String account = "ulohjobs";
        final TimelineOperations timelineOperations = twitterService.readOnlyTwitterApi().timelineOperations();
        final List<Tweet> first200 = timelineOperations.getUserTimeline(account, 200);
        final long lastId = first200.get(first200.size()).getId();
        int pages = 10;
        while (pages > 0) {
            timelineOperations.getUserTimeline(account, 200, lastId, -1);
            pages--;
        }
    }
}
