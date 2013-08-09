package org.tweet.twitter.service.live;

import org.common.metrics.MetricsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.OperationNotPermittedException;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TwitterTemplateCreator;

import com.codahale.metrics.MetricRegistry;

@Service
@Profile(SpringProfileUtil.WRITE_PRODUCTION)
public class TwitterWriteLiveService implements ITwitterWriteLiveService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterTemplateCreator twitterCreator;

    @Autowired
    private MetricRegistry metrics;

    public TwitterWriteLiveService() {
        super();
    }

    // API

    // write

    @Override
    public synchronized boolean retweet(final String twitterAccount, final long tweetId) {
        final Twitter twitterTemplate = twitterCreator.createTwitterTemplate(twitterAccount);
        try {
            twitterTemplate.timelineOperations().retweet(tweetId);
            metrics.counter(MetricsUtil.Meta.TWITTER_WRITE_OK).inc();

            return true;
        } catch (final RuntimeException ex) {
            logger.error("Unable to retweet on twitterAccount= " + twitterAccount + "; tweetid: " + tweetId, ex);
        }

        return false;
    }

    @Override
    public synchronized boolean tweet(final String twitterAccount, final String tweetText) {
        final Twitter twitterTemplate = twitterCreator.createTwitterTemplate(twitterAccount);

        try {
            twitterTemplate.timelineOperations().updateStatus(tweetText);
            metrics.counter(MetricsUtil.Meta.TWITTER_WRITE_OK).inc();

            return true;
        } catch (final OperationNotPermittedException notPermittedEx) {
            // TODO: will be warn, for now, to see how often it happens and why, is error
            logger.error("Unable to tweet on twitterAccount= " + twitterAccount + "; tweet: " + tweetText, notPermittedEx);
            // possible cause: over 140 chars
            // another possible cause: OperationNotPermittedException: Status is a duplicate
        } catch (final RuntimeException ex) {
            if (ex.getCause() instanceof NotAuthorizedException) {
                // TODO: go to warn soon
                logger.error("Unable to authenticate - tweeting on twitterAccount= " + twitterAccount + "; tweet: " + tweetText, ex);
            }
            logger.error("Generic Unable to tweet - 1 - on twitterAccount= " + twitterAccount + "; tweet: " + tweetText, ex);
        }

        return false;
    }

    @Override
    public synchronized boolean tweet(final String twitterAccount, final String textToTweet, final Tweet originalTweetForLogging) {
        final Twitter twitterTemplate = twitterCreator.createTwitterTemplate(twitterAccount);

        try {
            twitterTemplate.timelineOperations().updateStatus(textToTweet);
            metrics.counter(MetricsUtil.Meta.TWITTER_WRITE_OK).inc();

            return true;
        } catch (final OperationNotPermittedException notPermittedEx) {
            // TODO: will be warn, for now, to see how often it happens and why, is error
            final String tweetUrl = "https://twitter.com/" + originalTweetForLogging.getFromUser() + "/status/" + originalTweetForLogging.getId();
            logger.error("Unable to tweet on twitterAccount= " + twitterAccount + "; tweet: " + textToTweet + "\nfrom original: " + tweetUrl, notPermittedEx);
            // possible cause: over 140 chars
            // another possible cause: OperationNotPermittedException: Status is a duplicate
        } catch (final RuntimeException ex) {
            final String tweetUrl = "https://twitter.com/" + originalTweetForLogging.getFromUser() + "/status/" + originalTweetForLogging.getId();
            logger.error("Generic Unable to tweet - 2 - on twitterAccount= " + twitterAccount + "; tweet: " + textToTweet + "\nfrom original: " + tweetUrl, ex);
        }

        return false;
    }

}
