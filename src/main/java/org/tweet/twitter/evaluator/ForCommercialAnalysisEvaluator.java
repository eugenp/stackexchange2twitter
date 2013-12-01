package org.tweet.twitter.evaluator;

import java.util.List;

import org.tweet.twitter.util.ErrorUtil;
import org.tweet.twitter.util.TwitterUtil.ForAnalysis;

public class ForCommercialAnalysisEvaluator extends AbstractEvaluator {

    public ForCommercialAnalysisEvaluator() {
        super();
    }

    // API

    @Override
    public final boolean isRejectedByBannedRegexExpressions(final String tweet) {
        configureForRegexExpression(ForAnalysis.Commercial.acceptedRegExes, ForAnalysis.Commercial.bannedRegExesMaybe, ForAnalysis.Commercial.bannedRegExes, "analysis-commercial", ErrorUtil.bannedCommercialRegExesMaybeErrors);
        return super.isRejectedByBannedRegexExpressions(tweet);
    }

    @Override
    public final boolean isRejectedByContainsKeywordMaybe(final List<String> tweetTokens, final String tweet) {
        configureForContainsKeyword(ForAnalysis.Commercial.bannedContainsKeywordsMaybe, ForAnalysis.Commercial.acceptedContainsKeywordsOverrides, ErrorUtil.bannedCommercialContainsMaybeErrors);
        return super.isRejectedByContainsKeywordMaybe(tweetTokens, tweet);
    }

    @Override
    public final boolean isTweetBanned(final String originalTweet) {
        configure(ForAnalysis.Commercial.bannedExpressionsMaybe, ForAnalysis.Commercial.bannedExpressions, ForAnalysis.Commercial.bannedContainsKeywords, ForAnalysis.Commercial.bannedStartsWithExprs);
        return super.isTweetBanned(originalTweet);
    }

}
