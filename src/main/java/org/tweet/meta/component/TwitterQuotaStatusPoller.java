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

    @Scheduled(cron = "0 0 */2 * * *")
    public void checkTwitterApiQuotaOnAllAccounts() throws JsonProcessingException, InterruptedException {
        logger.info("Starting to check status of Twitter API Quota");

        for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
            if (twitterAccount.isRt()) {
                checkTwitterApiQuotaOnOneAccount(twitterAccount.name());
                logger.info("Done checking Quota status twitterAccount= " + twitterAccount.name() + "; sleeping for 10 secs...");
                Thread.sleep(1000 * 10 * 1); // 10 sec
            }
        }

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

        final Map<ResourceFamily, List<RateLimitStatus>> applicationRateLimitStatusMap = userOperations.getRateLimitStatus(ResourceFamily.APPLICATION);
        final List<RateLimitStatus> applicationRateLimitStatusList = applicationRateLimitStatusMap.values().iterator().next();
        final RateLimitStatus appRate = applicationRateLimitStatusList.get(0);
        logger.warn("Application of account= {} - remaining API hits= {} out of= {}; reset in {} mins", twitterAccount, appRate.getRemainingHits(), appRate.getQuarterOfHourLimit(), (appRate.getResetTimeInSeconds() / 216000000));

        final Map<ResourceFamily, List<RateLimitStatus>> accountRateLimitStatusMap = userOperations.getRateLimitStatus(ResourceFamily.ACCOUNT);
        final List<RateLimitStatus> accountRateLimitStatusList = accountRateLimitStatusMap.values().iterator().next();
        final RateLimitStatus accountRate = accountRateLimitStatusList.get(0);
        logger.warn("Account= {} - remaining API hits= {} out of= {}; reset in {} mins", twitterAccount, accountRate.getRemainingHits(), accountRate.getQuarterOfHourLimit(), appRate.getResetTimeInSeconds() / 216000000);
    }

}
