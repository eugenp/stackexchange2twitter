package org.tweet.meta;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.stackexchange.util.TwitterAccountEnum;
import org.stackexchange.util.TwitterTag;
import org.tweet.meta.service.TweetMetaLiveService;
import org.tweet.spring.util.SpringProfileUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Profile(SpringProfileUtil.WRITE_PRODUCTION)
public class TweetMetaScheduler {
    private static final String MODE_MAINTAINANCE_KEY = "mode.maintainance.rt";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TweetMetaLiveService service;

    @Autowired
    private Environment env;

    public TweetMetaScheduler() {
        super();
    }

    // API

    @Scheduled(cron = "0 10 12,16,20 * * *")
    public void newScheduleThree() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - three");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 6
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfLinux.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfCss.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.CryptoFact.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfCloud.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.PythonDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.AspnetDaily.name(), TwitterTag.aspnet.name());

        logger.info("Finished new retweet schedule - three");
    }

    @Scheduled(cron = "0 10 13,17,21 * * *")
    public void newScheduleFour() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - four");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 7
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfJava.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfSecurity.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfHTML5.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.GoogleDigest.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.LandOfSeo.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.LandOfWordpress.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.MysqlDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.SpringTip.name());

        // new and very much experimental
        service.retweetAnyByHashtag(TwitterAccountEnum.BestJPA.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.PerformanceTip.name()); // new

        logger.info("Finished new retweet schedule - four");
    }

    @Scheduled(cron = "0 10 14,16,18,20,22 * * *")
    public void newScheduleFive1() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - five 1");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 10
        service.retweetAnyByHashtag(TwitterAccountEnum.BestRubyOnRails.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestAlgorithms.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestGit.name()); // new and experimentally active
        service.retweetAnyByHashtag(TwitterAccountEnum.BestClojure.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestJavaScript.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestJSON.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestPHP.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.DotNetFact.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.HadoopDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestNoSQL.name());

        // experimental
        service.retweetAnyByHashtag(TwitterAccountEnum.HttpClient4.name());

        // even newer and more experimental
        service.retweetAnyByWord(TwitterAccountEnum.BestMultithread.name());
        // service.retweetAnyByWord(TwitterAccountEnum.ParsingDaily.name()); // no - not good results - activate it only after the programming recommender is up and running

        logger.info("Finished new retweet schedule - five 1");
    }

    @Scheduled(cron = "0 10 0,16,18,20,22 * * *")
    public void newScheduleFive2() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - five 2");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 11
        service.retweetAnyByHashtag(TwitterAccountEnum.BestSQL.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestXML.name()); // already 2
        service.retweetAnyByHashtag(TwitterAccountEnum.InTheAppleWorld.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.iOSdigest.name()); // already 2
        service.retweetAnyByHashtag(TwitterAccountEnum.LispDaily.name()); // already 2

        service.retweetAnyByHashtag(TwitterAccountEnum.RegexDaily.name()); // already 2
        service.retweetAnyByHashtag(TwitterAccountEnum.ObjectiveCDaily.name()); // already 2

        // experimental
        service.retweetAnyByWord(TwitterAccountEnum.AndroidFact.name(), TwitterTag.android.name()); // already 2
        service.retweetAnyByWord(TwitterAccountEnum.PerlDaily.name()); // (26.10) before moving to byWord, I was getting emails about to few = 5
        service.retweetAnyByWord(TwitterAccountEnum.jQueryDaily.name()); // (26.10)

        logger.info("Finished new retweet schedule - five 2");
    }

    //

    @Scheduled(cron = "0 10 14 * * *")
    public void newSchedule1() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - 1");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 5
        service.retweetAnyByHashtag(TwitterAccountEnum.BestAWS.name(), TwitterTag.aws.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestScala.name(), TwitterTag.scala.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.FacebookDigest.name(), TwitterTag.facebook.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.HTMLdaily.name(), TwitterTag.html5.name());

        service.retweetAnyByHashtag(TwitterAccountEnum.MathDaily.name());

        logger.info("Finished new retweet schedule - 1");
    }

    @Scheduled(cron = "0 10 16 * * *")
    public void newSchedule2() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - 2");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 5
        service.retweetAnyByHashtag(TwitterAccountEnum.BestAWS.name(), TwitterTag.ec2.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestScala.name(), TwitterTag.akka.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.FacebookDigest.name(), TwitterTag.opengraph.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.HTMLdaily.name(), TwitterTag.html.name());

        service.retweetAnyByHashtag(TwitterAccountEnum.MathDaily.name());

        logger.info("Finished new retweet schedule - 2");
    }

    @Scheduled(cron = "0 10 18 * * *")
    public void newSchedule3() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - 3");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 5
        service.retweetAnyByHashtag(TwitterAccountEnum.BestAWS.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestScala.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.FacebookDigest.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.HTMLdaily.name());

        service.retweetAnyByHashtag(TwitterAccountEnum.MathDaily.name());

        logger.info("Finished new retweet schedule - 3");
    }

    // for accounts - not yet: BestBash,EclipseFacts,BestJPA,BestMaven,BestOfRuby,SpringTip,ServerFaultBest,JavaTopSO,RESTDaily,BestOfCocoa

}
