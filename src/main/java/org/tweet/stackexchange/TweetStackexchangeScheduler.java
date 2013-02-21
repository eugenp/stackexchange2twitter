package org.tweet.stackexchange;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.stackexchange.api.constants.Site;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.stackexchange.util.SimpleTwitterAccount;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
@Profile(SpringProfileUtil.DEPLOYED)
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

        service.tweetTopQuestionBySite(Site.serverfault, SimpleTwitterAccount.ServerFaultBest.name());
        service.tweetTopQuestionBySite(Site.askubuntu, SimpleTwitterAccount.AskUbuntuBest.name());
        service.tweetTopQuestionByTag(Site.stackoverflow, SimpleTwitterAccount.SpringAtSO.name(), "spring");

        logger.info("Finished executing scheduled tweet operations");
    }

}
