package org.tweet.twitter.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;

import com.google.common.collect.Lists;

public final class TweetUtil {
    final static Logger logger = LoggerFactory.getLogger(TweetUtil.class);

    /*
     * "it" - +1 +1 +1 +1
     * "el" - +1
     * "nl" - +1 +1 +1
     * "ja" - +1
     * "ru" - +1 +1 +1 +1
     * "pt" - +1
     * "nl" - +1
    */
    public final static List<String> acceptedUserLang = Lists.newArrayList("en", "en-gb", "de", "es", "fr");

    public final static List<String> goodSingleMentionVariants = Lists.newArrayList(" - via @", " via @");

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

    public static Tweet getTweet(final Tweet tweet) {
        if (tweet.getRetweetedStatus() != null) {
            return tweet.getRetweetedStatus();
        }

        return tweet;
    }

}
