package org.tweet.twitter.evaluator.impl;

import java.util.List;

import org.tweet.twitter.evaluator.AbstractEvaluator;
import org.tweet.twitter.util.ErrorUtil;
import org.tweet.twitter.util.TwitterUtil.ForTweeting;

public class ForTweetingEvaluator extends AbstractEvaluator {

    public ForTweetingEvaluator() {
        super();
    }

    // API

    @Override
    public final boolean isRejectedByBannedRegexExpressions(final String tweet) {
        configureForRegexExpression(ForTweeting.acceptedRegExes, ForTweeting.bannedRegExesMaybe, ForTweeting.bannedRegExes, "tweeting-generic", ErrorUtil.bannedRegExesMaybeErrors);
        return super.isRejectedByBannedRegexExpressions(tweet);
    }

    @Override
    public final boolean isRejectedByContainsKeywordMaybe(final List<String> tweetTokens, final String tweet) {
        configureForContainsKeyword(ForTweeting.bannedContainsKeywordsMaybe, ForTweeting.acceptedContainsKeywordsOverrides, ErrorUtil.bannedContainsMaybeErrorsForTweeting);
        return super.isRejectedByContainsKeywordMaybe(tweetTokens, tweet);
    }

    @Override
    public final boolean isTweetBanned(final String originalTweet) {
        configure(ForTweeting.bannedExpressionsMaybe, ForTweeting.bannedExpressions, ForTweeting.bannedContainsKeywords, ForTweeting.bannedStartsWithExprs);
        return super.isTweetBanned(originalTweet);
    }

}
