package org.tweet.twitter.evaluator;

import java.util.List;

public interface IEvaluator {

    boolean isRejectedByBannedRegexExpressions(final String tweet);

    boolean isRejectedByContainsKeywordMaybe(final List<String> tweetTokens, final String tweet);

    boolean isTweetBanned(final String originalTweet);

}
