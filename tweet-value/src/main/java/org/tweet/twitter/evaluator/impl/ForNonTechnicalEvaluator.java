package org.tweet.twitter.evaluator.impl;

import java.util.List;

import org.tweet.twitter.evaluator.AbstractEvaluator;
import org.tweet.twitter.util.ErrorUtil;
import org.tweet.twitter.util.TwitterUtil.ForTweeting.NonTechnicalOnly;

public class ForNonTechnicalEvaluator extends AbstractEvaluator {

    public ForNonTechnicalEvaluator() {
        super();
    }

    // API

    @Override
    public final boolean isRejectedByBannedRegexExpressions(final String tweet) {
        configureForRegexExpression(NonTechnicalOnly.acceptedRegExes, NonTechnicalOnly.bannedRegExesMaybe, NonTechnicalOnly.bannedRegExes, "analysis-non-tech", ErrorUtil.bannedRegExesMaybeErrorsNonTech);
        return super.isRejectedByBannedRegexExpressions(tweet);
    }

    @Override
    public final boolean isRejectedByContainsKeywordMaybe(final List<String> tweetTokens, final String tweet) {
        configureForContainsKeyword(NonTechnicalOnly.bannedContainsKeywordsMaybe, NonTechnicalOnly.acceptedContainsKeywordsOverrides, ErrorUtil.bannedContainsMaybeErrorsForNonTech);
        return super.isRejectedByContainsKeywordMaybe(tweetTokens, tweet);
    }

    @Override
    public final boolean isTweetBanned(final String originalTweet) {
        configure(NonTechnicalOnly.bannedExpressionsMaybe, NonTechnicalOnly.bannedExpressions, NonTechnicalOnly.bannedContainsKeywords, NonTechnicalOnly.bannedStartsWithExprs);
        return super.isTweetBanned(originalTweet);
    }

}
