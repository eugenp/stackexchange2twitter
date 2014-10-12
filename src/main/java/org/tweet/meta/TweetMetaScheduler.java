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

    @Scheduled(cron = "0 10 10,14,18,22,2,6 * * *")
    public void processLargeAccounts() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - large accounts");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // large accounts - 8
        service.retweetAnyByHashtag(TwitterAccountEnum.ThinkJavaScript.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.ClojureFact.name());

        service.retweetAnyByWord(TwitterAccountEnum.ScalaFact.name(), TwitterTag.scala.name()); // newly moved to by word (11.11)
        service.retweetAnyByHashtag(TwitterAccountEnum.ScalaFact.name(), TwitterTag.akka.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.ScalaFact.name());

        service.retweetAnyByHashtag(TwitterAccountEnum.JavaFact.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.PythonDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.SpringTip.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestGit.name()); // new and experimentally active
        service.retweetAnyByHashtag(TwitterAccountEnum.BestAlgorithms.name());

        logger.info("Finished new retweet schedule - large accounts");
    }

    @Scheduled(cron = "0 30 10,14,18,22,2,6 * * *")
    public void processMediumAccounts() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - medium accounts");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // medium accounts - 11
        service.retweetAnyByHashtag(TwitterAccountEnum.HadoopDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfHTML5.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestRubyOnRails.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.LispDaily.name()); // already 2
        service.retweetAnyByHashtag(TwitterAccountEnum.ObjectiveCDaily.name()); // already 2
        service.retweetAnyByWord(TwitterAccountEnum.jQueryDaily.name()); // (26.10)
        service.retweetAnyByHashtag(TwitterAccountEnum.CryptoFact.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestSQL.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestPHP.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.MysqlDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.LinuxFact.name());

        logger.info("Finished new retweet schedule - medium accounts");
    }

    // TODO: SORT

    @Scheduled(cron = "0 10 9,13,17,21,1,5 * * *")
    public void processSmallAccounts1() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - small accounts 1");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 16
        service.retweetAnyByHashtag(TwitterAccountEnum.CssFact.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfCloud.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.AspnetDaily.name(), TwitterTag.aspnet.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.SecurityFact.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.GoogleDigest.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.LandOfSeo.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.LandOfWordpress.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestJSON.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.DotNetFact.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestNoSQL.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestXML.name()); // already 2
        service.retweetAnyByHashtag(TwitterAccountEnum.InTheAppleWorld.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.iOSdigest.name()); // already 2
        service.retweetAnyByHashtag(TwitterAccountEnum.RegexDaily.name()); // already 2
        service.retweetAnyByWord(TwitterAccountEnum.HttpClient4.name()); // newly moved to word
        service.retweetAnyByWord(TwitterAccountEnum.MultithreadFact.name());
        // service.retweetAnyByWord(TwitterAccountEnum.ParsingDaily.name()); // no - not good results - activate it only after the programming recommender is up and running

        logger.info("Finished new retweet schedule - small accounts 1");
    }

    @Scheduled(cron = "0 30 9,13,17,21,1,5 * * *")
    public void processSmallAccounts2() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - small accounts 2");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 16
        service.retweetAnyByWord(TwitterAccountEnum.AndroidFact.name(), TwitterTag.android.name()); // already 2
        service.retweetAnyByWord(TwitterAccountEnum.PerlDaily.name()); // (26.10) before moving to byWord, I was getting emails about to few = 5
        service.retweetAnyByHashtag(TwitterAccountEnum.BestAWS.name(), TwitterTag.aws.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.FacebookDigest.name(), TwitterTag.facebook.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.HTMLdaily.name(), TwitterTag.html5.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.MathDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestAWS.name(), TwitterTag.ec2.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.FacebookDigest.name(), TwitterTag.opengraph.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.HTMLdaily.name(), TwitterTag.html.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.MathDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestAWS.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.FacebookDigest.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.HTMLdaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.MathDaily.name());
        service.retweetAnyByWord(TwitterAccountEnum.BestJPA.name()); // new
        service.retweetAnyByHashtag(TwitterAccountEnum.PerformanceTip.name()); // new

        logger.info("Finished new retweet schedule - small accounts 2");
    }

    // for accounts - not yet: EclipseFacts,MavenFact,BestOfRuby,ServerFaultBest,JavaTopSO,RESTDaily,BestOfCocoa

}
