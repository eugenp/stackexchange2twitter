package org.tweet.meta.component;

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
@Profile(SpringProfileUtil.DEPLOYED_POLLER)
public class MetaPoller {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterAnalysisLiveService twitterAnalysisLiveService;

    public MetaPoller() {
        super();
    }

    // API

    @Scheduled(cron = "0 0 9 * * *")
    public void checkRetweetsMatch() throws JsonProcessingException, InterruptedException {
        logger.info("Starting Meta validity checks");

        for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
            if (twitterAccount.isRt()) {
                checkRetweetsMatchOnAccount(twitterAccount.name());
                logger.info("Done performing meta validity checks for twitterAccount= " + twitterAccount.name() + "; sleeping for 60 secs...");
                Thread.sleep(1000 * 60 * 1); // 60 sec
            }
        }

        logger.info("Finished Meta validity checks");
    }

    // util

    /**
     * - the current implementation still works on the assumption that the account has less than 200 tweets
     */
    final boolean checkRetweetsMatchOnAccount(final String twitterAccount) {
        try {
            checkRetweetsMatchOnAccountInternal(twitterAccount);
            return true;
        } catch (final RuntimeException ex) {
            logger.error("Unable to verify that live and local retweets match on twitterAccount= " + twitterAccount, ex);
            return false;
        }
    }

    /**
     * - the current implementation still works on the assumption that the account has less than 200 tweets
     */
    final void checkRetweetsMatchOnAccountInternal(final String twitterAccount) {
        final long absDifference = twitterAnalysisLiveService.calculateAbsDifferenceBetweenLocalAndLiveRetweetsOnAccount(twitterAccount);
        if (absDifference > 2) {
            System.out.println("On twitterAccount= " + twitterAccount + ", the difference between the live and the local retweets is: " + absDifference);
            logger.info("On twitterAccount= {}, the difference between the live and the local retweets is: {}", twitterAccount, absDifference);
        }
    }

}
