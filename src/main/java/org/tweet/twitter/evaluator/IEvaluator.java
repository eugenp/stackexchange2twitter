package org.tweet.twitter.evaluator;

public interface IEvaluator {

    boolean isRejectedByBannedRegexExpressions(final String tweet);

}
