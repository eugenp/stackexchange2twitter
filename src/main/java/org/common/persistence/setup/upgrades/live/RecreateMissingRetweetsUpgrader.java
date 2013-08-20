package org.common.persistence.setup.upgrades.live;

import java.util.Date;
import java.util.List;

import org.common.persistence.setup.AfterSetupEvent;
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
                    recreateLocalRetweetsFromLiveTweetsOnAccount(twitterAccount.name());
                } catch (final RuntimeException ex) {
                    logger.error("Unable to recreate missing retweets of twitterAccount= " + twitterAccount.name(), ex);
                }
            }
        }
    }

    @Override
    @Async
    public void recreateLocalRetweetsFromLiveTweetsOnAccount(final String twitterAccount) {
        final List<Tweet> allTweetsOnAccount = twitterReadLiveService.listTweetsOfAccountMultiRequestRaw(twitterAccount, 5);
        processAllLiveTweets(allTweetsOnAccount, twitterAccount);
    }

    private final boolean processAllLiveTweets(final List<Tweet> allTweetsForAccount, final String twitterAccount) {
        int count = 0;
        for (final Tweet tweet : allTweetsForAccount) {
            if (processLiveTweet(tweet, twitterAccount)) {
                count++;
            }
        }

        logger.info("Recreated {} retweets on account= {}", count, twitterAccount);
        return count > 0;
    }

    private final boolean processLiveTweet(final Tweet tweet, final String twitterAccount) {
        try {
            return processLiveTweetInternal(tweet, twitterAccount, tweet.getCreatedAt());
        } catch (final RuntimeException ex) {
            final String tweetUrl = "https://twitter.com/" + tweet.getFromUser() + "/status/" + tweet.getId();
            logger.error("Unable to recreate retweet: " + TweetUtil.getText(tweet) + " from \nlive tweet url= " + tweetUrl, ex);
            return false;
        }
    }

    private final boolean processLiveTweetInternal(final Tweet rawTweet, final String twitterAccount, final Date when) {
        final String rawTweetText = TweetUtil.getText(rawTweet);
        final boolean linkingToSe = linkLiveService.countLinksToAnyDomain(rawTweet, LinkUtil.seDomains) > 0;
        if (linkingToSe) {
            logger.debug("Tweet is linking to Stack Exchange - not a retweet= {}", rawTweetText);
            return false;
        }

        final String preProcessedText = tweetService.processPreValidity(rawTweetText);
        final String goodText = tweetService.postValidityProcessTweetTextWithUrl(preProcessedText, twitterAccount);

        final Retweet foundRetweet = retweetDao.findOneByTextAndTwitterAccount(goodText, twitterAccount);
        if (foundRetweet != null) {
            logger.debug("Found local retweet: " + foundRetweet);
            return false;
        }

        final List<Retweet> localCandidatesByStrictAdvancedSearch = tweetMetaLocalService.findLocalCandidatesStrict(goodText, twitterAccount);
        final boolean foundRetweetsAdvancedSearch = !localCandidatesByStrictAdvancedSearch.isEmpty();
        if (foundRetweetsAdvancedSearch) {
            logger.debug("Found local retweet (advanced)s: " + foundRetweetsAdvancedSearch);
            return false;
        }

        final Retweet newRetweet = new Retweet(IDUtil.randomPositiveLong(), twitterAccount, goodText, when);
        retweetDao.save(newRetweet);

        logger.info("Created on twitterAccount= {}, new retweet= {}", twitterAccount, newRetweet);
        return true;
    }

}
