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
import org.stackexchange.service.TweetStackexchangeLiveService;
import org.stackexchange.util.GenericUtil;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TagRetrieverService;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Profile(SpringProfileUtil.WRITE_PRODUCTION)
public class TweetStackexchangeScheduler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TweetStackexchangeLiveService service;

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
        service.tweetTopQuestionBySite(twitterAccountToStackSite(TwitterAccountEnum.ServerFaultBest), TwitterAccountEnum.ServerFaultBest.name());
        service.tweetTopQuestionBySite(twitterAccountToStackSite(TwitterAccountEnum.AskUbuntuBest), TwitterAccountEnum.AskUbuntuBest.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.JavaTopSO), TwitterAccountEnum.JavaTopSO.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestJSON), TwitterAccountEnum.BestJSON.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestOfLinux), TwitterAccountEnum.BestOfLinux.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.DjangoDaily), TwitterAccountEnum.DjangoDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.ParsingDaily), TwitterAccountEnum.ParsingDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.MathDaily), TwitterAccountEnum.MathDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestSQL), TwitterAccountEnum.BestSQL.name());

        logger.info("Finished tweet schedule - 1");
    }

    /**
     * - these accounts are not StackExchange specific
     */
    @Scheduled(cron = "0 0 13,17 * * *")
    public void tweetDailyTopQuestion2() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 2");

        // 10
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestScala), TwitterAccountEnum.BestScala.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.jQueryDaily), TwitterAccountEnum.jQueryDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.RESTDaily), TwitterAccountEnum.RESTDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestEclipse), TwitterAccountEnum.BestEclipse.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestGit), TwitterAccountEnum.BestGit.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestJPA), TwitterAccountEnum.BestJPA.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestAlgorithms), TwitterAccountEnum.BestAlgorithms.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.HibernateDaily), TwitterAccountEnum.HibernateDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.FacebookDigest), TwitterAccountEnum.FacebookDigest.name());

        final StackSite randomSite = GenericUtil.pickOneGeneric(twitterAccountToStackSites(TwitterAccountEnum.BestBash));
        service.tweetTopQuestionBySiteAndTag(randomSite, TwitterAccountEnum.BestBash.name());

        logger.info("Finished tweet schedule - 2");
    }

    /**
     * - these accounts are not StackExchange specific
     */
    @Scheduled(cron = "0 0 14,18 * * *")
    public void tweetDailyTopQuestion3() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 3");

        // 9
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestOfJava), TwitterAccountEnum.BestOfJava.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.SpringAtSO), TwitterAccountEnum.SpringAtSO.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestNoSQL), TwitterAccountEnum.BestNoSQL.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.PythonDaily), TwitterAccountEnum.PythonDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestAWS), TwitterAccountEnum.BestAWS.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.ObjectiveCDaily), TwitterAccountEnum.ObjectiveCDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestOfSecurity), TwitterAccountEnum.BestOfSecurity.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestOfCloud), TwitterAccountEnum.BestOfCloud.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.HTMLdaily), TwitterAccountEnum.HTMLdaily.name());

        logger.info("Finished tweet schedule - 3");
    }

    @Scheduled(cron = "0 0 15,19 * * *")
    public void tweetDailyTopQuestion4() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 4");

        // 8
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.iOSdigest), TwitterAccountEnum.iOSdigest.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestJavaScript), TwitterAccountEnum.BestJavaScript.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.PerlDaily), TwitterAccountEnum.PerlDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.LispDaily), TwitterAccountEnum.LispDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestRubyOnRails), TwitterAccountEnum.BestRubyOnRails.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.LandOfWordpress), TwitterAccountEnum.LandOfWordpress.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.GoogleDigest), TwitterAccountEnum.GoogleDigest.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestClojure), TwitterAccountEnum.BestClojure.name());

        logger.info("Finished tweet schedule - 4");
    }

    @Scheduled(cron = "0 0 16,20 * * *")
    public void tweetDailyTopQuestion5() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 5");

        // 8
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.AspnetDaily), TwitterAccountEnum.AspnetDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.RegexDaily), TwitterAccountEnum.RegexDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestOfRuby), TwitterAccountEnum.BestOfRuby.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestPHP), TwitterAccountEnum.BestPHP.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestMaven), TwitterAccountEnum.BestMaven.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.InTheAppleWorld), TwitterAccountEnum.InTheAppleWorld.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.MysqlDaily), TwitterAccountEnum.MysqlDaily.name());
        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.LandOfSeo), TwitterAccountEnum.LandOfSeo.name());

        logger.info("Finished tweet schedule - 5");
    }

}
