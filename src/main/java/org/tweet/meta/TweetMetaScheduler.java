package org.tweet.meta;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.stackexchange.util.SimpleTwitterAccount;
import org.tweet.meta.service.TweetMetaService;
import org.tweet.spring.util.SpringProfileUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Profile(SpringProfileUtil.DEPLOYED)
public class TweetMetaScheduler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TweetMetaService service;

    public TweetMetaScheduler() {
        super();
    }

    // API

    // git - not 100% sure that hashtag will only return relevant tweets - look into this further
    // for accounts - not yet: BestBash,BestEclipse,BestGit,BestJPA,BestMaven,BestOfRuby,SpringAtSO,ServerFaultBest,JavaTopSO,RESTDaily

    @Scheduled(cron = "0 0 9,12,15,18,21 * * *")
    public void tweetMeta1() throws JsonProcessingException, IOException {
        logger.info("Starting retweet schedule - 1");

        // 9
        service.retweetByHashtag(SimpleTwitterAccount.BestAWS.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestClojure.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestJavaScript.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestJSON.name()); // not 100% sure
        service.retweetByHashtag(SimpleTwitterAccount.BestPHP.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestRubyOnRails.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestScala.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestAlgorithms.name()); // not 100% sure
        service.retweetByHashtag(SimpleTwitterAccount.HTMLdaily.name()); // not 100% sure
        service.retweetByHashtag(SimpleTwitterAccount.PerlDaily.name()); // not 100% sure

        logger.info("Finished retweet schedule - 1");
    }

    @Scheduled(cron = "0 0 10,13,16,19,22 * * *")
    public void tweetMeta2() throws JsonProcessingException, IOException {
        logger.info("Starting retweet schedule - 2");

        // 8
        service.retweetByHashtag(SimpleTwitterAccount.BestSQL.name());
        service.retweetByHashtag(SimpleTwitterAccount.InTheAppleWorld.name());
        service.retweetByHashtag(SimpleTwitterAccount.iOSdigest.name());
        service.retweetByHashtag(SimpleTwitterAccount.jQueryDaily.name());
        service.retweetByHashtag(SimpleTwitterAccount.PythonDaily.name());
        service.retweetByHashtag(SimpleTwitterAccount.RegexDaily.name());
        service.retweetByHashtag(SimpleTwitterAccount.ObjectiveCDaily.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestOfJava.name());
        service.retweetByHashtag(SimpleTwitterAccount.FacebookDigest.name());
        service.retweetByHashtag(SimpleTwitterAccount.MathDaily.name());

        logger.info("Finished retweet schedule - 2");
    }

    @Scheduled(cron = "0 0 11,14,17,20,23 * * *")
    public void tweetMeta3() throws JsonProcessingException, IOException {
        logger.info("Starting retweet schedule - 3");

        // 9
        service.retweetByHashtag(SimpleTwitterAccount.BestOfCloud.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestOfSecurity.name());
        service.retweetByHashtag(SimpleTwitterAccount.LandOfWordpress.name());
        service.retweetByHashtag(SimpleTwitterAccount.LispDaily.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestNoSQL.name());
        service.retweetByHashtag(SimpleTwitterAccount.GoogleDigest.name());
        service.retweetByHashtag(SimpleTwitterAccount.MysqlDaily.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestOfLinux.name());
        service.retweetByHashtag(SimpleTwitterAccount.ParsingDaily.name());

        logger.info("Finished retweet schedule - 3");
    }

}
