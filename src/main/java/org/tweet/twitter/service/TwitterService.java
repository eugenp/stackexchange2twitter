package org.tweet.twitter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

@Service
public class TwitterService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public TwitterService() {
        super();
    }

    // API

    public void tweet(final Twitter twitter, final String tweetText) {
        try {
            twitter.timelineOperations().updateStatus(tweetText);
        } catch (final RuntimeException ex) {
            logger.error("Unable to tweet" + tweetText, ex);
        }
    }
}
