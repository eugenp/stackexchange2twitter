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
import org.tweet.stackexchange.util.StackexchangeUtil;
import org.tweet.stackexchange.util.Tag;

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
    // minute accuracy
    @Scheduled(cron = "0 22 13,22 * * *")
    // @Scheduled(cron = "0 0 16,19 * * *")
    public void tweetStackExchangeTopQuestion() throws JsonProcessingException, IOException {
        logger.info("Starting to execute scheduled tweet operations");

        service.tweetTopQuestionBySite(Site.serverfault, SimpleTwitterAccount.ServerFaultBest.name(), 1);

        service.tweetTopQuestionBySite(Site.askubuntu, SimpleTwitterAccount.AskUbuntuBest.name(), 1);

        service.tweetTopQuestionByTag(Site.stackoverflow, SimpleTwitterAccount.SpringAtSO.name(), Tag.spring.name(), 1);

        service.tweetTopQuestionByTag(Site.stackoverflow, SimpleTwitterAccount.JavaTopSO.name(), Tag.java.name(), 1);

        service.tweetTopQuestionByTag(Site.stackoverflow, SimpleTwitterAccount.BestClojure.name(), Tag.clojure.name(), 1);

        service.tweetTopQuestionByTag(Site.stackoverflow, SimpleTwitterAccount.BestScala.name(), Tag.scala.name(), 1);

        service.tweetTopQuestionByTag(Site.stackoverflow, SimpleTwitterAccount.jQueryDaily.name(), Tag.jquery.name(), 1);

        service.tweetTopQuestionByTag(Site.stackoverflow, SimpleTwitterAccount.RESTDaily.name(), Tag.rest.name(), 1);

        service.tweetTopQuestionByTag(Site.stackoverflow, SimpleTwitterAccount.BestEclipse.name(), Tag.eclipse.name(), 1);

        service.tweetTopQuestionByTag(Site.stackoverflow, SimpleTwitterAccount.BestGit.name(), Tag.git.name(), 1);

        service.tweetTopQuestionByTag(Site.stackoverflow, SimpleTwitterAccount.BestMaven.name(), Tag.maven.name(), 1);

        service.tweetTopQuestionByTag(Site.stackoverflow, SimpleTwitterAccount.BestJPA.name(), Tag.jpa.name(), 1);

        service.tweetTopQuestionByTag(Site.stackoverflow, SimpleTwitterAccount.BestAlgorithms.name(), Tag.algorithm.name(), 1);

        final Site randomSite = StackexchangeUtil.pickOne(Site.stackoverflow, Site.askubuntu, Site.superuser);
        service.tweetTopQuestionByTag(randomSite, SimpleTwitterAccount.BestBash.name(), Tag.bash.name(), 1);

        logger.info("Finished executing scheduled tweet operations");
    }

}
