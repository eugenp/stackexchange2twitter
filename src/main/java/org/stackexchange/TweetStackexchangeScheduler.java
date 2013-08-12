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

        logger.info("Finished tweet schedule - 1");
    }

    /**
     * - these accounts are not StackExchange specific
     */
    @Scheduled(cron = "0 0 13,17 * * *")
    public void tweetDailyTopQuestion2() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 2");

        // 10
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestScala), TwitterAccountEnum.BestScala.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.jQueryDaily), TwitterAccountEnum.jQueryDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.RESTDaily), TwitterAccountEnum.RESTDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestEclipse), TwitterAccountEnum.BestEclipse.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestGit), TwitterAccountEnum.BestGit.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestJPA), TwitterAccountEnum.BestJPA.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestAlgorithms), TwitterAccountEnum.BestAlgorithms.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.HibernateDaily), TwitterAccountEnum.HibernateDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.FacebookDigest), TwitterAccountEnum.FacebookDigest.name());

        final StackSite randomSite = GenericUtil.pickOneGeneric(twitterAccountToStackSites(TwitterAccountEnum.BestBash));
        service.tweetAnyTopQuestionBySiteAndTag(randomSite, TwitterAccountEnum.BestBash.name());

        logger.info("Finished tweet schedule - 2");
    }

    /**
     * - these accounts are not StackExchange specific
     */
    @Scheduled(cron = "0 0 14,18 * * *")
    public void tweetDailyTopQuestion3() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 3");

        // 9
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestOfJava), TwitterAccountEnum.BestOfJava.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.SpringAtSO), TwitterAccountEnum.SpringAtSO.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestNoSQL), TwitterAccountEnum.BestNoSQL.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.PythonDaily), TwitterAccountEnum.PythonDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestAWS), TwitterAccountEnum.BestAWS.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.ObjectiveCDaily), TwitterAccountEnum.ObjectiveCDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestOfSecurity), TwitterAccountEnum.BestOfSecurity.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestOfCloud), TwitterAccountEnum.BestOfCloud.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.HTMLdaily), TwitterAccountEnum.HTMLdaily.name());

        logger.info("Finished tweet schedule - 3");
    }

    @Scheduled(cron = "0 0 15,19 * * *")
    public void tweetDailyTopQuestion4() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 4");

        // 8
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.iOSdigest), TwitterAccountEnum.iOSdigest.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestJavaScript), TwitterAccountEnum.BestJavaScript.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.PerlDaily), TwitterAccountEnum.PerlDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.LispDaily), TwitterAccountEnum.LispDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestRubyOnRails), TwitterAccountEnum.BestRubyOnRails.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.LandOfWordpress), TwitterAccountEnum.LandOfWordpress.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.GoogleDigest), TwitterAccountEnum.GoogleDigest.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestClojure), TwitterAccountEnum.BestClojure.name());

        logger.info("Finished tweet schedule - 4");
    }

    @Scheduled(cron = "0 0 16,20 * * *")
    public void tweetDailyTopQuestion5() throws JsonProcessingException, IOException {
        logger.info("Starting tweet schedule - 5");

        // 8
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.AspnetDaily), TwitterAccountEnum.AspnetDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.RegexDaily), TwitterAccountEnum.RegexDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestOfRuby), TwitterAccountEnum.BestOfRuby.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestPHP), TwitterAccountEnum.BestPHP.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestMaven), TwitterAccountEnum.BestMaven.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.InTheAppleWorld), TwitterAccountEnum.InTheAppleWorld.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.MysqlDaily), TwitterAccountEnum.MysqlDaily.name());
        service.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.LandOfSeo), TwitterAccountEnum.LandOfSeo.name());

        logger.info("Finished tweet schedule - 5");
    }

}
