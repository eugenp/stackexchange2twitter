package org.tweet.meta.service;

import org.springframework.social.twitter.api.Tweet;

public final class RetweetStrategy {

    public RetweetStrategy() {
        super();
    }

    // API

    public final boolean shouldRetweet(final Tweet tweet) {
        if (tweet.getRetweetCount() > 15) {
            return false;
        }
        if (Math.random() < 0.25) {
            return true;
        }

        return false;
    }

}
