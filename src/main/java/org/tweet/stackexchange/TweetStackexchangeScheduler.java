package org.tweet.stackexchange;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.stackexchange.api.constants.Site;
import org.tweet.stackexchange.util.SimpleTwitterAccount;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class TweetStackexchangeScheduler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TweetStackexchangeService service;

    public TweetStackexchangeScheduler() {
        super();
    }

    // API

    // 12 hours = 1000 * 60 * 60 * 12
    @Scheduled(fixedRate = 43200000)
    public void tweetStackExchangeTopQuestion() throws JsonProcessingException, IOException {
        logger.info("Starting to execute scheduled tweet operations");

        service.tweetStackExchangeTopQuestion(Site.serverfault, SimpleTwitterAccount.ServerFaultBest.name());
        service.tweetStackExchangeTopQuestion(Site.askubuntu, SimpleTwitterAccount.AskUbuntuBest.name());

        logger.info("Finished executing scheduled tweet operations");
    }

}
