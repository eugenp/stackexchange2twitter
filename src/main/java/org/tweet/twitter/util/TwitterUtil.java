package org.tweet.twitter.util;

import java.util.List;

import org.common.text.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public final class TwitterUtil {
    final static Logger logger = LoggerFactory.getLogger(TwitterUtil.class);

    final static Splitter splitter = Splitter.on(' ').omitEmptyStrings().trimResults(); // if this would include more chars, then recreating the tweet would not be exact
    final static Joiner joiner = Joiner.on(' ');

    final static List<String> bannedKeywords = Lists.newArrayList("freelance", "job", "consulting", "hire", "hiring", "careers", "need", "escort", "escorts", "xxx");
    final static List<String> bannedExpressions = Lists.newArrayList("web developer", "application engineer", "jobs");

    private TwitterUtil() {
        throw new AssertionError();
    }

    // API

    public static boolean isTweetTextValid(final String text) {
        final int linkNoInTweet = TextUtils.extractUrls(text).size();
        if (linkNoInTweet > 1) {
            return false;
        }

        return text.length() <= 122;
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

    public static String hashtagWords(final String fullTweet, final List<String> wordsToHash) {
        final Iterable<String> tokens = splitter.split(fullTweet);
        // if (fullTweet.length() + countWordsToHash(tokens, wordsToHash) > 140) {
        // return fullTweet;
        // }

        final Iterable<String> transformedTokens = Iterables.transform(tokens, new HashtagWordFunction(wordsToHash));

        final String processedTweet = joiner.join(transformedTokens);
        return processedTweet;
    }

    // utils

    static int countWordsToHash(final Iterable<String> tokens, final List<String> lowerCaseWordsToHash) {
        int countWordsToHash = 0;
        for (final String token : tokens) {
            if (lowerCaseWordsToHash.contains(token.toLowerCase())) {
                countWordsToHash++;
            }
        }

        return countWordsToHash;
    }

    public static boolean tweetContainsBannedKeywords(final String text) {
        final List<String> tweetTokens = Lists.newArrayList(Splitter.on(CharMatcher.anyOf(" ,?!:#.")).split(text));
        for (final String tweetToken : tweetTokens) {
            if (TwitterUtil.bannedKeywords.contains(tweetToken.toLowerCase())) {
                logger.info("Rejecting the following tweet because a token matches one of the banned keywords: token={}; tweet=\n{}", tweetToken, text);
                return true;
            }
        }
        return false;
    }

}
