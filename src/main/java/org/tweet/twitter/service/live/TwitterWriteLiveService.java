package org.tweet.twitter.service.live;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.OperationNotPermittedException;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TwitterTemplateCreator;

@Service
@Profile(SpringProfileUtil.PRODUCTION)
public class TwitterWriteLiveService implements ITwitterWriteLiveService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterTemplateCreator twitterCreator;

    public TwitterWriteLiveService() {
        super();
    }

    // API

    // write

    @Override
    public boolean retweet(final String twitterAccount, final long tweetId) {
        final Twitter twitterTemplate = twitterCreator.getTwitterTemplate(twitterAccount);
        try {
            twitterTemplate.timelineOperations().retweet(tweetId);
            return true;
        } catch (final RuntimeException ex) {
            logger.error("Unable to retweet on twitterAccount= " + twitterAccount + "; tweetid: " + tweetId, ex);
        }

        return false;
    }

    @Override
    public boolean tweet(final String twitterAccount, final String tweetText) {
        final Twitter twitterTemplate = twitterCreator.getTwitterTemplate(twitterAccount);

        try {
            twitterTemplate.timelineOperations().updateStatus(tweetText);
            return true;
        } catch (final OperationNotPermittedException notPermittedEx) {
            // TODO: will be warn, for now, to see how often it happens and why, is error
            logger.error("Unable to tweet on twitterAccount= " + twitterAccount + "; tweet: " + tweetText, notPermittedEx);
            // possible cause: over 140 chars
            // another possible cause: OperationNotPermittedException: Status is a duplicate
        } catch (final RuntimeException ex) {
            logger.error("Generic Unable to tweet on twitterAccount= " + twitterAccount + "; tweet: " + tweetText, ex);
        }

        return false;
    }

    @Override
    public boolean tweet(final String twitterAccount, final String textToTweet, final Tweet originalTweet) {
        final Twitter twitterTemplate = twitterCreator.getTwitterTemplate(twitterAccount);

        try {
            twitterTemplate.timelineOperations().updateStatus(textToTweet);
            return true;
        } catch (final OperationNotPermittedException notPermittedEx) {
            // TODO: will be warn, for now, to see how often it happens and why, is error
            final String tweetUrl = "https://twitter.com/" + originalTweet.getFromUser() + "/status/" + originalTweet.getId();
            logger.error("Unable to tweet on twitterAccount= " + twitterAccount + "; tweet: " + textToTweet + "\nfrom original: " + tweetUrl, notPermittedEx);
            // possible cause: over 140 chars
            // another possible cause: OperationNotPermittedException: Status is a duplicate
        } catch (final RuntimeException ex) {
            final String tweetUrl = "https://twitter.com/" + originalTweet.getFromUser() + "/status/" + originalTweet.getId();
            logger.error("Generic Unable to tweet on twitterAccount= " + twitterAccount + "; tweet: " + textToTweet + "\nfrom original: " + tweetUrl, ex);
        }

        return false;
    }

}
