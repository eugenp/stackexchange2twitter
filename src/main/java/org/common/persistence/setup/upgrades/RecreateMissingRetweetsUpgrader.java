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
import org.tweet.twitter.service.TweetService;
import org.tweet.twitter.service.TwitterReadLiveService;

@Component
@Profile(SpringProfileUtil.DEPLOYED)
public class RecreateMissingRetweetsUpgrader implements ApplicationListener<AfterSetupEvent> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    @Autowired
    private IRetweetJpaDAO retweetDao;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private TwitterReadLiveService twitterLiveService;

    private Twitter twitterApi;

    public RecreateMissingRetweetsUpgrader() {
        super();
    }

    //

    @Override
    @Async
    public void onApplicationEvent(final AfterSetupEvent event) {
        if (env.getProperty("setup.upgrade.retweetmissing.do", Boolean.class)) {
            logger.info("Starting to execute the AddTextToRetweetsUpgrader Upgrader");
            addTextToRetweets();
            logger.info("Finished executing the AddTextToRetweetsUpgrader Upgrader");
        }
    }

    // util

    void addTextToRetweets() {
        logger.info("Executing the RecreateMissingRetweetsUpgrader Upgrader");
        twitterApi = twitterLiveService.readOnlyTwitterApi();
        int processed = 0;
        for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
            if (twitterAccount.isRt()) {
                try {
                    logger.info("Recreating all missing retweets of twitterAccount= " + twitterAccount.name());
                    final List<Retweet> allRetweetsForAccounts = retweetDao.findAllByTwitterAccount(twitterAccount.name());
                    addTextToRetweets(allRetweetsForAccounts);

                    if (!allRetweetsForAccounts.isEmpty()) {
                        processed += allRetweetsForAccounts.size();
                        logger.info("Done recreating all missing retweets of twitterAccount= " + twitterAccount.name() + "; processed= " + processed + "; sleeping for 9 secs...");
                        Thread.sleep(1000 * 30 * 3); // 90 sec
                    }
                } catch (final RuntimeException ex) {
                    logger.error("Unable to recreate missing retweets of twitterAccount= " + twitterAccount.name(), ex);
                } catch (final InterruptedException threadEx) {
                    logger.error("Unable to recreate missing retweets of twitterAccount= " + twitterAccount.name(), threadEx);
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
            // logger.trace("Retweet already has text - no need to upgrade; retweet= {}", retweet);
            // return;
            // TODO: temporarily disabling this so that texts are upgraded again
        }

        final Tweet status = twitterApi.timelineOperations().getStatus(retweet.getTweetId());
        final String textRaw = status.getText();
        final String preProcessedText = tweetService.preValidityProcess(textRaw);
        final String postProcessedText = tweetService.postValidityProcess(preProcessedText, retweet.getTwitterAccount());
        retweet.setText(postProcessedText);

        retweetDao.save(retweet);

        logger.info("Upgraded retweet with text= {}", retweet.getText());
    }

}
