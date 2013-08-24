package org.rss;

import java.io.IOException;

import org.rss.service.TweetRssLiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
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

    @Autowired
    private Environment env;

    public RssScheduler() {
        super();
    }

    // API
    @Scheduled(cron = "0 5 */1 * * *")
    public void tweetMeta1() throws JsonProcessingException, IOException {
        logger.info("Starting to execute scheduled RSS operations - 1");

        if (env.getProperty("mode.maintainance.rss", Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        service.tweetFromRss("http://feeds.feedburner.com/SpringSourceTeamBlog", TwitterAccountEnum.SpringAtSO.name(), "SpringSource");
        service.tweetFromRss("http://feeds.feedburner.com/springsource/OEVE", TwitterAccountEnum.SpringAtSO.name(), "SpringSource");

        logger.info("Finished executing scheduled RSS operations - 1");
    }

}
