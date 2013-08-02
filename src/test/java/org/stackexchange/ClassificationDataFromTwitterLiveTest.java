package org.stackexchange;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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
import org.tweet.twitter.service.live.TweetToStringFunction;
import org.tweet.twitter.service.live.TwitterReadLiveService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

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
        int pages = 3;
        final String account = "ulohjobs";

        final Set<String> collector = Sets.newHashSet();
        final TimelineOperations timelineOperations = twitterService.readOnlyTwitterApi().timelineOperations();

        List<Tweet> currentPage = timelineOperations.getUserTimeline(account, 200);
        collector.addAll(Lists.transform(currentPage, new TweetToStringFunction()));

        long lastId = currentPage.get(currentPage.size() - 1).getId();
        while (pages > 0) {
            System.out.println("Processing page: " + (10 - pages));
            currentPage = timelineOperations.getUserTimeline(account, 200, 01, lastId);

            collector.addAll(Lists.transform(currentPage, new TweetToStringFunction()));
            lastId = currentPage.get(currentPage.size() - 1).getId();
            pages--;
        }

        System.out.println("Gathered: " + collector.size());
    }

}
