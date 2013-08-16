package org.common.persistence.setup.upgrades.impl;

import java.util.Date;
import java.util.List;

import org.common.persistence.setup.AfterSetupEvent;
import org.common.persistence.setup.upgrades.IRemoveLocalDuplicateRetweetsUpgrader;
import org.common.service.live.LinkLiveService;
import org.common.util.LinkUtil;
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
import org.tweet.twitter.service.TweetService;
import org.tweet.twitter.service.live.TwitterReadLiveService;
import org.tweet.twitter.util.TweetUtil;

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
    private TwitterReadLiveService twitterReadLiveService;

    @Autowired
    private LinkLiveService linkLiveService;

    public RemoveLocalDuplicateRetweetsUpgrader() {
        super();
    }

    //

    @Override
    @Async
    public void onApplicationEvent(final AfterSetupEvent event) {
        if (env.getProperty("setup.upgrade.retweets.removeduplicates.do", Boolean.class)) {
            logger.info("Starting to execute the RemoveDuplicateRetweetsUpgrader Upgrader");
            removeLocalDuplicateRetweets();
            logger.info("Finished executing the RemoveDuplicateRetweetsUpgrader Upgrader");
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
                        logger.info("Done removing all duplicate retweets of twitterAccount= " + twitterAccount.name() + "; sleeping for 5 secs...");
                        Thread.sleep(1000 * 5 * 1); // 5 sec
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
        final List<Tweet> allTweetsOnAccount = twitterReadLiveService.listTweetsOfAccountMultiRequestRaw(twitterAccount, 3);
        removeDuplicateLocalRetweetsOnAccount(allTweetsOnAccount, twitterAccount);
        return !allTweetsOnAccount.isEmpty();
    }

    private final void removeDuplicateLocalRetweetsOnAccount(final List<Tweet> allTweetsForAccount, final String twitterAccount) {
        for (final Tweet tweet : allTweetsForAccount) {
            removeDuplicateLocalRetweets(tweet, twitterAccount);
        }
    }

    private final void removeDuplicateLocalRetweets(final Tweet tweet, final String twitterAccount) {
        try {
            removeDuplicateLocalRetweetsInternal(TweetUtil.getText(tweet), twitterAccount, tweet.getCreatedAt());
        } catch (final RuntimeException ex) {
            final String tweetUrl = "https://twitter.com/" + tweet.getFromUser() + "/status/" + tweet.getId();
            logger.error("Unable to remove duplicates for retweet: " + TweetUtil.getText(tweet) + " from \nlive tweet url= " + tweetUrl, ex);
        }
    }

    private final void removeDuplicateLocalRetweetsInternal(final String rawTweetText, final String twitterAccount, final Date when) {
        final boolean linkingToSe = linkLiveService.countLinksToAnyDomain(rawTweetText, LinkUtil.seDomains) > 0;
        if (linkingToSe) {
            logger.debug("Tweet is linking to Stack Exchange - not a retweet= {}", rawTweetText);
            return;
        }

        final String preProcessedText = tweetService.processPreValidity(rawTweetText);
        final String goodText = tweetService.postValidityProcessTweetTextWithUrl(preProcessedText, twitterAccount);

        final List<Retweet> foundRetweets = retweetDao.findAllByTextAndTwitterAccount(goodText, twitterAccount);
        if (foundRetweets.size() <= 1) {
            return;
        }
        final Retweet keepingOne = foundRetweets.remove(0);
        for (final Retweet duplicateRetweet : foundRetweets) {
            retweetDao.delete(duplicateRetweet);
        }

        logger.info("Removed on twitterAccount= {} - {} retweet= {}", twitterAccount, foundRetweets.size(), keepingOne.getText());
    }

}
