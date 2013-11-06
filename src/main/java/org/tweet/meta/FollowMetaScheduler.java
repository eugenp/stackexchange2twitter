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

    @Scheduled(cron = "0 20 12,16,20 * * *")
    public void newScheduleThree() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - three");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 6
        service.followBestUser(TwitterAccountEnum.BestOfLinux.name(), TwitterTag.linux.name());
        service.followBestUser(TwitterAccountEnum.BestOfCss.name(), TwitterTag.css.name());
        service.followBestUser(TwitterAccountEnum.CryptoFact.name(), TwitterTag.cryptography.name());
        service.followBestUser(TwitterAccountEnum.BestOfCloud.name(), TwitterTag.ec2.name());
        // service.followBestUser(TwitterAccountEnum.PythonDaily.name());
        // service.followBestUser(TwitterAccountEnum.AspnetDaily.name(), TwitterTag.aspnet.name());

        logger.info("Finished new retweet schedule - three");
    }

    @Scheduled(cron = "0 20 13,17,21 * * *")
    public void newScheduleFour() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - four");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 7
        service.followBestUser(TwitterAccountEnum.BestOfJava.name(), TwitterTag.java.name());
        service.followBestUser(TwitterAccountEnum.BestOfSecurity.name(), TwitterTag.security.name());
        service.followBestUser(TwitterAccountEnum.BestOfHTML5.name(), TwitterTag.html5.name());
        // service.followBestUser(TwitterAccountEnum.GoogleDigest.name(), TwitterTag.google.name());
        service.followBestUser(TwitterAccountEnum.LandOfSeo.name(), TwitterTag.seo.name());
        service.followBestUser(TwitterAccountEnum.LandOfWordpress.name(), TwitterTag.wordpress.name());
        service.followBestUser(TwitterAccountEnum.MysqlDaily.name(), TwitterTag.mysql.name());
        service.followBestUser(TwitterAccountEnum.SpringTip.name(), TwitterTag.springsecurity.name());

        // service.followBestUser(TwitterAccountEnum.BestJPA.name(), TwitterTag.springsecurity.name());
        // service.followBestUser(TwitterAccountEnum.PerformanceTip.name(), TwitterTag.springsecurity.name());

        logger.info("Finished new retweet schedule - four");
    }

    @Scheduled(cron = "0 20 14,16,18,20,22 * * *")
    public void newScheduleFive1() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - five 1");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 10
        service.followBestUser(TwitterAccountEnum.BestRubyOnRails.name(), TwitterTag.rubyonrails.name());
        service.followBestUser(TwitterAccountEnum.BestAlgorithms.name(), TwitterTag.algorithms.name());
        service.followBestUser(TwitterAccountEnum.BestGit.name(), TwitterTag.git.name());
        service.followBestUser(TwitterAccountEnum.BestClojure.name(), TwitterTag.clojure.name());
        service.followBestUser(TwitterAccountEnum.BestJavaScript.name(), TwitterTag.javascript.name());
        service.followBestUser(TwitterAccountEnum.BestJSON.name(), TwitterTag.json.name());
        service.followBestUser(TwitterAccountEnum.BestPHP.name(), TwitterTag.php.name());
        service.followBestUser(TwitterAccountEnum.DotNetFact.name(), TwitterTag.dotnet.name());
        service.followBestUser(TwitterAccountEnum.HadoopDaily.name(), TwitterTag.hadoop.name());
        service.followBestUser(TwitterAccountEnum.BestNoSQL.name(), TwitterTag.nosql.name());

        // experimental
        // service.followBestUser(TwitterAccountEnum.HttpClient4.name(), TwitterTag.springsecurity.name());

        // even newer and more experimental
        service.followBestUser(TwitterAccountEnum.BestMultithread.name(), TwitterTag.multithreading.name());
        // service.retweetAnyByWord(TwitterAccountEnum.ParsingDaily.name(), TwitterTag.springsecurity.name());

        logger.info("Finished new retweet schedule - five 1");
    }

    @Scheduled(cron = "0 20 0,16,18,20,22 * * *")
    public void newScheduleFive2() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - five 2");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 11
        service.followBestUser(TwitterAccountEnum.BestSQL.name(), TwitterTag.sql.name());
        service.followBestUser(TwitterAccountEnum.BestXML.name(), TwitterTag.xml.name());
        // service.followBestUser(TwitterAccountEnum.InTheAppleWorld.name(), TwitterTag.apple.name());
        service.followBestUser(TwitterAccountEnum.iOSdigest.name(), TwitterTag.ios.name());
        service.followBestUser(TwitterAccountEnum.LispDaily.name(), TwitterTag.lisp.name());

        service.followBestUser(TwitterAccountEnum.RegexDaily.name(), TwitterTag.regex.name());
        service.followBestUser(TwitterAccountEnum.ObjectiveCDaily.name(), TwitterTag.objectivec.name());

        // experimental
        service.followBestUser(TwitterAccountEnum.AndroidFact.name(), TwitterTag.android.name());
        service.followBestUser(TwitterAccountEnum.PerlDaily.name(), TwitterTag.perl.name());
        service.followBestUser(TwitterAccountEnum.jQueryDaily.name(), TwitterTag.jquery.name());

        logger.info("Finished new retweet schedule - five 2");
    }

    //

    @Scheduled(cron = "0 20 14 * * *")
    public void newSchedule1() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - 1");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 5
        service.followBestUser(TwitterAccountEnum.BestAWS.name(), TwitterTag.aws.name());
        service.followBestUser(TwitterAccountEnum.BestScala.name(), TwitterTag.scala.name());
        service.followBestUser(TwitterAccountEnum.FacebookDigest.name(), TwitterTag.facebook.name());
        service.followBestUser(TwitterAccountEnum.HTMLdaily.name(), TwitterTag.html5.name());

        logger.info("Finished new retweet schedule - 1");
    }

    @Scheduled(cron = "0 20 16 * * *")
    public void newSchedule2() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - 2");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 5
        service.followBestUser(TwitterAccountEnum.BestAWS.name(), TwitterTag.ec2.name());
        service.followBestUser(TwitterAccountEnum.BestScala.name(), TwitterTag.akka.name());
        service.followBestUser(TwitterAccountEnum.FacebookDigest.name(), TwitterTag.facebook.name());
        service.followBestUser(TwitterAccountEnum.HTMLdaily.name(), TwitterTag.html.name());

        service.followBestUser(TwitterAccountEnum.MathDaily.name(), TwitterTag.math.name());

        logger.info("Finished new retweet schedule - 2");
    }

    @Scheduled(cron = "0 20 18 * * *")
    public void newSchedule3() throws JsonProcessingException, IOException {
        logger.info("Starting new retweet schedule - 3");

        if (env.getProperty(MODE_MAINTAINANCE_KEY, Boolean.class)) {
            logger.warn("Maintainance Mode Active - skipping schedule");
            return;
        }

        // 5
        service.followBestUser(TwitterAccountEnum.HTMLdaily.name(), TwitterTag.html5.name());

        logger.info("Finished new retweet schedule - 3");
    }

    // for accounts - not yet: BestBash,EclipseFacts,BestJPA,BestMaven,BestOfRuby,SpringTip,ServerFaultBest,JavaTopSO,RESTDaily,BestOfCocoa

}
