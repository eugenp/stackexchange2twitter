package org.tweet.twitter.evaluator;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.classification.util.TweetSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tweet.twitter.util.ErrorUtil;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public abstract class AbstractEvaluator implements IEvaluator {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private List<String> acceptedRegExes;
    private List<String> bannedRegExesMaybe;
    private List<String> bannedRegExes;

    private String tag;

    private Map<String, Set<String>> bannedRegExesMaybeErrors;
    private List<String> acceptedContainsKeywordsOverrides;
    private List<String> bannedContainsKeywordsMaybe;
    private Map<String, Set<String>> bannedContainsMaybeErrors;

    private List<String> bannedExpressionsMaybe;

    private List<String> bannedExpressions;

    private List<String> bannedContainsKeywords;

    private List<String> bannedStartsWithExprs;

    public AbstractEvaluator() {
        super();
    }

    // API

    // config

    public final IEvaluator configure(final List<String> bannedExpressionsMaybe, final List<String> bannedExpressions, final List<String> bannedContainsKeywords, final List<String> bannedStartsWithExprs) {
        this.bannedExpressionsMaybe = Preconditions.checkNotNull(bannedExpressionsMaybe);
        this.bannedExpressions = Preconditions.checkNotNull(bannedExpressions);
        this.bannedContainsKeywords = Preconditions.checkNotNull(bannedContainsKeywords);
        this.bannedStartsWithExprs = Preconditions.checkNotNull(bannedStartsWithExprs);

        return this;
    }

    public final IEvaluator configureForRegexExpression(final List<String> acceptedRegExes, final List<String> bannedRegExesMaybe, final List<String> bannedRegExes, final String tag, final Map<String, Set<String>> bannedRegExesMaybeErrors) {
        this.acceptedRegExes = Preconditions.checkNotNull(acceptedRegExes);
        this.bannedRegExesMaybe = Preconditions.checkNotNull(bannedRegExesMaybe);
        this.bannedRegExes = Preconditions.checkNotNull(bannedRegExes);
        this.tag = Preconditions.checkNotNull(tag);

        this.bannedRegExesMaybeErrors = Preconditions.checkNotNull(bannedRegExesMaybeErrors);

        return this;
    }

    public final IEvaluator configureForContainsKeyword(final List<String> bannedContainsKeywordsMaybe, final List<String> acceptedContainsKeywordsOverrides, final Map<String, Set<String>> bannedContainsMaybeErrors) {
        this.bannedContainsKeywordsMaybe = Preconditions.checkNotNull(bannedContainsKeywordsMaybe);
        this.acceptedContainsKeywordsOverrides = Preconditions.checkNotNull(acceptedContainsKeywordsOverrides);
        this.bannedContainsMaybeErrors = Preconditions.checkNotNull(bannedContainsMaybeErrors);

        return this;
    }

    // is...

    /**
     * - <b>local</b> <br/>
     * Tweet can be banned FOR ANALYSIS ONLY by: <br/>
     * - expression (multiple words) <br/>
     * - single word - contains <br/>
     * - single word - starts with <br/>
     * - regular expression - matches <br/>
     */
    @Override
    public boolean isTweetBanned(final String originalTweet) {
        // by expression
        final String textLowerCase = originalTweet.toLowerCase();

        for (final String bannedExpressionMaybe : bannedExpressionsMaybe) {
            if (textLowerCase.contains(bannedExpressionMaybe)) {
                logger.error("1 - Rejecting the following tweet because a token matches the maybe banned expression={}; tweet=\n{}", bannedExpressionMaybe, textLowerCase);
                return true;
            }
        }

        for (final String bannedExpression : bannedExpressions) {
            if (textLowerCase.contains(bannedExpression)) {
                logger.debug("Rejecting the following tweet because a token matches the banned expression={}; tweet=\n{}", bannedExpression, originalTweet);
                return true;
            }
        }

        // by regex // moved before the contains logic

        if (isRejectedByBannedRegexExpressions(originalTweet)) {
            return true;
        }

        // by contains keyword - maybe

        final List<String> tweetTokens = Lists.newArrayList(Splitter.on(CharMatcher.anyOf(TweetSettings.TWEET_TOKENIZER + "#")).split(textLowerCase));
        if (isRejectedByContainsKeywordMaybe(tweetTokens, originalTweet)) {
            return true;
        }

        // by contains keyword

        for (final String tweetToken : tweetTokens) {
            if (bannedContainsKeywords.contains(tweetToken.toLowerCase())) {
                logger.debug("Rejecting the following tweet because a token matches one of the banned keywords: token= {}; tweet= \n{}", tweetToken, originalTweet);
                return true;
            }
        }

        // by starts with keyword
        for (final String bannedStartsWith : bannedStartsWithExprs) {
            if (textLowerCase.startsWith(bannedStartsWith)) {
                logger.debug("Rejecting the following tweet because it starts with= {}; tweet= \n{}", bannedStartsWith, originalTweet);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isRejectedByBannedRegexExpressions(final String tweet) {
        final String textLowerCase = tweet.toLowerCase();

        for (final String hardAcceptedRegEx : acceptedRegExes) {
            if (textLowerCase.matches(hardAcceptedRegEx)) {
                // was error - is OK now - moving down - move back up when something is added into the accept list
                logger.info("(" + tag + ") - Hard Accept by regular expression (maybe)=  " + hardAcceptedRegEx + "; text= \n" + tweet);
                return false;
            }
        }

        for (final String bannedRegExMaybe : bannedRegExesMaybe) {
            if (textLowerCase.matches(bannedRegExMaybe)) {
                ErrorUtil.registerError(bannedRegExesMaybeErrors, bannedRegExMaybe, tweet);
                return true;
            }
        }

        for (final String bannedRegEx : bannedRegExes) {
            if (textLowerCase.matches(bannedRegEx)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isRejectedByContainsKeywordMaybe(final List<String> tweetTokens, final String tweet) {
        final String textLowerCase = tweet.toLowerCase();

        for (final String tweetToken : tweetTokens) {
            if (bannedContainsKeywordsMaybe.contains(tweetToken.toLowerCase())) {
                // first - check if there are any overrides
                if (overrideFoundForContainsKeywords(textLowerCase, acceptedContainsKeywordsOverrides)) {
                    continue;
                }

                // try catch to at least get the stack
                try {
                    throw new IllegalStateException("I need the full stack - maybe keywords rejection");
                } catch (final Exception ex) {
                    logger.debug("2 - Rejecting the following tweet because a token matches one of the banned maybe keywords (go to debug for the whole stack): token= " + tweetToken + "; tweet= \n" + tweet, ex);
                    ErrorUtil.registerError(bannedContainsMaybeErrors, tweetToken, tweet);
                }
                return true;
            }
        }

        return false;
    }

    // UTILS

    private boolean overrideFoundForContainsKeywords(final String originalTweet, final Iterable<String> overrides) {
        for (final String override : overrides) {
            if (originalTweet.toLowerCase().contains(override)) {
                // was error - confirmed OK - moving down
                logger.debug("Found override= " + override + "; in tweet= \n" + originalTweet);
                return true;
            }
        }

        return false;
    }

}
