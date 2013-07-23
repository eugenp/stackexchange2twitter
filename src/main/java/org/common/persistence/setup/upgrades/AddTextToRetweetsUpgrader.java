package org.common.persistence.setup.upgrades;

import java.util.List;

import org.common.persistence.setup.AfterSetupEvent;
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
import org.tweet.twitter.service.TwitterReadLiveService;

@Component
@Profile(SpringProfileUtil.DEPLOYED)
public class AddTextToRetweetsUpgrader implements ApplicationListener<AfterSetupEvent> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    @Autowired
    private IRetweetJpaDAO retweetDao;

    @Autowired
    private TwitterReadLiveService twitterLiveService;

    private Twitter twitterApi;

    public AddTextToRetweetsUpgrader() {
        super();
    }

    //

    @Override
    @Async
    public void onApplicationEvent(final AfterSetupEvent event) {
        if (env.getProperty("setup.upgrade.do", Boolean.class)) {
            logger.info("Starting to execute the AddTextToRetweets Upgrader on threadId= ", Thread.currentThread().getId());
            addTextToRetweets();
            logger.info("Finished executing the AddTextToRetweets Upgrader on threadId= ", Thread.currentThread().getId());
        }
    }

    // util

    void addTextToRetweets() {
        logger.info("Executing the AddTextToRetweets Upgrader on threadId= ", Thread.currentThread().getId());
        twitterApi = twitterLiveService.readOnlyTwitterApi();

        for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
            if (twitterAccount.isRt()) {
                try {
                    logger.info("Upgrading (adding text) to retweets of twitterAccount= " + twitterAccount.name());
                    final List<Retweet> allRetweetsForAccounts = retweetDao.findAllByTwitterAccount(twitterAccount.name());
                    addTextToRetweets(allRetweetsForAccounts);

                    logger.info("Done upgrading (adding text) to retweets of twitterAccount= " + twitterAccount.name() + "; sleeping for 2 mins...");
                    Thread.sleep(1000 * 60 * 2);
                } catch (final RuntimeException ex) {
                    logger.error("Unable to add text to retweets of twitterAccount= " + twitterAccount.name(), ex);
                } catch (final InterruptedException threadEx) {
                    logger.error("Unable to add text to retweets of twitterAccount= " + twitterAccount.name(), threadEx);
                }
            }
        }
    }

    private final void addTextToRetweets(final List<Retweet> allRetweetsForAccounts) {
        for (final Retweet retweet : allRetweetsForAccounts) {
            addTextToRetweet(retweet);
        }
    }

    private final void addTextToRetweet(final Retweet retweet) {
        try {
            addTextToRetweetInternal(retweet);
        } catch (final RuntimeException ex) {
            logger.error("Unable to add text to retweet: " + retweet);
        }
    }

    private final void addTextToRetweetInternal(final Retweet retweet) {
        if (retweet.getText() != null) {
            logger.trace("Retweet already has text - no need to upgrade; retweet= {}", retweet);
            return;
        }

        final Tweet status = twitterApi.timelineOperations().getStatus(retweet.getTweetId());
        retweet.setText(status.getText());
        retweetDao.save(retweet);

        logger.info("Upgraded retweet with text= {}", retweet.getText());
    }

}
