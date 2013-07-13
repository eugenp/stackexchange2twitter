package org.tweet.twitter.util;

import java.util.List;

import org.common.text.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public final class TwitterUtil {
    final static Logger logger = LoggerFactory.getLogger(TwitterUtil.class);

    final static Splitter splitter = Splitter.on(' ').omitEmptyStrings().trimResults(); // if this would include more chars, then recreating the tweet would not be exact
    final static Joiner joiner = Joiner.on(' ');

    final static List<String> bannedKeywords = Lists.newArrayList("buy", "freelance", "job", "consulting", "hire", "hiring", "careers", "need", "escort", "escorts", "xxx");
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
                logger.debug("Rejecting the following tweet because a token matches one of the banned keywords: token={}; tweet=\n{}", tweetToken, text);
                return true;
            }
        }

        // Get 42% off Secrets of the #JavaScript Ninja on Amazon http://amzn.to/12kkaUn @jeresig
        if (text.matches("Get (.)* on Amazon.*")) {
            return true;
        }

        return false;
    }

}
