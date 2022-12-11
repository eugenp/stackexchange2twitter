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
import org.tweet.meta.service.FollowLiveService;
import org.tweet.spring.util.SpringProfileUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Profile(SpringProfileUtil.WRITE_PRODUCTION)
public class FollowMetaScheduler {
    private static final String MODE_MAINTAINANCE_KEY = "mode.maintainance.rt";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FollowLiveService service;

    @Autowired
    private Environment env;

    public FollowMetaScheduler() {
        super();
    }

    // API

    @Scheduled(cron = "0 20 2,6,10,14,18,22 * * *")
    public void processLargeAccounts() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - large accounts");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 7
        service.followBestUser(TwitterAccountEnum.ThinkJavaScript.name(), TwitterTag.javascript.name());
        service.followBestUser(TwitterAccountEnum.ClojureFact.name(), TwitterTag.clojure.name());
        service.followBestUser(TwitterAccountEnum.ScalaFact.name(), TwitterTag.scala.name());
        service.followBestUser(TwitterAccountEnum.JavaFact.name(), TwitterTag.java.name());
        // service.followBestUser(TwitterAccountEnum.PythonDaily.name());
        service.followBestUser(TwitterAccountEnum.SpringTip.name(), TwitterTag.springsecurity.name());
        service.followBestUser(TwitterAccountEnum.BestGit.name(), "git programming");
        service.followBestUser(TwitterAccountEnum.BestAlgorithms.name(), TwitterTag.algorithms.name());

        logger.info("Finished new retweet schedule - large accounts");
    }

    @Scheduled(cron = "0 40 1,5,9,13,17,21 * * *")
    public void processMediumAccounts() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - medium accounts");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 11
        service.followBestUser(TwitterAccountEnum.HadoopDaily.name(), TwitterTag.hadoop.name());
        service.followBestUser(TwitterAccountEnum.BestOfHTML5.name(), TwitterTag.html5.name());
        service.followBestUser(TwitterAccountEnum.BestRubyOnRails.name(), TwitterTag.rubyonrails.name());
        service.followBestUser(TwitterAccountEnum.LispDaily.name(), TwitterTag.lisp.name());
        service.followBestUser(TwitterAccountEnum.ObjectiveCDaily.name(), TwitterTag.objectivec.name());
        service.followBestUser(TwitterAccountEnum.jQueryDaily.name(), TwitterTag.jquery.name());
        service.followBestUser(TwitterAccountEnum.CryptoFact.name(), TwitterTag.cryptography.name());
        service.followBestUser(TwitterAccountEnum.BestSQL.name(), TwitterTag.sql.name());
        service.followBestUser(TwitterAccountEnum.BestPHP.name(), TwitterTag.php.name());
        service.followBestUser(TwitterAccountEnum.MysqlDaily.name(), TwitterTag.mysql.name());
        service.followBestUser(TwitterAccountEnum.LinuxFact.name(), TwitterTag.linux.name());

        logger.info("Finished new retweet schedule - medium accounts");
    }

    @Scheduled(cron = "0 40 0,4,8,12,16,20 * * *")
    public void processSmallAccounts1() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - small accounts 1");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        service.followBestUser(TwitterAccountEnum.CssFact.name(), TwitterTag.css.name());
        service.followBestUser(TwitterAccountEnum.BestOfCloud.name(), TwitterTag.ec2.name());
        service.followBestUser(TwitterAccountEnum.SecurityFact.name(), TwitterTag.security.name());
        service.followBestUser(TwitterAccountEnum.LandOfSeo.name(), TwitterTag.seo.name());
        service.followBestUser(TwitterAccountEnum.LandOfWordpress.name(), TwitterTag.wordpress.name());
        service.followBestUser(TwitterAccountEnum.BestJSON.name(), TwitterTag.json.name());
        service.followBestUser(TwitterAccountEnum.DotNetFact.name(), TwitterTag.dotnet.name());
        service.followBestUser(TwitterAccountEnum.BestNoSQL.name(), TwitterTag.nosql.name());
        service.followBestUser(TwitterAccountEnum.MultithreadFact.name(), TwitterTag.multithreading.name());
        service.followBestUser(TwitterAccountEnum.ParsingDaily.name(), "parsing programming");
        service.followBestUser(TwitterAccountEnum.BestXML.name(), TwitterTag.xml.name());
        service.followBestUser(TwitterAccountEnum.iOSdigest.name(), TwitterTag.ios.name());
        service.followBestUser(TwitterAccountEnum.RegexDaily.name(), TwitterTag.regex.name());
        service.followBestUser(TwitterAccountEnum.AndroidFact.name(), TwitterTag.android.name());
        service.followBestUser(TwitterAccountEnum.PerlDaily.name(), TwitterTag.perl.name());
        service.followBestUser(TwitterAccountEnum.BestAWS.name(), TwitterTag.aws.name());

        logger.info("Finished new retweet schedule - small accounts 1");
    }

    @Scheduled(cron = "0 40 3,7,11,15,19,23 * * *")
    public void processSmallAccounts2() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - small accounts 2");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        service.followBestUser(TwitterAccountEnum.FacebookDigest.name(), TwitterTag.facebook.name());
        service.followBestUser(TwitterAccountEnum.HTMLdaily.name(), TwitterTag.html5.name());
        service.followBestUser(TwitterAccountEnum.BestAWS.name(), TwitterTag.ec2.name());
        service.followBestUser(TwitterAccountEnum.ScalaFact.name(), TwitterTag.akka.name());
        service.followBestUser(TwitterAccountEnum.FacebookDigest.name(), TwitterTag.facebook.name());
        service.followBestUser(TwitterAccountEnum.HTMLdaily.name(), TwitterTag.html.name());
        service.followBestUser(TwitterAccountEnum.MathDaily.name(), TwitterTag.math.name());
        service.followBestUser(TwitterAccountEnum.HTMLdaily.name(), TwitterTag.html5.name());

        // service.followBestUser(TwitterAccountEnum.HttpClient4.name(), TwitterTag.springsecurity.name());
        // service.followBestUser(TwitterAccountEnum.AspnetDaily.name(), TwitterTag.aspnet.name());
        // service.followBestUser(TwitterAccountEnum.GoogleDigest.name(), TwitterTag.google.name());
        // service.followBestUser(TwitterAccountEnum.BestJPA.name(), TwitterTag.springsecurity.name());
        // service.followBestUser(TwitterAccountEnum.PerformanceTip.name(), TwitterTag.springsecurity.name());
        // service.followBestUser(TwitterAccountEnum.InTheAppleWorld.name(), TwitterTag.apple.name());

        logger.info("Finished new retweet schedule - small accounts 2");
    }

    // for accounts - not yet: BestBash,EclipseFacts,BestJPA,MavenFact,BestOfRuby,SpringTip,ServerFaultBest,JavaTopSO,RESTDaily,BestOfCocoa

}
