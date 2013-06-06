package org.tweet.twitter.util;

import com.google.common.base.Preconditions;

public final class TwitterUtil {

    private TwitterUtil() {
        throw new AssertionError();
    }

    // API

    public static boolean isTweetTextValid(final String text) {
        return text.length() <= 120;
    }

    public static boolean isTweetValid(final String fullTweet) {
        return fullTweet.length() <= 140;
    }

    public static String prepareTweet(final String text, final String link) {
        Preconditions.checkNotNull(text);
        Preconditions.checkNotNull(link);

        final String textOfTweet = text;
        final String tweet = textOfTweet + " - " + link;
        return tweet;
    }

    public static String prepareTweetNew(final String text, final String link) {
        Preconditions.checkNotNull(text);
        Preconditions.checkNotNull(link);

        String textOfTweet = null;
        if (text.length() < 120) {
            textOfTweet = text;
        } else {
            textOfTweet = text.substring(0, 120);
        }
        final String tweet = textOfTweet + " - " + link;
        return tweet;
    }

}
