package org.tweet.twitter.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.classification.util.ClassificationSettings;
import org.common.service.LinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public final class TwitterUtil {
    final static Logger logger = LoggerFactory.getLogger(TwitterUtil.class);

    public final static Splitter splitter = Splitter.on(' ').omitEmptyStrings().trimResults(); // if this would include more chars, then recreating the tweet would not be exact
    public final static Joiner joiner = Joiner.on(' ');

    final static List<String> bannedContainsKeywords = Lists.newArrayList(// @formatter:off
        "buy", 
        "freelance", "job", "consulting", "hire", "hiring", "careers", 
        // "need", // in the maybe pile for now 
        "football", "exclusive",
        "dumb", 
        "escort", "escorts", "xxx", "porn", "fuck"
    );// @formatter:on
    final static List<String> bannedContainsKeywordsMaybe = Lists.newArrayList(// @formatter:off
        "buy", "gift",
        "need", 
        "dumb", 
        "snake", // python snake...yes, it happened
        "islamic", "islam", 
        "$3.99", "$2.99", "$1.99", "$0.99" 
    );// @formatter:on
    final static List<String> bannedStartsWithExprs = Lists.newArrayList(// @formatter:off
        "photo: "
    );// @formatter:on
    final static List<String> bannedExpressions = Lists.newArrayList(// @formatter:off
        "web developer", "web developers", 
        "application engineer", "application engineers", 
        "python developer", "java developer", "php developer", "clojure developer", "c# developer", "c++ developer", 
        "backend developer", "back end developer", "frontend developer", "front end developer", "fullstack developer", "full stack developer", 
        "on strike", 
        "for sale", 
        "i need", "we need", "need help", "need someone"
    ); // @formatter:on
    final static List<String> bannedRegExes = Lists.newArrayList(// @formatter:off
        "Get (.)* on Amazon.*", // Get 42% off Secrets of the #JavaScript Ninja on Amazon http://amzn.to/12kkaUn @jeresig
        "I'm broadcasting .* on .*",  // I'm broadcasting #LIVE on #HangWith for #iPhone! Come Hang w/souljaboy! http://bit.ly/hangwsocial
        "Follow us on (Linkedin|Twitter|G+) .*", // Follow us on Linkedin - http://linkd.in/V4Fxa5  #Android #iOS #PS3 #Xbox360 #Apps #GameDev #IDRTG #Video #Game #Developer
        ".*RT[ .!@\\-].*RT([ .!@\\-]|\\Z).*", // 2 RTs
        ".*(?i)FREE[ .!@\\-].*RT([ .!@\\-]|\\Z).*" // Free ... RT
    ); // @formatter:on

    final static List<String> bannedTwitterUsers = Lists.newArrayList(// @formatter:off
        "blogginginside", // in German - https://twitter.com/blogginginside
        "ulohjobs",  // jobs
        "heyyouapp" // crap
    ); // @formatter:on

    private TwitterUtil() {
        throw new AssertionError();
    }

    // API

    /**
     * Verifies that: <br/>
     * - the text has <b>no link</b> <br/>
     * - the text has the <b>correct length</b> <br/>
     */
    public static boolean isTweetTextWithoutLinkValid(final String text) {
        final int linkNoInTweet = new LinkService().extractUrls(text).size();
        if (linkNoInTweet > 1) {
            return false;
        }

        return text.length() <= 122;
    }

    /**
     * Verifies that: <br/>
     * - the text has the <b>correct length</b> <br/>
     * - <b>note</b>: there is something about shortened urls - 20 to 18 characters - experimenting with 142 (from 140)
     */
    public static boolean isTweetTextWithLinkValid(final String fullTweet) {
        return fullTweet.length() <= 142;
    }

    public static boolean isUserBannedFromRetweeting(final String username) {
        return bannedTwitterUsers.contains(username);
    }

    // retweet logic

    public static String extractTweetFromRt(final String fullTweet) {
        // \A - anchor - matches before start of text block
        final String resultWhenRtIsStart = fullTweet.replaceAll("\\ART @[a-zA-Z0-9_]+ ?[:-] ?", "");
        if (!resultWhenRtIsStart.equals(fullTweet)) {
            return resultWhenRtIsStart;
        }

        final String resultWhenRtIsEnd = fullTweet.replaceAll(" ?[:-]? ?RT @[a-zA-Z0-9_]+ ?\\Z", "");
        if (!resultWhenRtIsEnd.equals(fullTweet)) {
            return resultWhenRtIsEnd;
        }

        return resultWhenRtIsStart;
    }

    /**
     * - note: only for a very special case of tweet - the retweet mention (which I am not sure really happens without the added `RT @`)
     */
    public static String extractOriginalUserFromRt(final String textOfRetweet) {
        final Pattern pattern = Pattern.compile("@[a-zA-Z0-9_]*");
        final Matcher matcher = pattern.matcher(textOfRetweet);
        if (matcher.find()) {
            final String user = matcher.group(0);
            return user.substring(1);
        }

        return null;
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

    /**
     * - <b>local</b> <br/>
    */
    public static boolean isTweetBanned(final String text) {
        // final List<String> tweetTokens = Lists.newArrayList(Splitter.on(CharMatcher.anyOf(" ,?!:#.")).split(text)); // on 07.08 - see what happens
        final List<String> tweetTokens = Lists.newArrayList(Splitter.on(CharMatcher.anyOf(ClassificationSettings.TWEET_TOKENIZER + "#")).split(text));

        // by expression
        final String textLowerCase = text.toLowerCase();
        for (final String bannedExpression : bannedExpressions) {
            if (textLowerCase.contains(bannedExpression)) {
                logger.debug("Rejecting the following tweet because a token matches the banned expression={}; tweet=\n{}", bannedExpression, text);
                return true;
            }
        }

        // by contains keyword - maybe
        for (final String tweetToken : tweetTokens) {
            if (TwitterUtil.bannedContainsKeywordsMaybe.contains(tweetToken.toLowerCase())) {
                // TODO: temporarily error to get some examples
                // also moved up so that it runs first and I see what kind of tweets would belong to this category
                logger.error("Rejecting the following tweet because a token matches one of the banned maybe keywords: token= {}; tweet= \n{}", tweetToken, text);
                return true;
            }
        }

        // by contains keyword
        for (final String tweetToken : tweetTokens) {
            if (TwitterUtil.bannedContainsKeywords.contains(tweetToken.toLowerCase())) {
                logger.debug("Rejecting the following tweet because a token matches one of the banned keywords: token= {}; tweet= \n{}", tweetToken, text);
                return true;
            }
        }

        // by starts with keyword
        for (final String bannedStartsWith : bannedStartsWithExprs) {
            if (text.startsWith(bannedStartsWith)) {
                logger.debug("Rejecting the following tweet because it starts with= {}; tweet= \n{}", bannedStartsWith, text);
                return true;
            }
        }

        // by regex
        if (isRejectedByBannedRegexExpressions(text)) {
            return true;
        }

        return false;
    }

    /**
     * - <b>local</b> <br/>
     */
    static boolean isRejectedByBannedRegexExpressions(final String text) {
        for (final String bannedRegEx : bannedRegExes) {
            if (text.matches(bannedRegEx)) {
                return true;
            }
        }

        return false;
    }

    // tweet - break

    /**
     * - returns null if there the text doesn't contain a single link
     */
    public static Pair<String, String> breakByUrl(final String originalTweet) {
        final List<String> extractedUrls = new LinkService().extractUrls(originalTweet);
        if (extractedUrls.size() != 1) {
            return null;
        }
        final String mainUrl = extractedUrls.get(0);
        final int indexOfMainUrl = originalTweet.indexOf(mainUrl);
        final String before = originalTweet.substring(0, indexOfMainUrl);
        final String after = originalTweet.substring(indexOfMainUrl + mainUrl.length());
        return new ImmutablePair<String, String>(before, after);
    }

    public static String extractLargerPart(final String originalTweet) {
        final Pair<String, String> beforeAndAfter = breakByUrl(originalTweet);
        if (beforeAndAfter == null) {
            return originalTweet;
        }
        if (beforeAndAfter.getLeft().length() > beforeAndAfter.getRight().length()) {
            return beforeAndAfter.getLeft();
        } else {
            return beforeAndAfter.getRight();
        }
    }

}
