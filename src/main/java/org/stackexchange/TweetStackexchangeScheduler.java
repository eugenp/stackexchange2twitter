package org.stackexchange;

import static org.stackexchange.persistence.setup.TwitterAccountToStackAccount.twitterAccountToStackSite;
import static org.stackexchange.persistence.setup.TwitterAccountToStackAccount.twitterAccountToStackSites;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.stackexchange.api.constants.StackSite;
import org.stackexchange.service.TweetStackexchangeService;
import org.stackexchange.util.GenericUtil;
import org.stackexchange.util.SimpleTwitterAccount;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TagRetrieverService;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Profile(SpringProfileUtil.DEPLOYED)
public class TweetStackexchangeScheduler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TweetStackexchangeService service;

    @Autowired
    private TagRetrieverService tagService;

    public TweetStackexchangeScheduler() {
        super();
    }

    // API
    @Scheduled(cron = "0 0 12,18 * * *")
    public void tweetStackExchangeTopQuestion() throws JsonProcessingException, IOException {
        logger.info("Starting to execute scheduled tweet operations");

        service.tweetTopQuestionBySite(twitterAccountToStackSite(SimpleTwitterAccount.ServerFaultBest), SimpleTwitterAccount.ServerFaultBest.name());

        service.tweetTopQuestionBySite(twitterAccountToStackSite(SimpleTwitterAccount.AskUbuntuBest), SimpleTwitterAccount.AskUbuntuBest.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.SpringAtSO), SimpleTwitterAccount.SpringAtSO.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.JavaTopSO), SimpleTwitterAccount.JavaTopSO.name());

        logger.info("Finished executing scheduled tweet operations");
    }

    /**
     * - these accounts are not StackExchange specific
     */
    @Scheduled(cron = "0 0 13,19 * * *")
    public void tweetDailyTopQuestion1() throws JsonProcessingException, IOException {
        logger.info("Starting to execute scheduled tweet operations 1");

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestClojure), SimpleTwitterAccount.BestClojure.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestScala), SimpleTwitterAccount.BestScala.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.jQueryDaily), SimpleTwitterAccount.jQueryDaily.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.RESTDaily), SimpleTwitterAccount.RESTDaily.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestEclipse), SimpleTwitterAccount.BestEclipse.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestGit), SimpleTwitterAccount.BestGit.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestMaven), SimpleTwitterAccount.BestMaven.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestJPA), SimpleTwitterAccount.BestJPA.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestAlgorithms), SimpleTwitterAccount.BestAlgorithms.name());

        final StackSite randomSite = GenericUtil.pickOneGeneric(twitterAccountToStackSites(SimpleTwitterAccount.BestBash));
        service.tweetTopQuestionBySiteAndTag(randomSite, SimpleTwitterAccount.BestBash.name());

        logger.info("Finished executing scheduled tweet operations 1");
    }

    /**
     * - these accounts are not StackExchange specific
     */
    @Scheduled(cron = "0 0 14,20 * * *")
    public void tweetDailyTopQuestion2() throws JsonProcessingException, IOException {
        logger.info("Starting to execute scheduled tweet operations 2");

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestJSON), SimpleTwitterAccount.BestJSON.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestOfJava), SimpleTwitterAccount.BestOfJava.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestSQL), SimpleTwitterAccount.BestSQL.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestNoSQL), SimpleTwitterAccount.BestNoSQL.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.RegexDaily), SimpleTwitterAccount.RegexDaily.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestPHP), SimpleTwitterAccount.BestPHP.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.PythonDaily), SimpleTwitterAccount.PythonDaily.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestAWS), SimpleTwitterAccount.BestAWS.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.ObjectiveCDaily), SimpleTwitterAccount.ObjectiveCDaily.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestOfSecurity), SimpleTwitterAccount.BestOfSecurity.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestOfCloud), SimpleTwitterAccount.BestOfCloud.name());

        logger.info("Finished executing scheduled tweet operations 2");
    }

    /**
     * - these accounts are not StackExchange specific
     */
    @Scheduled(cron = "0 0 15,21 * * *")
    public void tweetDailyTopQuestion3() throws JsonProcessingException, IOException {
        logger.info("Starting to execute scheduled tweet operations 3");

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.iOSdigest), SimpleTwitterAccount.iOSdigest.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestJavaScript), SimpleTwitterAccount.BestJavaScript.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.PerlDaily), SimpleTwitterAccount.PerlDaily.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestOfRuby), SimpleTwitterAccount.BestOfRuby.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.LispDaily), SimpleTwitterAccount.LispDaily.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestRubyOnRails), SimpleTwitterAccount.BestRubyOnRails.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.InTheAppleWorld), SimpleTwitterAccount.InTheAppleWorld.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.LandOfWordpress), SimpleTwitterAccount.LandOfWordpress.name());

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.GoogleDigest), SimpleTwitterAccount.GoogleDigest.name());

        logger.info("Finished executing scheduled tweet operations 4");
    }

}
