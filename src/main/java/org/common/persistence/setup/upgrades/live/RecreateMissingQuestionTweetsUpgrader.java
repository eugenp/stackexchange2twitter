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
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;
import org.stackexchange.api.constants.StackSite;
import org.stackexchange.persistence.dao.IQuestionTweetJpaDAO;
import org.stackexchange.persistence.model.QuestionTweet;
import org.stackexchange.persistence.setup.TwitterAccountToStackAccount;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.service.TweetMetaLocalService;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TweetService;
import org.tweet.twitter.service.live.TwitterReadLiveService;
import org.tweet.twitter.util.TweetUtil;

import com.google.api.client.util.Preconditions;

@Component
@Profile(SpringProfileUtil.DEPLOYED)
public class RecreateMissingQuestionTweetsUpgrader implements ApplicationListener<AfterSetupEvent>, IRecreateMissingQuestionTweetsUpgrader {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    @Autowired
    private IQuestionTweetJpaDAO questionTweetDao;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private TweetMetaLocalService tweetMetaLocalService;

    @Autowired
    private TwitterReadLiveService twitterReadLiveService;

    @Autowired
    private LinkLiveService linkLiveService;

    public RecreateMissingQuestionTweetsUpgrader() {
        super();
    }

    //

    @Override
    // @Async
    public void onApplicationEvent(final AfterSetupEvent event) {
        if (env.getProperty("setup.upgrade.retweetmissing.do", Boolean.class)) {
            logger.info("Starting to execute the AddTextToRetweetsUpgrader Upgrader");
            recreateLocalQuestionTweetsFromLiveTweets();
            logger.info("Finished executing the AddTextToRetweetsUpgrader Upgrader");
        }
    }

    // util

    @Override
    public void recreateLocalQuestionTweetsFromLiveTweets() {
        logger.info("Executing the RecreateMissingRetweetsUpgrader Upgrader");
        for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
            try {
                logger.info("Recreating all missing retweets of twitterAccount= " + twitterAccount.name());
                recreateLocalQuestionTweetsOnAccount(twitterAccount.name());
            } catch (final RuntimeException ex) {
                logger.error("Unable to recreate missing retweets of twitterAccount= " + twitterAccount.name(), ex);
            }
        }
    }

    @Override
    public void recreateLocalQuestionTweetsOnAccount(final String twitterAccount) {
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
            processLiveTweetInternal(tweet, twitterAccount, tweet.getCreatedAt());
        } catch (final RuntimeException ex) {
            final String tweetUrl = "https://twitter.com/" + tweet.getFromUser() + "/status/" + tweet.getId();
            logger.error("Unable to recreate retweet: " + TweetUtil.getText(tweet) + " from \nlive tweet url= " + tweetUrl, ex);
        }
    }

    private final void processLiveTweetInternal(final Tweet rawTweet, final String twitterAccount, final Date when) {
        final String rawTweetText = TweetUtil.getText(rawTweet);
        final boolean linkingToSe = linkLiveService.countLinksToAnyDomain(rawTweet, LinkUtil.seDomains) > 0;
        if (!linkingToSe) {
            logger.debug("Tweet is not linking to Stack Exchange - not a retweet= {}", rawTweetText);
            return;
        }

        final String extractQuestionIdFromLiveTweet = extractQuestionIdFromTweet(rawTweet);
        final QuestionTweet questionTweet = questionTweetDao.findByQuestionIdAndTwitterAccount(extractQuestionIdFromLiveTweet, twitterAccount);
        if (questionTweet != null) {
            logger.debug("Found local question tweet: " + questionTweet);
            return;
        }

        final StackSite stackSite = TwitterAccountToStackAccount.twitterAccountToStackSite(TwitterAccountEnum.valueOf(twitterAccount));
        final QuestionTweet qt = new QuestionTweet(extractQuestionIdFromLiveTweet, twitterAccount, stackSite.name(), rawTweet.getCreatedAt());
        questionTweetDao.save(qt);

        logger.info("Created on twitterAccount= {}, new Question Tweet= {}", twitterAccount, qt);
    }

    @Override
    public final String extractQuestionIdFromTweet(final Tweet tweet) {
        final List<String> linksToSe = linkLiveService.getLinksToAnyDomain(tweet, LinkUtil.seDomains);
        Preconditions.checkState(linksToSe.size() == 1);
        String linkToSe = linksToSe.get(0);
        final int start = linkToSe.indexOf("questions/") + 10;
        linkToSe = linkToSe.substring(start);
        final int end = linkToSe.indexOf("/");

        return linkToSe.substring(0, end);
    }

}
