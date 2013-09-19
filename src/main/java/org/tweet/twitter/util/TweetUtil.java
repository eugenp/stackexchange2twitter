package org.tweet.twitter.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;

import com.google.common.collect.Lists;

public final class TweetUtil {
    final static Logger logger = LoggerFactory.getLogger(TweetUtil.class);

    /*
     * "ar"    - 
     * "ca"    - 
     * "el"    - 
     * "hu"    -
     * "it"    - 
     * "id"    - 
     * "ja"    -
     * "ko"    - 
     * "nl"    - 
     * "pt"    - 
     * "pl"    - 
     * "ru"    - 
     * "sl"    - 
     * "sk"    - 
     * "tr"    - 
     * "und"   - 
     * "xx-lc" - 
    */
    public final static List<String> acceptedUserLang = Lists.newArrayList("ca", "de", "en", "en-gb"); // "es", "fr", "it", "nl", "ru", "und", "id"
    public final static List<String> rejectedUserLang = Lists.newArrayList(); // "bg", "sv", "he", "ar", "sk"
    // still very much in progress

    /*
     * "ar"    - 
     * "ca"    - 
     * "da"    - +1 +1
     * "el"    - 
     * "et"    - +1 +1
     * "el"    - -1  
     * -"fi"    - +1 -1  
     * "hu"    - -1
     * "ht"    - -1 
     * "it"    - 
     * "id"    - 
     * "ja"    -
     * "ko"    - +1
     * "nl"    - 
     * "uk"    - -1
     * -"pt"    - -1 -1 -1 -1 = rejecting
     * "pl"    - +1 +1
     * "ru"    - 
     * -"sl"    - +1 +1 +1 -1
     * "sk"    - 
     * "tr"    - 
     * "tl"    - +1 +1 +1
     * "und"   - 
     * "xx-lc" -
     * "vi"    - +1 +1 +1 +1 +1 +1 +1  = accepting
    */
    public final static List<String> acceptedTweetLang = Lists.newArrayList("ca", "de", "en", "en-gb", "es", "fr", "it", "nl", "ru", "und", "id", "vi");
    public final static List<String> rejectedTweetLang = Lists.newArrayList("ar", "bg", "he", "pt", "sv", "sk");

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
