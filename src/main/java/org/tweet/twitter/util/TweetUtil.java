package org.tweet.twitter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;

public final class TweetUtil {
    final static Logger logger = LoggerFactory.getLogger(TweetUtil.class);

    private TweetUtil() {
        throw new AssertionError();
    }

    // API

    public static String getText(final Tweet tweet) {
        String text = tweet.getText();
        if (tweet.getRetweetedStatus() != null) {
            text = tweet.getRetweetedStatus().getText();
        }

        return text;
    }

}
