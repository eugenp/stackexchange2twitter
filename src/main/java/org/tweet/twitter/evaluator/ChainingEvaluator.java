package org.tweet.twitter.evaluator;

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

}
