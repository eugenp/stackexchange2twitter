package org.common.persistence.setup.upgrades.live.nolonger;

import java.util.List;

import org.common.persistence.setup.AfterSetupEvent;
import org.common.service.live.LinkLiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.meta.service.TweetMetaLocalService;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.AdvancedTweetService;
import org.tweet.twitter.service.live.TwitterReadLiveService;
import org.tweet.twitter.util.TweetUtil;

@Component
@Profile(SpringProfileUtil.DEPLOYED)
class FillInDatesOfRetweetsUpgrader implements ApplicationListener<AfterSetupEvent>, IFillInDatesOfRetweetsUpgrader {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    @Autowired
    private IRetweetJpaDAO retweetDao;

    @Autowired
    private TweetMetaLocalService tweetMetaLocalService;

    @Autowired
    private AdvancedTweetService tweetService;

    @Autowired
    private TwitterReadLiveService twitterReadLiveService;

    @Autowired
    private LinkLiveService linkLiveService;

    public FillInDatesOfRetweetsUpgrader() {
        super();
    }

    //

    @Override
    @Async
    public void onApplicationEvent(final AfterSetupEvent event) {
        if (env.getProperty("setup.upgrade.retweetdates.do", Boolean.class)) {
            logger.info("Starting to execute the FillInDatesOfRetweetsUpgrader Upgrader");
            fillInDatesOfRetweetsOfAllAccounts();
            logger.info("Finished executing the FillInDatesOfRetweetsUpgrader Upgrader");
        }
    }

    // util

    @Override
    public void fillInDatesOfRetweetsOfAllAccounts() {
        logger.info("Executing the FillInDatesOfRetweetsUpgrader Upgrader");
        for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
            try {
                logger.info("Recreating dates for all missing retweets of twitterAccount= " + twitterAccount.name());
                final boolean processedSomething = fillInDatesOfRetweetsOfOneAccount(twitterAccount.name());
                if (processedSomething) {
                    logger.info("Done recreating dates on retweets of twitterAccount= " + twitterAccount.name() + "; sleeping for 10 secs...");
                    Thread.sleep(1000 * 10 * 1); // 10 sec
                }
            } catch (final RuntimeException ex) {
                logger.error("Unable to recreate dates on retweets of twitterAccount= " + twitterAccount.name(), ex);
            } catch (final InterruptedException threadEx) {
                logger.error("Unable to recreate dates on missing retweets of twitterAccount= " + twitterAccount.name(), threadEx);
            }
        }
    }

    @Override
    public boolean fillInDatesOfRetweetsOfOneAccount(final String twitterAccount) {
        int processedCount = 0;
        final List<Tweet> allTweetsOfAccount = twitterReadLiveService.listTweetsOfAccountMultiRequestRaw(twitterAccount, 5);
        for (final Tweet tweetRaw : allTweetsOfAccount) {
            final Tweet tweet = TweetUtil.getTweet(tweetRaw);
            final String fullTweetRaw = tweet.getText();
            final String fullTweetProcessedPreValidity = tweetService.processPreValidity(fullTweetRaw);
            final String fullTweetProcessed = tweetService.postValidityProcessTweetTextWithUrl(fullTweetProcessedPreValidity, twitterAccount);
            final List<Retweet> correspondingLocalRetweets = tweetMetaLocalService.findLocalCandidatesRelaxed(fullTweetProcessed, twitterAccount);
            for (final Retweet correspondingLocalRetweet : correspondingLocalRetweets) {
                if (correspondingLocalRetweet != null && correspondingLocalRetweet.getWhen() == null && tweet.getCreatedAt() != null) {
                    correspondingLocalRetweet.setWhen(tweet.getCreatedAt());
                    retweetDao.save(correspondingLocalRetweet);
                    processedCount++;
                    logger.info("Upgraded retweet (which had no date)= {}", tweet.getText());
                }
            }
        }

        return processedCount > 0;
    }

}
