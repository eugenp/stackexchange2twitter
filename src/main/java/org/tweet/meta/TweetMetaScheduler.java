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
@Profile(SpringProfileUtil.WRITE_PRODUCTION)
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

    @Scheduled(cron = "0 3 9,12,15,18,21 * * *")
    public void tweetMeta1() throws JsonProcessingException, IOException {
        logger.info("Starting retweet schedule - 1");

        // 10
        service.retweetAnyByHashtag(TwitterAccountEnum.BestAWS.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestClojure.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestJavaScript.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestJSON.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestPHP.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestRubyOnRails.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestScala.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestAlgorithms.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.HTMLdaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.PerlDaily.name());

        logger.info("Finished retweet schedule - 1");
    }

    @Scheduled(cron = "0 3 10,13,16,19,22 * * *")
    public void tweetMeta2() throws JsonProcessingException, IOException {
        logger.info("Starting retweet schedule - 2");

        // 12
        service.retweetAnyByHashtag(TwitterAccountEnum.BestSQL.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.InTheAppleWorld.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.iOSdigest.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.jQueryDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.PythonDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.RegexDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.ObjectiveCDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfJava.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.FacebookDigest.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.MathDaily.name());
        // service.retweetAnyByHashtag(TwitterAccountEnum.BestOfCocoa.name()); // no

        /*dup*/service.retweetAnyByHashtag(TwitterAccountEnum.BestRubyOnRails.name());

        logger.info("Finished retweet schedule - 2");
    }

    @Scheduled(cron = "0 3 11,14,17,20,23 * * *")
    public void tweetMeta3() throws JsonProcessingException, IOException {
        logger.info("Starting retweet schedule - 3");

        // 11
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfCloud.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfSecurity.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.LandOfWordpress.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.LispDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestNoSQL.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.GoogleDigest.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.MysqlDaily.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfLinux.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestXML.name());
        service.retweetAnyByHashtag(TwitterAccountEnum.BestOfCss.name());
        // disabled for now - no results // service.retweetAnyByHashtag(TwitterAccountEnum.ParsingDaily.name());

        /*dup*/service.retweetAnyByHashtag(TwitterAccountEnum.PerlDaily.name());

        logger.info("Finished retweet schedule - 3");
    }

}
