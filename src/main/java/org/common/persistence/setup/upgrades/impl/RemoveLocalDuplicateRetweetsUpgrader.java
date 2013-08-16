package org.common.persistence.setup.upgrades.impl;

import java.util.List;

import org.common.persistence.setup.AfterSetupEvent;
import org.common.persistence.setup.upgrades.IRemoveLocalDuplicateRetweetsUpgrader;
import org.common.service.live.LinkLiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.meta.service.TweetMetaLocalService;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TweetService;

/**
 */
@Component
@Profile(SpringProfileUtil.DEPLOYED)
class RemoveLocalDuplicateRetweetsUpgrader implements ApplicationListener<AfterSetupEvent>, IRemoveLocalDuplicateRetweetsUpgrader {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    @Autowired
    private IRetweetJpaDAO retweetDao;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private TweetMetaLocalService tweetMetaLocalService;

    @Autowired
    private LinkLiveService linkLiveService;

    public RemoveLocalDuplicateRetweetsUpgrader() {
        super();
    }

    //

    @Override
    @Async
    public void onApplicationEvent(final AfterSetupEvent event) {
        if (env.getProperty("setup.upgrade.retweets.local.removeduplicates.do", Boolean.class)) {
            logger.info("Starting to execute the RemoveLocalDuplicateRetweetsUpgrader Upgrader");
            removeLocalDuplicateRetweets();
            logger.info("Finished executing the RemoveLocalDuplicateRetweetsUpgrader Upgrader");
        }
    }

    // util

    @Override
    public void removeLocalDuplicateRetweets() {
        logger.info("Executing the RecreateMissingRetweetsUpgrader Upgrader");
        for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
            if (twitterAccount.isRt()) {
                try {
                    logger.info("Removing all duplicate retweets of twitterAccount= " + twitterAccount.name());
                    final boolean processedSomething = removeLocalDuplicateRetweetsOnAccount(twitterAccount.name());
                    if (processedSomething) {
                        logger.info("Done removing all duplicate retweets of twitterAccount= " + twitterAccount.name() + "; sleeping for 2 secs...");
                        Thread.sleep(1000 * 2 * 1); // 2 sec
                    }
                } catch (final RuntimeException ex) {
                    logger.error("Unable to remove duplicate retweets of twitterAccount= " + twitterAccount.name(), ex);
                } catch (final InterruptedException threadEx) {
                    logger.error("Unable to remove duplicate retweets of twitterAccount= " + twitterAccount.name(), threadEx);
                }
            }
        }
    }

    @Override
    public final boolean removeLocalDuplicateRetweetsOnAccount(final String twitterAccount) {
        final List<Retweet> allLocalRetweetsOfAccount = retweetDao.findAllByTwitterAccount(twitterAccount);
        removeDuplicateLocalRetweetsOnAccount(allLocalRetweetsOfAccount, twitterAccount);
        return true;
    }

    private final void removeDuplicateLocalRetweetsOnAccount(final List<Retweet> allLocalRetweetsForAccount, final String twitterAccount) {
        for (final Retweet retweet : allLocalRetweetsForAccount) {
            removeDuplicateLocalRetweets(retweet, twitterAccount);
        }
    }

    private final void removeDuplicateLocalRetweets(final Retweet retweet, final String twitterAccount) {
        try {
            removeDuplicateLocalRetweetsInternal(retweet, twitterAccount);
        } catch (final RuntimeException ex) {
            logger.error("Unable to remove duplicates for retweet: " + retweet, ex);
        }
    }

    private final void removeDuplicateLocalRetweetsInternal(final Retweet retweet, final String twitterAccount) {
        final String preProcessedText = tweetService.processPreValidity(retweet.getText());
        final String goodText = tweetService.postValidityProcessTweetTextWithUrl(preProcessedText, twitterAccount);

        final List<Retweet> foundRetweets = retweetDao.findAllByTextAndTwitterAccount(goodText, twitterAccount);
        if (foundRetweets.size() <= 1) {
            return;
        }
        final Retweet keepingOne = foundRetweets.remove(0);
        for (final Retweet duplicateRetweet : foundRetweets) {
            retweetDao.delete(duplicateRetweet);
        }

        logger.info("Removed on twitterAccount= {} - {} retweets= {}", twitterAccount, foundRetweets.size(), keepingOne.getText());
    }

}
