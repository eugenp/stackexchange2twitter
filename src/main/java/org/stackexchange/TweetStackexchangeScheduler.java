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

    @Scheduled(cron = "0 0 12,16 * * *")
    public void tweetStackExchangeTopQuestion1() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 1");

        // 9
        service.tweetTopQuestionBySite(twitterAccountToStackSite(SimpleTwitterAccount.ServerFaultBest), SimpleTwitterAccount.ServerFaultBest.name());
        service.tweetTopQuestionBySite(twitterAccountToStackSite(SimpleTwitterAccount.AskUbuntuBest), SimpleTwitterAccount.AskUbuntuBest.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.JavaTopSO), SimpleTwitterAccount.JavaTopSO.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestJSON), SimpleTwitterAccount.BestJSON.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestOfLinux), SimpleTwitterAccount.BestOfLinux.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.DjangoDaily), SimpleTwitterAccount.DjangoDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.ParsingDaily), SimpleTwitterAccount.ParsingDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.MathDaily), SimpleTwitterAccount.MathDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestSQL), SimpleTwitterAccount.BestSQL.name());

        logger.info("Finished tweet schedule - 1");
    }

    /**
     * - these accounts are not StackExchange specific
     */
    @Scheduled(cron = "0 0 13,17 * * *")
    public void tweetDailyTopQuestion2() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 2");

        // 9
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestScala), SimpleTwitterAccount.BestScala.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.jQueryDaily), SimpleTwitterAccount.jQueryDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.RESTDaily), SimpleTwitterAccount.RESTDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestEclipse), SimpleTwitterAccount.BestEclipse.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestGit), SimpleTwitterAccount.BestGit.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestJPA), SimpleTwitterAccount.BestJPA.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestAlgorithms), SimpleTwitterAccount.BestAlgorithms.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.HibernateDaily), SimpleTwitterAccount.HibernateDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.FacebookDigest), SimpleTwitterAccount.FacebookDigest.name());

        final StackSite randomSite = GenericUtil.pickOneGeneric(twitterAccountToStackSites(SimpleTwitterAccount.BestBash));
        service.tweetTopQuestionBySiteAndTag(randomSite, SimpleTwitterAccount.BestBash.name());

        logger.info("Finished tweet schedule - 2");
    }

    /**
     * - these accounts are not StackExchange specific
     */
    @Scheduled(cron = "0 0 14,18 * * *")
    public void tweetDailyTopQuestion3() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 3");

        // 8
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestOfJava), SimpleTwitterAccount.BestOfJava.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.SpringAtSO), SimpleTwitterAccount.SpringAtSO.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestNoSQL), SimpleTwitterAccount.BestNoSQL.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.PythonDaily), SimpleTwitterAccount.PythonDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestAWS), SimpleTwitterAccount.BestAWS.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.ObjectiveCDaily), SimpleTwitterAccount.ObjectiveCDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestOfSecurity), SimpleTwitterAccount.BestOfSecurity.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestOfCloud), SimpleTwitterAccount.BestOfCloud.name());

        logger.info("Finished tweet schedule - 3");
    }

    @Scheduled(cron = "0 0 15,19 * * *")
    public void tweetDailyTopQuestion4() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 4");

        // 8
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.iOSdigest), SimpleTwitterAccount.iOSdigest.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestJavaScript), SimpleTwitterAccount.BestJavaScript.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.PerlDaily), SimpleTwitterAccount.PerlDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.LispDaily), SimpleTwitterAccount.LispDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestRubyOnRails), SimpleTwitterAccount.BestRubyOnRails.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.LandOfWordpress), SimpleTwitterAccount.LandOfWordpress.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.GoogleDigest), SimpleTwitterAccount.GoogleDigest.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestClojure), SimpleTwitterAccount.BestClojure.name());

        logger.info("Finished tweet schedule - 4");
    }

    @Scheduled(cron = "0 0 16,20 * * *")
    public void tweetDailyTopQuestion5() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 5");

        // 7
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.AspnetDaily), SimpleTwitterAccount.AspnetDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.RegexDaily), SimpleTwitterAccount.RegexDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestOfRuby), SimpleTwitterAccount.BestOfRuby.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestPHP), SimpleTwitterAccount.BestPHP.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestMaven), SimpleTwitterAccount.BestMaven.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.InTheAppleWorld), SimpleTwitterAccount.InTheAppleWorld.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.MysqlDaily), SimpleTwitterAccount.MysqlDaily.name());

        logger.info("Finished tweet schedule - 5");
    }

}
