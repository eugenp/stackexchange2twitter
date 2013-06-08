package org.tweet.meta;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tweet.meta.service.TweetMetaService;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.stackexchange.util.SimpleTwitterAccount;
import org.tweet.stackexchange.util.Tag;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * - note: scheduler for tweets on accounts that are StackExchange specific
 */
@Component
@Profile(SpringProfileUtil.DEPLOYED)
public class TweetMetaScheduler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TweetMetaService service;

    public TweetMetaScheduler() {
        super();
    }

    // API
    // 12 14 -16- 18 20 22
    @Scheduled(cron = "0 0 16 * * *")
    public void tweetMeta() throws JsonProcessingException, IOException {
        logger.info("Starting to execute scheduled tweet operations");

        service.retweetByHashtag(SimpleTwitterAccount.jQueryDaily.name(), Tag.jquery.name());

        logger.info("Finished executing scheduled tweet operations");
    }

}
