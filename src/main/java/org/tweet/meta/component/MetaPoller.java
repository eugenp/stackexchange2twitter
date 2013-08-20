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

    @Scheduled(cron = "0 30 5 * * *")
    public void checkRetweetsMatch() throws JsonProcessingException, InterruptedException {
        logger.info("Starting Meta validity checks");

        for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
            if (twitterAccount.isRt()) {
                checkRetweetsMatchOnAccount(twitterAccount.name());
                logger.info("Done performing meta validity checks for twitterAccount= " + twitterAccount.name() + "; sleeping for 120 secs...");
                Thread.sleep(1000 * 60 * 2); // 120 sec
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
            final int differenceLocalAndLive = checkRetweetsMatchOnAccountInternal(twitterAccount);
            if (differenceLocalAndLive == 0) {
                return true;
            }
            return false;
        } catch (final RuntimeException ex) {
            logger.error("Unable to verify that live and local retweets match on twitterAccount= " + twitterAccount, ex);
            return false;
        }
    }

    /**
     * - the current implementation still works on the assumption that the account has less than 200 tweets
     */
    final int checkRetweetsMatchOnAccountInternal(final String twitterAccount) {
        final int absDifference = twitterAnalysisLiveService.calculateAbsDifferenceBetweenLocalAndLiveRetweetsOnAccount(twitterAccount);
        if (absDifference == 0) {
            logger.info("On twitterAccount= {}, NO difference between the live and the local retweets is: {}", twitterAccount, absDifference);
        }
        if (absDifference > 1) {
            logger.info("On twitterAccount= {}, SMALL difference between the live and the local retweets is: {}", twitterAccount, absDifference);
        } else if (absDifference > 4) {
            logger.warn("On twitterAccount= {}, MEDIUM difference between the live and the local retweets is: {}", twitterAccount, absDifference);
        } else if (absDifference > 8) {
            logger.error("On twitterAccount= {}, LARGE difference between the live and the local retweets is: {}", twitterAccount, absDifference);
        }
        return absDifference;
    }

}
