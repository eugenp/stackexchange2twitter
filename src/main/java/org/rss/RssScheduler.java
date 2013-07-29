package org.rss;

import java.io.IOException;

import org.rss.service.TweetRssLiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.util.SpringProfileUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Profile(SpringProfileUtil.WRITE_PRODUCTION)
public class RssScheduler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TweetRssLiveService service;

    public RssScheduler() {
        super();
    }

    // API
    @Scheduled(cron = "0 0 16,20 * * *")
    public void tweetMeta1() throws JsonProcessingException, IOException {
        logger.info("Starting to execute scheduled retweet operations - 1");

        service.tweetFromRss("http://feeds.feedburner.com/FeedForMkyong", TwitterAccountEnum.BestOfJava.name());

        logger.info("Finished executing scheduled retweet operations - 1");
    }

}
