package org.common.persistence.setup.upgrades;

import java.util.List;

import org.common.persistence.setup.AfterSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TwitterLiveService;

@Component
@Profile(SpringProfileUtil.DEPLOYED)
public final class AddTextToRetweetsUpgrader implements ApplicationListener<AfterSetupEvent> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    @Autowired
    private IRetweetJpaDAO retweetDao;

    @Autowired
    private TwitterLiveService twitterLiveService;

    private Twitter twitterApi;

    public AddTextToRetweetsUpgrader() {
        super();
    }

    //

    @Override
    public final void onApplicationEvent(final AfterSetupEvent event) {
        if (env.getProperty("setup.do", Boolean.class)) {
            logger.info("Starting to execute the AddTextToRetweets Upgrader");
            addTextToRetweets();
            logger.info("Finished executing the AddTextToRetweets Upgrader");
        }
    }

    // util

    private final void addTextToRetweets() {
        twitterApi = twitterLiveService.readOnlyTwitterApi();

        for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
            if (twitterAccount.isRt()) {
                try {
                    final List<Retweet> allRetweetsForAccounts = retweetDao.findAllByTwitterAccount(twitterAccount.name());
                    addTextToRetweets(allRetweetsForAccounts);
                } catch (final RuntimeException ex) {
                    logger.error("Unable to add text to retweets of twitterAccount= " + twitterAccount.name(), ex);
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
        final Tweet status = twitterApi.timelineOperations().getStatus(retweet.getTweetId());
        retweet.setText(status.getText());
        retweetDao.save(retweet);
    }

}
