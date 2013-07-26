package org.common.persistence.setup.upgrades;

import java.util.Date;
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
import org.stackexchange.util.IDUtil;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TweetService;
import org.tweet.twitter.service.live.TwitterReadLiveService;

@Component
@Profile(SpringProfileUtil.DEPLOYED)
class RecreateMissingRetweetsUpgrader implements ApplicationListener<AfterSetupEvent>, IRecreateMissingRetweetsUpgrader {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    @Autowired
    private IRetweetJpaDAO retweetDao;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private TwitterReadLiveService twitterLiveService;

    @Autowired
    private LinkLiveService linkLiveService;

    public RecreateMissingRetweetsUpgrader() {
        super();
    }

    //

    @Override
    @Async
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
                    final boolean processedSomething = processAllLiveTweetsOnAccount(twitterAccount.name());
                    if (processedSomething) {
                        logger.info("Done recreating all missing retweets of twitterAccount= " + twitterAccount.name() + "; sleeping for 120 secs...");
                        Thread.sleep(1000 * 60 * 3); // 120 sec
                    }
                } catch (final RuntimeException ex) {
                    logger.error("Unable to recreate missing retweets of twitterAccount= " + twitterAccount.name(), ex);
                } catch (final InterruptedException threadEx) {
                    logger.error("Unable to recreate missing retweets of twitterAccount= " + twitterAccount.name(), threadEx);
                }
            }
        }
    }

    @Override
    public final boolean processAllLiveTweetsOnAccount(final String twitterAccount) {
        final List<Tweet> allTweetsOnAccount = twitterLiveService.listTweetsOfInternalAccountRaw(twitterAccount, 200);
        processAllLiveTweets(allTweetsOnAccount, twitterAccount);
        return !allTweetsOnAccount.isEmpty();
    }

    private final void processAllLiveTweets(final List<Tweet> allTweetsForAccount, final String twitterAccount) {
        for (final Tweet tweet : allTweetsForAccount) {
            processLiveTweet(tweet, twitterAccount);
        }
    }

    private final void processLiveTweet(final Tweet tweet, final String twitterAccount) {
        try {
            processLiveTweetInternal(tweet.getText(), twitterAccount, tweet.getCreatedAt());
        } catch (final RuntimeException ex) {
            logger.error("Unable to add text to retweet: " + tweet);
        }
    }

    private final void processLiveTweetInternal(final String tweetText, final String twitterAccount, final Date when) {
        final Retweet foundRetweet = retweetDao.findOneByTextAndTwitterAccount(tweetText, twitterAccount);
        if (foundRetweet != null) {
            logger.debug("Found local retweet: " + foundRetweet);
            return;
        }

        final boolean linkingToSo = linkLiveService.containsLinkToDomain(tweetText, "http://stackoverflow.com");
        if (linkingToSo) {
            logger.debug("Tweet is linking to SO - not a retweet= {}", tweetText);
            return;

        }

        final Retweet newRetweet = new Retweet(IDUtil.randomPositiveLong(), twitterAccount, tweetText, when);
        retweetDao.save(newRetweet);

        // final Tweet status = twitterApi.timelineOperations().getStatus(retweet.getTweetId());
        // final String textRaw = status.getText();
        // final String preProcessedText = tweetService.preValidityProcess(textRaw);
        // final String postProcessedText = tweetService.postValidityProcess(preProcessedText, retweet.getTwitterAccount());
        // retweet.setText(postProcessedText);
        //
        // retweetDao.save(retweet);

        logger.info("Created on twitterAccount= {}, new retweet= {}", twitterAccount, newRetweet);
    }

}
