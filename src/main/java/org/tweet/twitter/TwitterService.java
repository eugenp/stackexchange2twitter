package org.tweet.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

@Service
public class TwitterService {

    @Autowired
    private Twitter twitter;

    public TwitterService() {
        super();
    }

    // API

    public void tweet(final String tweetText) {
        twitter.timelineOperations().updateStatus(tweetText);
    }

}
