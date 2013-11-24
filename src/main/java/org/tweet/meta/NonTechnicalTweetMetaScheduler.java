package org.tweet.meta;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.service.TweetMetaLiveService;
import org.tweet.spring.util.SpringProfileUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Profile(SpringProfileUtil.WRITE_PRODUCTION)
public class NonTechnicalTweetMetaScheduler {
    private static final String MODE_MAINTAINANCE_KEY = "mode.maintainance.rt";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TweetMetaLiveService service;

    @Autowired
    private Environment env;

    public NonTechnicalTweetMetaScheduler() {
        super();
    }

    // API

    @Scheduled(cron = "0 10 12,14,16,18,20,22 * * *")
    public void newScheduleSix() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - six (non-tech)");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 10
        service.retweetAnyByHashtag(TwitterAccountEnum.thedogbreeds.name());
        // service.retweetAnyByWord(TwitterAccountEnum.HttpClient4.name());

        logger.info("Finished new retweet schedule - six (non-tech)");
    }

}
