package org.tweet.meta.component;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.social.twitter.api.RateLimitStatus;
import org.springframework.social.twitter.api.ResourceFamily;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.UserOperations;
import org.springframework.stereotype.Component;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.live.TwitterReadLiveService;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Profile(SpringProfileUtil.DEPLOYED)
public class TwitterQuotaStatusPoller {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterReadLiveService twitterReadLiveService;

    public TwitterQuotaStatusPoller() {
        super();
    }

    // API

    @Scheduled(cron = "0 0 9 * * *")
    public void checkTwitterApiQuotaOnAllAccounts() throws JsonProcessingException, InterruptedException {
        logger.info("Starting to check status of Twitter API Quota");

        // for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
        // if (twitterAccount.isRt()) {
        // checkTwitterApiQuotaInternal(twitterAccount.name());
        // logger.info("Done recreating all missing retweets of twitterAccount= " + twitterAccount.name() + "; sleeping for 60 secs...");
        // Thread.sleep(1000 * 60 * 1); // 60 sec
        // }
        // }

        checkTwitterApiQuotaOnOneAccount(TwitterAccountEnum.PerlDaily.name());
        checkTwitterApiQuotaOnOneAccount(TwitterAccountEnum.BestScala.name());
        checkTwitterApiQuotaOnOneAccount(TwitterAccountEnum.JavaTopSO.name());

        logger.info("Finished checking status of Twitter API Quota");
    }

    // util

    final boolean checkTwitterApiQuotaOnOneAccount(final String twitterAccount) {
        try {
            checkTwitterApiQuotaOnOneAccountInternal(twitterAccount);
            return true;
        } catch (final RuntimeException ex) {
            logger.error("Unable to check status of Twitter API Quota", ex);
            return false;
        }
    }

    final void checkTwitterApiQuotaOnOneAccountInternal(final String twitterAccount) {
        final Twitter readOnlyTwitterApi = twitterReadLiveService.readOnlyTwitterApi(twitterAccount);
        final UserOperations userOperations = readOnlyTwitterApi.userOperations();
        final Map<ResourceFamily, List<RateLimitStatus>> applicationRateLimitStatus = userOperations.getRateLimitStatus(ResourceFamily.APPLICATION);
        final Map<ResourceFamily, List<RateLimitStatus>> accountRateLimitStatus = userOperations.getRateLimitStatus(ResourceFamily.ACCOUNT);
        logger.warn("Application Rate Limit Status is= {}", applicationRateLimitStatus);
        logger.warn("Account Rate Limit Status is= {}", accountRateLimitStatus);
    }

}
