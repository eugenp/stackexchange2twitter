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
     * "ar"    - |
     * "ca"    - |
     * "cs"    - +1 +1 | 
     * "el"    - |
     * "es"    - +1 +1 +1 +1 +1 +1 +1 +1 | -1 -1 -1 -1 -1 -1 -1 -1 -1 - rejected
     * "fr"    - 16 +1 | at the full user level (not English): ~0 -1
     * "hu"    - +1 | 
     * "it"    - +1 +1 +1 +1 +1 +1 | -1
     * "id"    - | -1 -1 
     * "ja"    - +1 +1 +1 | 
     * "lv"    - +1 |
     * "ko"    - | -1 
     * "no"    - +1
     * "nl"    - +1 +1 +1 +1 | -1 -1
     * "pl"    - | -1 
     * "pt"    - +1 +1 +1 +1 +1 +1 +1 | 
     * "ru"    - +1 +1 +1 | 
     * "sl"    - |
     * "sv"    - | -1
     * "sk"    - |
     * "tr"    - |
     * "und"   - |
     * "uk"    - +1 |
     * "xx-lc" - |
    */

    // @formatter:off
    public final static List<String> acceptedUserLang = Lists.newArrayList(
        "ca", 
        "de", 
        "en", 
        "en-gb"
    ); 
    public final static List<String> rejectedUserLang = Lists.newArrayList(
        "es"
    ); 
    // @formatter:on

    // @formatter:off
    public final static List<String> acceptedTweetLangForTweeting = Lists.newArrayList(
        "ca", 
        "de", 
        "en", 
        "en-gb", 
        "es", 
        "et", 
        "fr", 
        "id", 
        "it", 
        "nl", 
        "ru", 
        "sl", 
        "und", 
        "vi"
    );
    public final static List<String> rejectedTweetLangForTweeting = Lists.newArrayList(
        "ar", 
        "bg", 
        "da", 
        "el", 
        "fa", 
        "fi", 
        "he", 
        "hi",
        "hu", 
        "ht", 
        "iu", 
        "ja", 
        "ko", 
        "lt", 
        "nl", 
        "pl", 
        "pt", 
        "sv", 
        "sk", 
        "ta", 
        "tl", 
        "tr", 
        "uk", 
        "zh"
    );
    /*
     * "ar"    - 
     * "bo"    - -1
     * "ca"    - 
     * "da"    - ~21 +1 +1 +1 +1 +1 | -1 -1 -1 -1 -1 -1 -1 -1 - reject for tweeting, accept for analysis
     * "et"    - ~20 +1 - accepting
     * "el"    - | -1 -1 -1 | +1 - rejecting
     * -"fi"   - ~10 +1 | ~10 -1 - rejecting
     * "fa"    - | -1 -1 -1 -1 -1 -1 -1 - rejecting
     * "hi"    - | -1 -1 -1 -1 -1 -1 -1 - rejecting
     * "hu"    - +1 +1 +1 | ~12 -1 -1 - rejecting
     * "ht"    - +1 | -1 -1 -1 -1 - rejecting // some English but plain weird tweets
     * "it"    - 
     * "iu"    - -1 -1 -1 -1 -1 - rejecting
     * "is"    - +1 +1 +1 | -1
     * "id"    - 
     * "ja"    - | ~12 -1 - rejecting
     * "ko"    - +1 | -1 -1 -1 -1 - rejecting
     * "lt"    - ~10 +1 | ~10 -1 - rejecting
     * "lv"    - +1 +1 +1 +1 +1 +1 | -1 -1 -1 -1 -1 -1 -1 -1 -1 - rejecting
     * "ml"    - 
     * "nl"    - | -1 -1 -1 - rejecting
     * "no"    - 17 +1 | -1 -1 -1 -1 -1 -1
     * "uk"    - | -1 -1 -1 -1 -1 -1 -1 - rejecting
     * "ur"    - | -1 -1
     * -"pt"   - | -1 -1 -1 -1 = rejecting
     * "pl"    - ~25 | -1 -1 -1 -1  - reject for tweeting, accept for analysis
     * "ru"    - | 
     * -"sl"   - +1 +1 +1 +1 +1 +1 +1 +1 +1 +1 +1 +1 | -1 - accepting
     * "sk"    - | 
     * "ta"    - | -1 -1 -1 -1 -1 - rejected
     * "th"    - | -1 -1
     * "tr"    - +1 +1 +1 | -1 -1 -1 -1 -1 -1 -1 - rejected
     * "tl"    - ~12 +1 | -1 -1 -1 -1 -1 - rejecting
     * "und"   - | 
     * "xx-lc" - | 
     * "vi"    - +1 +1 +1 +1 +1 +1 +1  = accepting
     * "zh"    - | -1 -1 -1 -1 -1 -1 -1 - rejecting
    */
    public final static List<String> acceptedTweetLangForAnalysis = Lists.newArrayList(
        "ca", 
        "da", 
        "de", 
        "en", 
        "en-gb", 
        "es", 
        "et", 
        "fr", 
        "id", 
        "it", 
        "nl", 
        "pl", 
        "ru", 
        "sl", 
        "und", 
        "vi"
    );
    public final static List<String> rejectedTweetLangForAnalysis = Lists.newArrayList(
        "ar", 
        "bg", 
        "el", 
        "fa", 
        "fi", 
        "he", 
        "hi",
        "hu", 
        "ht", 
        "iu", 
        "ja", 
        "ko", 
        "lt", 
        "lv", 
        "nl", 
        "pt", 
        "sv", 
        "sk", 
        "ta", 
        "tl", 
        "tr", 
        "uk", 
        "zh"
    );
    // @formatter:on

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
