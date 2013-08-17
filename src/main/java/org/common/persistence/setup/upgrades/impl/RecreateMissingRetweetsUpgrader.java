package org.common.persistence.setup.upgrades.impl;

import java.util.Date;
import java.util.List;

import org.common.persistence.setup.AfterSetupEvent;
import org.common.persistence.setup.upgrades.live.IRecreateMissingRetweetsUpgrader;
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
import org.stackexchange.util.IDUtil;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.meta.service.TweetMetaLocalService;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TweetService;
import org.tweet.twitter.service.live.TwitterReadLiveService;
import org.tweet.twitter.util.TweetUtil;

@Component
@Profile(SpringProfileUtil.DEPLOYED)
public class RecreateMissingRetweetsUpgrader implements ApplicationListener<AfterSetupEvent>, IRecreateMissingRetweetsUpgrader {
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

    public RecreateMissingRetweetsUpgrader() {
        super();
    }

    //

    @Override
    // @Async
    public void onApplicationEvent(final AfterSetupEvent event) {
        if (env.getProperty("setup.upgrade.retweetmissing.do", Boolean.class)) {
            logger.info("Starting to execute the AddTextToRetweetsUpgrader Upgrader");
            recreateLocalRetweetsFromLiveTweets();
            logger.info("Finished executing the AddTextToRetweetsUpgrader Upgrader");
        }
    }

    // util

    @Override
    public void recreateLocalRetweetsFromLiveTweets() {
        logger.info("Executing the RecreateMissingRetweetsUpgrader Upgrader");
        for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
            if (twitterAccount.isRt()) {
                try {
                    logger.info("Recreating all missing retweets of twitterAccount= " + twitterAccount.name());
                    processAllLiveTweetsOnAccount(twitterAccount.name());
                } catch (final RuntimeException ex) {
                    logger.error("Unable to recreate missing retweets of twitterAccount= " + twitterAccount.name(), ex);
                }
            }
        }
    }

    @Override
    @Async
    public void processAllLiveTweetsOnAccount(final String twitterAccount) {
        final List<Tweet> allTweetsOnAccount = twitterReadLiveService.listTweetsOfAccountMultiRequestRaw(twitterAccount, 3);
        processAllLiveTweets(allTweetsOnAccount, twitterAccount);
    }

    private final void processAllLiveTweets(final List<Tweet> allTweetsForAccount, final String twitterAccount) {
        for (final Tweet tweet : allTweetsForAccount) {
            processLiveTweet(tweet, twitterAccount);
        }
    }

    private final void processLiveTweet(final Tweet tweet, final String twitterAccount) {
        try {
            processLiveTweetInternal(TweetUtil.getText(tweet), twitterAccount, tweet.getCreatedAt());
        } catch (final RuntimeException ex) {
            final String tweetUrl = "https://twitter.com/" + tweet.getFromUser() + "/status/" + tweet.getId();
            logger.error("Unable to recreate retweet: " + TweetUtil.getText(tweet) + " from \nlive tweet url= " + tweetUrl, ex);
        }
    }

    private final void processLiveTweetInternal(final String rawTweetText, final String twitterAccount, final Date when) {
        final boolean linkingToSe = linkLiveService.countLinksToAnyDomain(rawTweetText, LinkUtil.seDomains) > 0;
        if (linkingToSe) {
            logger.debug("Tweet is linking to Stack Exchange - not a retweet= {}", rawTweetText);
            return;
        }

        final String preProcessedText = tweetService.processPreValidity(rawTweetText);
        final String goodText = tweetService.postValidityProcessTweetTextWithUrl(preProcessedText, twitterAccount);

        final Retweet foundRetweet = retweetDao.findOneByTextAndTwitterAccount(goodText, twitterAccount);
        if (foundRetweet != null) {
            logger.debug("Found local retweet: " + foundRetweet);
            return;
        }

        final boolean foundRetweetsAdvancedSearch = !tweetMetaLocalService.findLocalCandidatesStrict(goodText, twitterAccount).isEmpty();
        if (foundRetweetsAdvancedSearch) {
            logger.debug("Found local retweet (advanced)s: " + foundRetweetsAdvancedSearch);
            return;
        }

        final Retweet newRetweet = new Retweet(IDUtil.randomPositiveLong(), twitterAccount, goodText, when);
        retweetDao.save(newRetweet);

        logger.info("Created on twitterAccount= {}, new retweet= {}", twitterAccount, newRetweet);
    }

}
