package org.tweet.twitter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.OperationNotPermittedException;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

@Service
public class TwitterWriteLiveService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterTemplateCreator twitterCreator;

    public TwitterWriteLiveService() {
        super();
    }

    // API

    // write

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

    public boolean tweet(final String twitterAccount, final String tweetText) {
        final Twitter twitterTemplate = twitterCreator.getTwitterTemplate(twitterAccount);

        try {
            twitterTemplate.timelineOperations().updateStatus(tweetText);
            return true;
        } catch (final OperationNotPermittedException notPermittedEx) {
            // likely tracked by https://github.com/eugenp/stackexchange2twitter/issues/11
            logger.warn("Unable to tweet on twitterAccount= " + twitterAccount + "; tweet: " + tweetText, notPermittedEx);

            // another possible cause: OperationNotPermittedException: Status is a duplicate
        } catch (final RuntimeException ex) {
            logger.error("Generic Unable to tweet on twitterAccount= " + twitterAccount + "; tweet: " + tweetText, ex);
        }

        return false;
    }

}
