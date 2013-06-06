package org.tweet.twitter.util;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

public final class TwitterUtil {

    private static Splitter splitter = Splitter.on(' ').omitEmptyStrings().trimResults();;
    private static Joiner joiner = Joiner.on(' ');

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

    // pre-processing

    public static String hashWords(final String fullTweet, final List<String> wordsToHash) {
        final Iterable<String> tokens = splitter.split(fullTweet);
        if (fullTweet.length() + countWordsToHash(tokens, wordsToHash) > 140) {
            return fullTweet;
        }

        final Iterable<String> transformedTokens = Iterables.transform(tokens, new HashWordFunction(wordsToHash));

        final String processedTweet = joiner.join(transformedTokens);
        return processedTweet;
    }

    // utils

    static int countWordsToHash(final Iterable<String> tokens, final List<String> wordsToHash) {
        int countWordsToHash = 0;
        for (final String token : tokens) {
            if (wordsToHash.contains(token)) {
                countWordsToHash++;
            }
        }

        return countWordsToHash;
    }

}
