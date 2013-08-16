package org.common.persistence.setup.upgrades.impl;

import java.util.List;

import org.common.persistence.setup.AfterSetupEvent;
import org.common.persistence.setup.upgrades.IAddTextToRetweetsUpgrader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TweetService;
import org.tweet.twitter.service.live.TwitterReadLiveService;

@Component
@Profile(SpringProfileUtil.DEPLOYED)
class AddTextToRetweetsUpgrader implements ApplicationListener<AfterSetupEvent>, IAddTextToRetweetsUpgrader {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    @Autowired
    private IRetweetJpaDAO retweetDao;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private TwitterReadLiveService twitterReadLiveService;

    private Twitter twitterApi;

    public AddTextToRetweetsUpgrader() {
        super();
    }

    // API

    @Override
    @Async
    public void onApplicationEvent(final AfterSetupEvent event) {
        if (env.getProperty("setup.upgrade.retweettext.do", Boolean.class)) {
            logger.info("Starting to execute the AddTextToRetweetsUpgrader Upgrader");
            addTextOfRetweets();
            logger.info("Finished executing the AddTextToRetweetsUpgrader Upgrader");
        }
    }

    @Override
    public void addTextOfRetweets() {
        logger.info("Executing the AdsdTextToRetweets Upgrader");
        twitterApi = twitterReadLiveService.readOnlyTwitterApi();
        for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
            if (twitterAccount.isRt()) {
                try {
                    logger.info("Upgrading (adding text) to retweets of twitterAccount= " + twitterAccount.name());
                    final boolean success = addTextOfRetweetsOnAccount(twitterAccount.name());
                    if (!success) {
                        logger.info("Done upgrading (adding text) to retweets of twitterAccount= " + twitterAccount.name() + "; sleeping for 90 secs...");
                        Thread.sleep(1000 * 30 * 3); // 90 sec
                    }
                } catch (final RuntimeException ex) {
                    logger.error("Unable to add text to retweets of twitterAccount= " + twitterAccount.name(), ex);
                } catch (final InterruptedException threadEx) {
                    logger.error("Unable to add text to retweets of twitterAccount= " + twitterAccount.name(), threadEx);
                }
            }
        }
    }

    @Override
    public boolean addTextOfRetweetsOnAccount(final String twitterAccount) {
        final List<Retweet> allRetweetsForAccount = retweetDao.findAllByTwitterAccount(twitterAccount);
        addTextOfRetweets(allRetweetsForAccount);
        return !allRetweetsForAccount.isEmpty();
    }

    // util

    private final void addTextOfRetweets(final List<Retweet> allRetweetsForAccount) {
        for (final Retweet retweet : allRetweetsForAccount) {
            addTextOfRetweet(retweet);
        }
    }

    private final void addTextOfRetweet(final Retweet retweet) {
        try {
            addTextOfRetweetInternal(retweet);
        } catch (final RuntimeException ex) {
            logger.error("Unable to add text to retweet: " + retweet);
        }
    }

    private final void addTextOfRetweetInternal(final Retweet retweet) {
        if (retweet.getText() != null) {
            logger.trace("Retweet already has text - no need to upgrade; retweet= {}", retweet);
            return;
        }

        final Tweet tweet = twitterApi.timelineOperations().getStatus(retweet.getTweetId());
        final String textRaw = tweet.getText();
        final String preProcessedText = tweetService.processPreValidity(textRaw);
        final String postProcessedText = tweetService.postValidityProcessTweetTextWithUrl(preProcessedText, retweet.getTwitterAccount());
        retweet.setText(postProcessedText);

        retweetDao.save(retweet);

        logger.info("Upgraded retweet with text= {}", retweet.getText());
    }

}
