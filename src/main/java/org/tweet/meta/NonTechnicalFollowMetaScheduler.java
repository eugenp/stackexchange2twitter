package org.tweet.meta;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tweet.meta.service.FollowLiveService;
import org.tweet.spring.util.SpringProfileUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Profile(SpringProfileUtil.WRITE_PRODUCTION)
public class NonTechnicalFollowMetaScheduler {
    private static final String MODE_MAINTAINANCE_KEY = "mode.maintainance.rt";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FollowLiveService service;

    @Autowired
    private Environment env;

    public NonTechnicalFollowMetaScheduler() {
        super();
    }

    // API

    @Scheduled(cron = "0 30 14,16,18,20,22 * * *")
    public void newScheduleFive() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - five (non-tech)");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // service.followBestUser(TwitterAccountEnum.thedogbreeds.name(), TwitterTag.puppy.name());

        logger.info("Finished new retweet schedule - five (non-tech)");
    }

}
