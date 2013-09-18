package org.tweet.twitter.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;

import com.google.common.collect.Lists;

public final class TweetUtil {
    final static Logger logger = LoggerFactory.getLogger(TweetUtil.class);

    /*
     * "ar"    - -1 -1
     * "ca"    - +1 +1 +1 +1 +1 +1 +1 - accepting
     * "el"    - +1 +1 +1
     * "it"    - +1 +1 +1 +1 +1 - accepting
     * "id"    - +1 +1 +1 +1 +1 +1
     * "ja"    - +1 +1 +1 +1 +1
     * "ko"    - +1 +1
     * "nl"    - +1 +1 +1 +1 +1 +1 - accepting
     * -"pt"   - +1 +1 +1 +1 +1 -1 +1 +1 +1 +1 +1 +1
     * "pl"    - +1 +1 +1 +1
     * "ru"    - +1 +1 +1 +1 +1 - accepting
     * "sl"    - +1 +1
     * -"tr"   - +1 +1 +1 +1 -1
     * "und"   - +1 +1 +1 +1 +1 +1 - accepting
     * "xx-lc" - +1
    */
    public final static List<String> acceptedUserLang = Lists.newArrayList("ca", "de", "en", "en-gb", "es", "fr", "it", "nl", "ru", "und");
    public final static List<String> rejectedUserLang = Lists.newArrayList("bg", "sv", "he");

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
