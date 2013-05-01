package org.gplus.stackexchange;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.stackexchange.api.constants.Site;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.stackexchange.TweetStackexchangeService;
import org.tweet.stackexchange.util.SimpleTwitterAccount;
import org.tweet.stackexchange.util.StackexchangeUtil;
import org.tweet.stackexchange.util.Tag;

import com.fasterxml.jackson.core.JsonProcessingException;

// @Service
@Profile(SpringProfileUtil.DEPLOYED)
public class GplusStackexchangeScheduler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TweetStackexchangeService service;

    public GplusStackexchangeScheduler() {
        super();
    }

    // API
    // minute accuracy
    // @Scheduled(cron = "0 23 9,12 * * *")
    @Scheduled(cron = "0 0 1,5 * * *")
    public void tweetStackExchangeTopQuestion() throws JsonProcessingException, IOException {
        logger.info("Starting to execute scheduled tweet operations");

        service.tweetTopQuestionBySite(Site.serverfault, SimpleTwitterAccount.ServerFaultBest.name(), 1);

        service.tweetTopQuestionBySite(Site.askubuntu, SimpleTwitterAccount.AskUbuntuBest.name(), 1);

        service.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.spring.name(), SimpleTwitterAccount.SpringAtSO.name(), 1);

        service.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.java.name(), SimpleTwitterAccount.JavaTopSO.name(), 1);

        service.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.clojure.name(), SimpleTwitterAccount.BestClojure.name(), 1);

        service.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.scala.name(), SimpleTwitterAccount.BestScala.name(), 1);

        service.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.jquery.name(), SimpleTwitterAccount.jQueryDaily.name(), 1);

        service.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.rest.name(), SimpleTwitterAccount.RESTDaily.name(), 1);

        service.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.eclipse.name(), SimpleTwitterAccount.BestEclipse.name(), 1);

        service.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.git.name(), SimpleTwitterAccount.BestGit.name(), 1);

        service.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.maven.name(), SimpleTwitterAccount.BestMaven.name(), 1);

        service.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.jpa.name(), SimpleTwitterAccount.BestJPA.name(), 1);

        service.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.algorithm.name(), SimpleTwitterAccount.BestAlgorithms.name(), 1);

        final Site randomSite = StackexchangeUtil.pickOne(Site.stackoverflow, Site.askubuntu, Site.superuser);
        service.tweetTopQuestionBySiteAndTag(randomSite, Tag.bash.name(), SimpleTwitterAccount.BestBash.name(), 1);

        logger.info("Finished executing scheduled tweet operations");
    }

}
