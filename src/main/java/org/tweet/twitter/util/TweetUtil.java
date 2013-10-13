package org.tweet.twitter.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;

import com.google.common.collect.Lists;

public final class TweetUtil {
    final static Logger logger = LoggerFactory.getLogger(TweetUtil.class);

    // @formatter:off
    public final static List<String> acceptedUserLangForAnalysis = Lists.newArrayList(
        "ca", 
        "de", 
        "en", 
        "en-gb", 
        "it"
    ); 
    public final static List<String> rejectedUserLangForAnalysis = Lists.newArrayList(
        "es"
    ); 
    // ! PAY ATTENTION - not English
    /*
     * "ar"    - +1 | -1
     * "ca"    - |
     * "cs"    - +1 +1 | 
     * "el"    - |
     * "es"    - +1 +1 +1 +1 +1 +1 +1 +1 | -1 -1 -1 -1 -1 -1 -1 -1 -1 - rejected
     * "fr"    - 36 +1 | 4 -1 | at the full user level (not English): ~0 -1 - gathering some more data (not much) (06.10)
     * "hu"    - +1 | 
     * "id"    - +1 +1 | 
     * "it"    - 22 +1 | 4 -1 - rejecting for tweeting, accepting for analysis
     * "id"    - +1 | -1 -1 
     * "ja"    - 9 +1 | - looking for just a few more datapoints 
     * "lv"    - +1 |
     * "ko"    - +1 | -1 
     * "no"    - +1 +1
     * "nl"    - 16 +1 | -1 -1 -1 -1
     * "pl"    - +1 +1 +1 +1 | -1 
     * "pt"    - 14 +1 |  - almost there 
     * "ru"    - 7 +1 |  
     * "sl"    - |
     * "sv"    - +1 | -1
     * "sk"    - |
     * "tr"    - 9 +1 |
     * "und"   - |
     * "uk"    - +1 |
     * "xx-lc" - |
    */
    public final static List<String> acceptedUserLangForTweeting = Lists.newArrayList(
        "ca", 
        "de", 
        "en", 
        "en-gb"
    ); 
    public final static List<String> rejectedUserLangForTweeting = Lists.newArrayList(
        "es", 
        "it"
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
        "ne",
        "no", 
        "pl", 
        "pt", 
        "sv", 
        "sk", 
        "ta", 
        "tl", 
        "th", 
        "tr", 
        "uk", 
        "ur",
        "zh"
    );
    /*
     * "ar"    - 
     * "bo"    - | -1 -1
     * "ca"    - 
     * "chr"   - | -1 -1
     * "da"    - ~21 +1 +1 +1 +1 +1 | -1 -1 -1 -1 -1 -1 -1 -1 - reject for tweeting, accept for analysis
     * "et"    - ~20 +1 | - accepting
     * "el"    - | -1 -1 -1 | +1 - rejecting
     * -"fi"   - ~10 +1 | ~10 -1 - rejecting
     * "fa"    - | -1 -1 -1 -1 -1 -1 -1 - rejecting
     * "gu"    - | -1 -1 -1 -1
     * "hi"    - | -1 -1 -1 -1 -1 -1 -1 - rejecting
     * "hu"    - +1 +1 +1 | ~12 -1 -1 - rejecting
     * "ht"    - +1 | -1 -1 -1 -1 - rejecting // some English but plain weird tweets
     * "hy"    - | -1
     * "it"    - 
     * "iu"    - -1 -1 -1 -1 -1 - rejecting
     * "is"    - 8 +1 | -1 -1 -1 // weirdly short tweets - all of them - not sure...
     * "id"    - 
     * "ja"    - | ~12 -1 - rejecting
     * "ka"    - | -1
     * "kn"    - | -1 -1 
     * "ko"    - +1 | -1 -1 -1 -1 - rejecting
     * "lt"    - ~10 +1 | ~10 -1 - rejecting
     * "lv"    - +1 +1 +1 +1 +1 +1 | -1 -1 -1 -1 -1 -1 -1 -1 -1 - rejecting
     * "ml"    - -1 -1
     * "ne"    - | 5 -1 - rejecting
     * "nl"    - | -1 -1 -1 - rejecting
     * "no"    - 20 +1 | 10 -1 - rejecting
     * "or"    - | -1
     * "uk"    - | -1 -1 -1 -1 -1 -1 -1 - rejecting
     * "ur"    - | -1 -1 -1 -1 -1 -1 -1 -1 -1 - rejecting
     * -"pt"   - | -1 -1 -1 -1 = rejecting
     * "pl"    - ~25 | -1 -1 -1 -1  - reject for tweeting, accept for analysis
     * "ru"    - | 
     * -"sl"   - +1 +1 +1 +1 +1 +1 +1 +1 +1 +1 +1 +1 | -1 - accepting
     * "sk"    - | 
     * "ta"    - | -1 -1 -1 -1 -1 - rejected
     * "te"    - | -1 -1 -1
     * "th"    - | 7 -1 - rejecting
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
        "ne", 
        "nl", 
        "no", 
        "pt", 
        "sv", 
        "sk", 
        "ta", 
        "tl", 
        "th", 
        "tr", 
        "uk", 
        "ur",
        "zh"
    );
    // @formatter:on

    public final static List<String> goodSingleMentionVariants = Lists.newArrayList(" - via @", " via @"); // another: "Very cool."

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
