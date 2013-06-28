package org.tweet.meta;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.stackexchange.util.SimpleTwitterAccount;
import org.stackexchange.util.Tag;
import org.stackexchange.util.TagAdvanced;
import org.tweet.meta.service.TweetMetaService;
import org.tweet.spring.util.SpringProfileUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * - note: scheduler for tweets on accounts that are StackExchange specific
 */
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

    @Scheduled(cron = "0 0 9,11,13,15,17,19,21 * * *")
    public void tweetMetaExperimental() throws JsonProcessingException, IOException {
        logger.info("Experimental - Starting to execute scheduled retweet operations");

        service.retweetByHashtag(SimpleTwitterAccount.jQueryDaily.name(), Tag.jquery.name());

        service.retweetByHashtag(SimpleTwitterAccount.BestClojure.name(), Tag.clojure.name());

        service.retweetByHashtag(SimpleTwitterAccount.BestScala.name(), Tag.scala.name());

        service.retweetByHashtag(SimpleTwitterAccount.LispDaily.name(), Tag.lisp.name());

        service.retweetByHashtag(SimpleTwitterAccount.BestSQL.name(), Tag.sql.name());

        service.retweetByHashtag(SimpleTwitterAccount.BestNoSQL.name(), Tag.nosql.name());

        service.retweetByHashtag(SimpleTwitterAccount.BestJavaScript.name(), Tag.javascript.name());

        service.retweetByHashtag(SimpleTwitterAccount.iOSdigest.name(), Tag.ios.name());

        service.retweetByHashtag(SimpleTwitterAccount.ObjectiveCDaily.name(), TagAdvanced.objectivec);

        service.retweetByHashtag(SimpleTwitterAccount.BestAWS.name(), Tag.aws.name());

        // python - not 100% sure that hashtag will only return relevant tweets - look into this further
        // git - not 100% sure that hashtag will only return relevant tweets - look into this further

        logger.info("Experimental - Finished executing scheduled tweet operations");
    }

}
