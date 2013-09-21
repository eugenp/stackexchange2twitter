package org.stackexchange;

import static org.stackexchange.persistence.setup.TwitterAccountToStackAccount.twitterAccountToStackSite;
import static org.stackexchange.persistence.setup.TwitterAccountToStackAccount.twitterAccountToStackSites;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.stackexchange.api.constants.StackSite;
import org.stackexchange.service.TweetStackexchangeLiveService;
import org.stackexchange.util.GenericUtil;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TagRetrieverService;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Profile(SpringProfileUtil.WRITE_PRODUCTION)
public class TweetStackexchangeScheduler {
    private static final String MODE_MAINTAINANCE_KEY = "mode.maintainance.se";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TweetStackexchangeLiveService service;

    @Autowired
    private TagRetrieverService tagService;

    @Autowired
    private Environment env;

    public TweetStackexchangeScheduler() {
        super();
    }

    // API

    @Scheduled(cron = "0 0 14 * * *")
    public void tweetStackExchangeTopQuestion1() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 1");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 11
        service.tweetAnyTopQuestionBySite(twitterAccountToStackSite(TwitterAccountEnum.ServerFaultBest), TwitterAccountEnum.ServerFaultBest.name());
        service.tweetAnyTopQuestionBySite(twitterAccountToStackSite(TwitterAccountEnum.AskUbuntuBest), TwitterAccountEnum.AskUbuntuBest.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.JavaTopSO), TwitterAccountEnum.JavaTopSO.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestJSON), TwitterAccountEnum.BestJSON.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestOfLinux), TwitterAccountEnum.BestOfLinux.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.DjangoDaily), TwitterAccountEnum.DjangoDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.ParsingDaily), TwitterAccountEnum.ParsingDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.MathDaily), TwitterAccountEnum.MathDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestSQL), TwitterAccountEnum.BestSQL.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestOfCss), TwitterAccountEnum.BestOfCss.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestOfHTML5), TwitterAccountEnum.BestOfHTML5.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestJSP), TwitterAccountEnum.BestJSP.name());

        logger.info("Finished tweet schedule - 1");
    }

    /**
     * - these accounts are not StackExchange specific
     */
    @Scheduled(cron = "0 0 16 * * *")
    public void tweetDailyTopQuestion2() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 2");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 10
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestScala), TwitterAccountEnum.BestScala.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.jQueryDaily), TwitterAccountEnum.jQueryDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.RESTDaily), TwitterAccountEnum.RESTDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestEclipse), TwitterAccountEnum.BestEclipse.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestGit), TwitterAccountEnum.BestGit.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestJPA), TwitterAccountEnum.BestJPA.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestAlgorithms), TwitterAccountEnum.BestAlgorithms.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.HibernateDaily), TwitterAccountEnum.HibernateDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestWPF), TwitterAccountEnum.BestWPF.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestXML), TwitterAccountEnum.BestXML.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.DotNetFact), TwitterAccountEnum.DotNetFact.name());

        final StackSite randomSite = GenericUtil.pickOneGeneric(twitterAccountToStackSites(TwitterAccountEnum.BestBash));
        service.tweetAnyTopQuestionBySiteAndTag(randomSite, TwitterAccountEnum.BestBash.name());

        logger.info("Finished tweet schedule - 2");
    }

    /**
     * - these accounts are not StackExchange specific
     */
    @Scheduled(cron = "0 0 18 * * *")
    public void tweetDailyTopQuestion3() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 3");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 10
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestOfJava), TwitterAccountEnum.BestOfJava.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.SpringAtSO), TwitterAccountEnum.SpringAtSO.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestNoSQL), TwitterAccountEnum.BestNoSQL.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.PythonDaily), TwitterAccountEnum.PythonDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestAWS), TwitterAccountEnum.BestAWS.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.ObjectiveCDaily), TwitterAccountEnum.ObjectiveCDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestOfSecurity), TwitterAccountEnum.BestOfSecurity.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestOfCloud), TwitterAccountEnum.BestOfCloud.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.HTMLdaily), TwitterAccountEnum.HTMLdaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.HadoopDaily), TwitterAccountEnum.HadoopDaily.name());

        logger.info("Finished tweet schedule - 3");
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void tweetDailyTopQuestion4() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 4");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 9
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.iOSdigest), TwitterAccountEnum.iOSdigest.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestJavaScript), TwitterAccountEnum.BestJavaScript.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.PerlDaily), TwitterAccountEnum.PerlDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.LispDaily), TwitterAccountEnum.LispDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestRubyOnRails), TwitterAccountEnum.BestRubyOnRails.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.LandOfWordpress), TwitterAccountEnum.LandOfWordpress.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.GoogleDigest), TwitterAccountEnum.GoogleDigest.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestClojure), TwitterAccountEnum.BestClojure.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.FacebookDigest), TwitterAccountEnum.FacebookDigest.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestMultithread), TwitterAccountEnum.BestMultithread.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.AndroidFact), TwitterAccountEnum.AndroidFact.name());

        logger.info("Finished tweet schedule - 4");
    }

    @Scheduled(cron = "0 0 22 * * *")
    public void tweetDailyTopQuestion5() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 5");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 8
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.AspnetDaily), TwitterAccountEnum.AspnetDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.RegexDaily), TwitterAccountEnum.RegexDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestOfRuby), TwitterAccountEnum.BestOfRuby.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestPHP), TwitterAccountEnum.BestPHP.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestMaven), TwitterAccountEnum.BestMaven.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.InTheAppleWorld), TwitterAccountEnum.InTheAppleWorld.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.MysqlDaily), TwitterAccountEnum.MysqlDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.LandOfSeo), TwitterAccountEnum.LandOfSeo.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.CryptoFact), TwitterAccountEnum.CryptoFact.name());

        logger.info("Finished tweet schedule - 5");
    }

}
