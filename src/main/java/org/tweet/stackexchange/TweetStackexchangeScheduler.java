package org.tweet.stackexchange;

import static org.tweet.stackexchange.persistence.setup.TwitterAccountToStackAccount.twitterAccountToStackSite;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.stackexchange.service.TweetStackexchangeService;
import org.tweet.stackexchange.util.SimpleTwitterAccount;
import org.tweet.stackexchange.util.Tag;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * - note: scheduler for tweets on accounts that are StackExchange specific
 */
@Component
@Profile(SpringProfileUtil.DEPLOYED)
public class TweetStackexchangeScheduler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TweetStackexchangeService service;

    public TweetStackexchangeScheduler() {
        super();
    }

    // API
    // 12 14 16 18 20 22
    @Scheduled(cron = "0 0 12,18 * * *")
    public void tweetStackExchangeTopQuestion() throws JsonProcessingException, IOException {
        logger.info("Starting to execute scheduled tweet operations");

        service.tweetTopQuestionBySite(twitterAccountToStackSite(SimpleTwitterAccount.ServerFaultBest), SimpleTwitterAccount.ServerFaultBest.name(), 1);

        service.tweetTopQuestionBySite(twitterAccountToStackSite(SimpleTwitterAccount.AskUbuntuBest), SimpleTwitterAccount.AskUbuntuBest.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.SpringAtSO), Tag.spring.name(), SimpleTwitterAccount.SpringAtSO.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.JavaTopSO), Tag.java.name(), SimpleTwitterAccount.JavaTopSO.name(), 1);

        logger.info("Finished executing scheduled tweet operations");
    }

}
