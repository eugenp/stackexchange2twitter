package org.tweet.twitter.evaluator;

import java.util.List;

import com.google.api.client.util.Preconditions;

public class ChainingEvaluator implements IEvaluator {

    private final IEvaluator[] evaluators;

    public ChainingEvaluator(final IEvaluator... evaluators) {
        this.evaluators = Preconditions.checkNotNull(evaluators);
    }

    // API

    @Override
    public final boolean isRejectedByBannedRegexExpressions(final String tweet) {
        for (final IEvaluator evaluator : evaluators) {
            if (evaluator.isRejectedByBannedRegexExpressions(tweet)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public final boolean isRejectedByContainsKeywordMaybe(final List<String> tweetTokens, final String tweet) {
        for (final IEvaluator evaluator : evaluators) {
            if (evaluator.isRejectedByContainsKeywordMaybe(tweetTokens, tweet)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public final boolean isTweetBanned(final String originalTweet) {
        for (final IEvaluator evaluator : evaluators) {
            if (evaluator.isTweetBanned(originalTweet)) {
                return true;
            }
        }

        return false;
    }

}
