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

    @Scheduled(cron = "0 10 15,17,19,21,23 * * *")
    public void newSchedule0() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - 0");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 3
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfSecurity.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.GoogleDigest.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfCloud.name());

        logger.info("Finished new retweet schedule - 0");
    }

    @Scheduled(cron = "0 10 14 * * *")
    public void newSchedule1() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - 1");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 8
        service.retweetAnyByHashtag(TwitterAccountEnum.BestAWS.name(), TwitterTag.aws.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfHTML5.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestScala.name(), TwitterTag.scala.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestNoSQL.name(), TwitterTag.nosql.name());

        service.retweetAnyByHashtag(TwitterAccountEnum.FacebookDigest.name(), TwitterTag.facebook.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.HTMLdaily.name(), TwitterTag.html5.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.LandOfSeo.name());

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

        // 8
        service.retweetAnyByHashtag(TwitterAccountEnum.BestAWS.name(), TwitterTag.ec2.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfHTML5.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestScala.name(), TwitterTag.akka.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestNoSQL.name());

        service.retweetAnyByHashtag(TwitterAccountEnum.FacebookDigest.name(), TwitterTag.opengraph.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.HTMLdaily.name(), TwitterTag.html.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.LandOfSeo.name());

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

        // 8
        service.retweetAnyByHashtag(TwitterAccountEnum.BestAWS.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfHTML5.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestScala.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestNoSQL.name());

        service.retweetAnyByHashtag(TwitterAccountEnum.FacebookDigest.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.HTMLdaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.LandOfSeo.name());

        service.retweetAnyByHashtag(TwitterAccountEnum.MathDaily.name());

        logger.info("Finished new retweet schedule - 3");
    }

    @Scheduled(cron = "0 10 20 * * *")
    public void newSchedule4() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - 4");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 0

        logger.info("Finished new retweet schedule - 4");
    }

    @Scheduled(cron = "0 10 22 * * *")
    public void newSchedule5() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - 5");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 0

        logger.info("Finished new retweet schedule - 5");
    }

    // OLD

    // for accounts - not yet: BestBash,BestEclipse,BestJPA,BestMaven,BestOfRuby,SpringAtSO,ServerFaultBest,JavaTopSO,RESTDaily,BestOfCocoa

    @Scheduled(cron = "0 3 9,12,15,18,21 * * *")
    public void tweetMeta1() throws JsonProcessingException, IOException {
        logger.info("Starting retweet schedule - 1");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 8
        service.retweetAnyByHashtag(TwitterAccountEnum.BestClojure.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestJavaScript.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestJSON.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestPHP.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestRubyOnRails.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestAlgorithms.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestMultithread.name());

        service.retweetAnyByHashtag(TwitterAccountEnum.BestGit.name()); // new and experimentally active

        logger.info("Finished retweet schedule - 1");
    }

    @Scheduled(cron = "0 3 10,13,16,19,22 * * *")
    public void tweetMeta2() throws JsonProcessingException, IOException {
        logger.info("Starting retweet schedule - 2");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 9
        service.retweetAnyByHashtag(TwitterAccountEnum.BestSQL.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.InTheAppleWorld.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.iOSdigest.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.jQueryDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.PythonDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.RegexDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.ObjectiveCDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfJava.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.PerlDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.HadoopDaily.name());

        logger.info("Finished retweet schedule - 2");
    }

    @Scheduled(cron = "0 3 11,14,17,20,23 * * *")
    public void tweetMeta3() throws JsonProcessingException, IOException {
        logger.info("Starting retweet schedule - 3");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 8
        service.retweetAnyByHashtag(TwitterAccountEnum.LandOfWordpress.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.LispDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.MysqlDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfLinux.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestXML.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfCss.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.CryptoFact.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.AndroidFact.name());
        // disabled for now - no results // service.retweetAnyByHashtag(TwitterAccountEnum.ParsingDaily.name());

        logger.info("Finished retweet schedule - 3");
    }

}
