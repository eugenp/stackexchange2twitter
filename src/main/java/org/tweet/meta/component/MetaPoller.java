package org.tweet.meta.component;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.live.TwitterAnalysisLiveService;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Profile(SpringProfileUtil.DEPLOYED)
public class MetaPoller {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterAnalysisLiveService twitterAnalysisLiveService;

    public MetaPoller() {
        super();
    }

    // API

    @Scheduled(cron = "0 0 9 * * *")
    public void checkRetweetsMatch() throws JsonProcessingException, IOException {
        logger.info("Starting Meta validity checks");

        for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
            if (twitterAccount.isRt()) {
                checkRetweetsMatchOnAccount(twitterAccount.name());
            }
        }

        logger.info("Finished Meta validity checks");
    }

    // util

    /**
     * - the current implementation still works on the assumption that the account has less than 200 tweets
     */
    final void checkRetweetsMatchOnAccount(final String twitterAccount) {
        final long absDifference = twitterAnalysisLiveService.calculateAbsDifferenceBetweenLocalAndLiveRetweetsOnAccount(twitterAccount);

        System.out.println("On twitterAccount= " + twitterAccount + ", the difference between the live and the local retweets is: " + absDifference);
        logger.info("On twitterAccount= {}, the difference between the live and the local retweets is: {}", twitterAccount, absDifference);
    }

}
