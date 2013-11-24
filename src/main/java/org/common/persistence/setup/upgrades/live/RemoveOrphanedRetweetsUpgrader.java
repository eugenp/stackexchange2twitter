package org.common.persistence.setup.upgrades.live;

import java.util.Iterator;
import java.util.List;

import org.common.persistence.setup.AfterSetupEvent;
import org.common.service.live.LinkLiveService;
import org.common.util.LinkUtil.Technical;
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

@Component
@Profile(SpringProfileUtil.DEPLOYED)
public class RemoveOrphanedRetweetsUpgrader implements ApplicationListener<AfterSetupEvent>, IRemoveOrphanedRetweetsUpgrader {
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

    public RemoveOrphanedRetweetsUpgrader() {
        super();
    }

    //

    @Override
    @Async
    public void onApplicationEvent(final AfterSetupEvent event) {
        if (env.getProperty("setup.upgrade.remove.orphanedretweets.do", Boolean.class)) {
            logger.info("Starting to execute the AddTextToRetweetsUpgrader Upgrader");
            removeOrphanedRetweets();
            logger.info("Finished executing the AddTextToRetweetsUpgrader Upgrader");
        }
    }

    // util

    @Override
    public void removeOrphanedRetweets() {
        logger.info("Executing the RecreateMissingRetweetsUpgrader Upgrader");
        for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
            if (twitterAccount.isRt()) {
                try {
                    logger.info("Recreating all missing retweets of twitterAccount= " + twitterAccount.name());
                    removeOrphanedRetweetsOnAccount(twitterAccount.name());
                } catch (final RuntimeException ex) {
                    logger.error("Unable to recreate missing retweets of twitterAccount= " + twitterAccount.name(), ex);
                }
            }
        }
    }

    @Override
    public void removeOrphanedRetweetsOnAccount(final String twitterAccount) {
        final List<Tweet> allLiveTweetsOnAccount = twitterReadLiveService.listTweetsOfAccountMultiRequestRaw(twitterAccount, 3);
        final List<Retweet> allLocalRetweetsOnAccount = retweetDao.findAllByTwitterAccount(twitterAccount);
        removeOrphanedRetweetsOnAccount(allLiveTweetsOnAccount, allLocalRetweetsOnAccount, twitterAccount);
    }

    private final void removeOrphanedRetweetsOnAccount(final List<Tweet> allLiveTweetsOnAccount, final List<Retweet> allLocalReweetsOnAccount, final String twitterAccount) {
        for (final Tweet tweet : allLiveTweetsOnAccount) {
            final boolean linkingToSe = linkLiveService.countLinksToAnyDomain(tweet, Technical.seDomains) > 0;
            if (linkingToSe) {
                continue;
            }

            final String rawText = TweetUtil.getText(tweet);
            final String preProcessedText = tweetService.processPreValidity(rawText);
            final String goodText = tweetService.postValidityProcessTweetTextWithUrl(preProcessedText, twitterAccount);

            final List<Retweet> localCorrespondingRetweets = tweetMetaLocalService.findLocalCandidatesRelaxed(goodText, twitterAccount);
            allLocalReweetsOnAccount.removeAll(localCorrespondingRetweets);
        }

        final Iterator<Retweet> iteratorOfLocalRetweets = allLocalReweetsOnAccount.iterator();
        Retweet retweetLeft;
        for (; iteratorOfLocalRetweets.hasNext();) {
            retweetLeft = iteratorOfLocalRetweets.next();
            final boolean linkingToSe = linkLiveService.hasLinksToAnyDomain(retweetLeft.getText(), Technical.seDomains);
            if (linkingToSe) {
                iteratorOfLocalRetweets.remove();
            }
        }

        logger.warn("Deleting Orphan Retweets= {}", allLocalReweetsOnAccount.size());
        for (final Retweet retweetOrphan : allLocalReweetsOnAccount) {
            retweetDao.delete(retweetOrphan);
        }
    }

}
