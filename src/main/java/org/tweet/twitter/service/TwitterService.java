package org.tweet.twitter.service;

import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

@Service
public class TwitterService {

    public TwitterService() {
        super();
    }

    // API

    public void tweet(final Twitter twitter, final String tweetText) {
        twitter.timelineOperations().updateStatus(tweetText);
    }

}
