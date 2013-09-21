package org.tweet.twitter.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;

import com.google.common.collect.Lists;

public final class TweetUtil {
    final static Logger logger = LoggerFactory.getLogger(TweetUtil.class);

    // ! PAY ATTENTION - not English
    /*
     * "ar"    - 
     * "ca"    - 
     * "el"    - 
     * "es"    - +1 +1 +1 +1 +1 +1 | -1 -1 | at the full user level (not English): +1 +1
     * "fr"    - +1 +1 +1 +1 +1 | at the full user level (not English): ~0 -1
     * "hu"    -
     * "it"    - +1 +1
     * "id"    - 
     * "ja"    - +1
     * "ko"    - | at the full user level (not English): +1 
     * "nl"    - 
     * "pt"    - +1 +1 +1
     * "pl"    - | at the full user level (not English): +1 
     * "ru"    - +1
     * "sl"    - 
     * "sv"    - | at the full user level (not English): -1
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
     * "da"    - ~12 +1 +1 +1 | -1 -1 -1 -1
     * "et"    - ~20 +1 - accepting
     * "el"    - -1 -1 -1 | +1 - rejecting
     * -"fi"    - ~10 +1 | ~10 -1 - rejecting
     * "fa"    - -1
     * "hi"    - -1
     * "hu"    - -1 -1
     * "ht"    - -1 -1 -1 -1 | +1 - rejecting // some English but plain weird tweets
     * "it"    - 
     * "id"    - 
     * "ja"    - ~12 -1 - rejecting
     * "ko"    - +1 | -1 -1 -1 -1 - rejecting
     * "lt"    - ~10 +1 | ~10 -1 - rejecting
     * "lv"    - +1 +1 +1 
     * "nl"    - 
     * "no"    - +1 +1 +1 +1 
     * "uk"    - -1 -1 -1 -1 -1 -1 -1 - rejecting
     * -"pt"    - -1 -1 -1 -1 = rejecting
     * "pl"    - ~12 +1 +1 +1 +1 +1 +1 +1 +1 +1 +1 +1 +1 | -1 -1
     * "ru"    - 
     * -"sl"    - +1 +1 +1 +1 +1 +1 +1 +1 +1 +1 +1 +1 | -1 - accepting
     * "sk"    - 
     * "ta"    - -1 -1 -1 -1 -1 - rejected
     * "tr"    - -1 -1 -1 -1 -1 -1 -1 | +1 +1 +1 - rejected
     * "tl"    - ~12 +1 | -1 -1 -1 -1 -1 - rejecting
     * "und"   - 
     * "xx-lc" -
     * "vi"    - +1 +1 +1 +1 +1 +1 +1  = accepting
     * "zh"    - -1
    */
    public final static List<String> acceptedTweetLang = Lists.newArrayList("ca", "de", "en", "en-gb", "es", "et", "fr", "it", "nl", "ru", "sl", "und", "id", "vi");
    public final static List<String> rejectedTweetLang = Lists.newArrayList("ar", "bg", "el", "fi", "he", "ht", "ja", "ko", "lt", "pt", "sv", "sk", "ta", "tl", "tr", "uk");

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
