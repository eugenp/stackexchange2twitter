package org.tweet.meta;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.service.TweetMetaLiveService;
import org.tweet.spring.util.SpringProfileUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Profile(SpringProfileUtil.DEPLOYED)
public class TweetMetaScheduler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TweetMetaLiveService service;

    public TweetMetaScheduler() {
        super();
    }

    // API

    // git - not 100% sure that hashtag will only return relevant tweets - look into this further
    // for accounts - not yet: BestBash,BestEclipse,BestGit,BestJPA,BestMaven,BestOfRuby,SpringAtSO,ServerFaultBest,JavaTopSO,RESTDaily

    @Scheduled(cron = "0 0 9,12,15,18,21 * * *")
    public void tweetMeta1() throws JsonProcessingException, IOException {
        logger.info("Starting retweet schedule - 1");

        // 10
        service.retweetByHashtag(TwitterAccountEnum.BestAWS.name());
        service.retweetByHashtag(TwitterAccountEnum.BestClojure.name());
        service.retweetByHashtag(TwitterAccountEnum.BestJavaScript.name());
        service.retweetByHashtag(TwitterAccountEnum.BestJSON.name()); // not 100% sure
        service.retweetByHashtag(TwitterAccountEnum.BestPHP.name());
        service.retweetByHashtag(TwitterAccountEnum.BestRubyOnRails.name());
        service.retweetByHashtag(TwitterAccountEnum.BestScala.name());
        service.retweetByHashtag(TwitterAccountEnum.BestAlgorithms.name()); // not 100% sure
        service.retweetByHashtag(TwitterAccountEnum.HTMLdaily.name()); // not 100% sure
        service.retweetByHashtag(TwitterAccountEnum.PerlDaily.name()); // not 100% sure

        logger.info("Finished retweet schedule - 1");
    }

    @Scheduled(cron = "0 0 10,13,16,19,22 * * *")
    public void tweetMeta2() throws JsonProcessingException, IOException {
        logger.info("Starting retweet schedule - 2");

        // 10
        service.retweetByHashtag(TwitterAccountEnum.BestSQL.name());
        service.retweetByHashtag(TwitterAccountEnum.InTheAppleWorld.name());
        service.retweetByHashtag(TwitterAccountEnum.iOSdigest.name());
        service.retweetByHashtag(TwitterAccountEnum.jQueryDaily.name());
        service.retweetByHashtag(TwitterAccountEnum.PythonDaily.name());
        service.retweetByHashtag(TwitterAccountEnum.RegexDaily.name());
        service.retweetByHashtag(TwitterAccountEnum.ObjectiveCDaily.name());
        service.retweetByHashtag(TwitterAccountEnum.BestOfJava.name());
        service.retweetByHashtag(TwitterAccountEnum.FacebookDigest.name());
        service.retweetByHashtag(TwitterAccountEnum.MathDaily.name());

        logger.info("Finished retweet schedule - 2");
    }

    @Scheduled(cron = "0 0 11,14,17,20,23 * * *")
    public void tweetMeta3() throws JsonProcessingException, IOException {
        logger.info("Starting retweet schedule - 3");

        // 10
        service.retweetByHashtag(TwitterAccountEnum.BestOfCloud.name());
        service.retweetByHashtag(TwitterAccountEnum.BestOfSecurity.name());
        service.retweetByHashtag(TwitterAccountEnum.LandOfWordpress.name());
        service.retweetByHashtag(TwitterAccountEnum.LispDaily.name());
        service.retweetByHashtag(TwitterAccountEnum.BestNoSQL.name());
        service.retweetByHashtag(TwitterAccountEnum.GoogleDigest.name());
        service.retweetByHashtag(TwitterAccountEnum.MysqlDaily.name());
        service.retweetByHashtag(TwitterAccountEnum.BestOfLinux.name());
        service.retweetByHashtag(TwitterAccountEnum.ParsingDaily.name());
        service.retweetByHashtag(TwitterAccountEnum.BestXML.name());

        logger.info("Finished retweet schedule - 3");
    }

}
