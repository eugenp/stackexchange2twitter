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
    // for accounts - not yet: InTheAppleWorld,BestAlgorithms,BestBash,BestEclipse,BestGit,BestJPA,BestMaven,BestOfRuby,BestOfJava,SpringAtSO,ServerFaultBest,JavaTopSO,RESTDaily

    @Scheduled(cron = "0 0 9,11,13,15,17,19,21 * * *")
    public void tweetMeta1() throws JsonProcessingException, IOException {
        logger.info("Starting to execute scheduled retweet operations - 1");

        service.retweetByHashtag(SimpleTwitterAccount.BestAWS.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestClojure.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestJavaScript.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestJSON.name()); // not 100% sure
        service.retweetByHashtag(SimpleTwitterAccount.BestNoSQL.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestOfCloud.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestOfSecurity.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestPHP.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestRubyOnRails.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestScala.name());
        service.retweetByHashtag(SimpleTwitterAccount.BestSQL.name());

        logger.info("Finished executing scheduled retweet operations - 1");
    }

    @Scheduled(cron = "0 0 10,12,14,16,18,20,22 * * *")
    public void tweetMeta2() throws JsonProcessingException, IOException {
        logger.info("Starting to execute scheduled retweet operations - 2");

        // for accounts - not yet: InTheAppleWorld,BestAlgorithms,BestBash,BestEclipse,BestGit,BestJPA,BestMaven,BestOfRuby,BestOfJava,SpringAtSO,ServerFaultBest,JavaTopSO,RESTDaily

        service.retweetByHashtag(SimpleTwitterAccount.InTheAppleWorld.name()); // not 100% sure
        service.retweetByHashtag(SimpleTwitterAccount.iOSdigest.name());
        service.retweetByHashtag(SimpleTwitterAccount.jQueryDaily.name());
        service.retweetByHashtag(SimpleTwitterAccount.GoogleDigest.name());
        service.retweetByHashtag(SimpleTwitterAccount.LandOfWordpress.name());
        service.retweetByHashtag(SimpleTwitterAccount.LispDaily.name());

        service.retweetByHashtag(SimpleTwitterAccount.PythonDaily.name()); // not 100% sure
        service.retweetByHashtag(SimpleTwitterAccount.RegexDaily.name());
        service.retweetByHashtag(SimpleTwitterAccount.ObjectiveCDaily.name());

        logger.info("Finished executing scheduled retweet operations - 2");
    }

}
