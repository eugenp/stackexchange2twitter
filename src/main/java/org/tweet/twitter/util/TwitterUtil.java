package org.tweet.twitter.util;

import java.util.List;
import java.util.Set;

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

    public static final class ForTweeting {

        // by contains

        final static List<String> bannedContainsKeywords = Lists.newArrayList(// @formatter:off
            "wife" // it's OK to skip these
        );// @formatter:on
        final static List<String> bannedContainsKeywordsMaybe = Lists.newArrayList(// @formatter:off
            "dance"// it's OK for analysis, but not for tweeting - leaving it in the maybe pile for a bit, then move up (06.10)
            ,"trial" // it's OKish for analysis (06.10)
            ,"win" // definitely rejecting for tweeting just in case (13.10)
            ,"israel", "israeli", "palestine", "zionism"
            ,"sudan"
            ,"racial"
            ,"voucher"
            ,"meetup"
            ,"sponsored"
        );// @formatter:on

        final static List<String> acceptedContainsKeywordsOverrides = Lists.newArrayList(// @formatter:off
            //
        );// @formatter:on

        // by starts with

        final static List<String> bannedStartsWithExprs = Lists.newArrayList(// @formatter:off
            // 
        );// @formatter:on

        // by expression

        final static List<String> bannedExpressions = Lists.newArrayList(// @formatter:off
            // 
        ); // @formatter:on

        final static List<String> bannedExpressionsMaybe = Lists.newArrayList(// @formatter:off
            // 
        ); // @formatter:on

        // by regex

        final static List<String> bannedRegExesMaybe = Lists.newArrayList(// @formatter:off
            // 
        ); // @formatter:on
        final static List<String> bannedRegExes = Lists.newArrayList(// @formatter:off
            //
        ); // @formatter:on

    }

    public static final class ForAnalysis {

        // by contains

        final static List<String> bannedContainsKeywords = Lists.newArrayList(// @formatter:off
             "freelance", "job", "consulting", "hire", "hiring", "careers" 
            ,"football" 
            // ,"exclusive" // no hits yet, but it does create some false positives in my manual tests - commenting out for now
            // ,"dumb", "dumber" // were on maybe - didn't really find to many wrong tweets - for now - they're OK (06.10)
            ,"highheels"
            ,"djmix", "housemusic"
            ,"escort", "escorts", "xxx", "porn", "fuck", "boobs", "breastfeeding" 
            ,"killed"
            ,"islamic", "islam", "muslim", "muslims", "pakistan", "egypt", "syria", "jewish", "jew"
            ,"snake" // python snake...yes, it happened
            ,"followback"
            ,"remix"
        );// @formatter:on
        final static List<String> bannedContainsKeywordsMaybe = Lists.newArrayList(// @formatter:off
             "fpl" // fantasy player league
            ,"kurd", "kurds", "afganistan", "palestinians" // other political stuff
            ,"hindus" // new (13.10)
            ,"thugs" // new
            ,"racial"
        );// @formatter:on

        /**
         * These are special cases that are OK <br/>
         * - <b>ex</b>: `killed it` is a special case for `killed` that is OK
         */
        final static List<String> acceptedContainsKeywordsOverrides = Lists.newArrayList(// @formatter:off
            "killed it"
        );// @formatter:on

        // by starts with

        final static List<String> bannedStartsWithExprs = Lists.newArrayList(// @formatter:off
            "photo: "
        );// @formatter:on

        // by expression

        final static List<String> bannedExpressions = Lists.newArrayList(// @formatter:off
            "web developer", "web developers"
            ,"application engineer", "application engineers" 
            ,"python developer", "java developer", "php developer", "clojure developer", "c# developer", "c++ developer" 
            ,"backend developer", "back end developer", "frontend developer", "front end developer", "fullstack developer", "full stack developer" 
            ,"on strike" 
            ,"rt if" 
            ,"i need", "we need", "need help", "need someone" 
            ,"music video"
        ); // @formatter:on

        final static List<String> bannedExpressionsMaybe = Lists.newArrayList(// @formatter:off
            // 
        ); // @formatter:on

        // by regex

        // note: move the logging of this back up to error if something new is added
        /** if this matches, the banned expressions are no longer evaluated */
        final static List<String> acceptedRegExes = Lists.newArrayList(// @formatter:off
             // 
        ); // @formatter:on

        final static List<String> bannedRegExes = Lists.newArrayList(// @formatter:off
            "Get .* on Amazon.*" // Get 42% off Secrets of the #JavaScript Ninja on Amazon http://amzn.to/12kkaUn @jeresig
            ,"I'm broadcasting .* on .*" // I'm broadcasting #LIVE on #HangWith for #iPhone! Come Hang w/souljaboy! http://bit.ly/hangwsocial
            ,"Follow us on (Linkedin|Twitter|G+) .*" // Follow us on Linkedin - http://linkd.in/V4Fxa5  #Android #iOS #PS3 #Xbox360 #Apps #GameDev #IDRTG #Video #Game #Developer
            ,".*R(T|t)[ .!@\\-].*R(T|t)([ .!@\\-]|\\Z).*" // 2 RTs
            ,".*(?i)FREE[ .!@\\-].*R(T|t)([ .!@\\-]|\\Z).*" // Free ... RT
            ,".*(f|F)ollow (&|and|AND) R(T|t).*" // Follow & RT
            ,".*(R|r)(T|t) .* (f|F)ollow(ed)? .*" // RT this if you want me to follow you
            ,"(?i)(pls|please) rt.*" // pls rt
            ,".*\\d(\\d)?% (o|O)ff.*" // 97% Off
            ,"(?i).*follow @.*"
        ); // @formatter:on

        final static List<String> bannedRegExesMaybe = Lists.newArrayList(// @formatter:off
            // other commercial
            ".*only\\b.*\\$.*" // +1 +1 +1 +1 -1 -1
        ); // @formatter:on

        public static final class Commercial {

            // by contains

            final static List<String> bannedContainsKeywords = Lists.newArrayList(// @formatter:off
                "buy" 
                ,"discount" // newly reactivated (10.11) - I don't think there are to many false positive for this word 
                // ,"gift" // more fine grained stuff in use now
                ,"promo" // newly moved (02.11)
                ,"free trial" // identified from the trial keyword - all selling something
                ,"giveaway" // verified - moved (11.11)
                ,"lucky" // moved - it's OK to skip these
            );// @formatter:on
            final static List<String> bannedContainsKeywordsMaybe = Lists.newArrayList(// @formatter:off
                // "buy", // was here, I'm sufficiently convinced that it's not good 
                // "#deal", "#deals" // makes no difference with my testing data - so commenting them out
                 "cheep" // trying it out
                ,"priced" // new
                ,"$3.99", "$2.99", "$1.99", "$0.99" 
            );// @formatter:on

            /**
             * These are special cases that are OK <br/>
             * - <b>ex</b>: `killed it` is a special case for `killed` that is OK
             */
            final static List<String> acceptedContainsKeywordsOverrides = Lists.newArrayList(// @formatter:off
                "win-win", "win/win"
            );// @formatter:on

            // by starts with

            final static List<String> bannedStartsWithExprs = Lists.newArrayList(// @formatter:off
                "#win"
            );// @formatter:on

            // by expression

            final static List<String> bannedExpressions = Lists.newArrayList(// @formatter:off
                // 
            ); // @formatter:on

            final static List<String> bannedExpressionsMaybe = Lists.newArrayList(// @formatter:off
                // 
            ); // @formatter:on

            // by regex

            // note: move the logging of this back up to error if something new is added
            /** if this matches, the banned expressions are no longer evaluated */
            final static List<String> acceptedRegExes = Lists.newArrayList(// @formatter:off
                 ".*\\bdeal\\b with.*"
                ,".*deal.*merger.*", ".*merger.*deal.*"
                ,".*win.*merger.*", ".*merger.*win.*"
            ); // @formatter:on

            final static List<String> bannedRegExes = Lists.newArrayList(// @formatter:off
                // win - commercial stuff
                
                // deal - commercial stuff
                 ".*\\% \\boff\\b.*\\bdeal.*"
                
                // other: 
                
            ); // @formatter:on
            static {
                // win
                bannedRegExes.add(RejectExpressionUtil.rejectWinStart("chance"));

                bannedRegExes.add(RejectExpressionUtil.rejectWinEnd("ticket"));
                bannedRegExes.add(RejectExpressionUtil.rejectWinEnd("check"));
                bannedRegExes.add(RejectExpressionUtil.rejectWinEnd("free"));
                bannedRegExes.add(RejectExpressionUtil.rejectWinEnd("prize"));

                bannedRegExes.addAll(RejectExpressionUtil.rejectWin("£"));
                bannedRegExes.addAll(RejectExpressionUtil.rejectWin("contest"));
                bannedRegExes.addAll(RejectExpressionUtil.rejectWin("giving away"));

                bannedRegExes.addAll(RejectExpressionUtil.rejectWin("follow")); // new
                bannedRegExes.addAll(RejectExpressionUtil.rejectWin("enter to")); // new
                bannedRegExes.addAll(RejectExpressionUtil.rejectWin("entered")); // new
                bannedRegExes.addAll(RejectExpressionUtil.rejectStrictWin("enter")); // new
                bannedRegExes.addAll(RejectExpressionUtil.rejectStrictWin("click")); // new
                bannedRegExes.addAll(RejectExpressionUtil.rejectStrictWin("rt")); // new

                // deal
                bannedRegExes.add(RejectExpressionUtil.rejectDealStart("free"));
                bannedRegExes.add(RejectExpressionUtil.rejectDealStart("price"));
                bannedRegExes.add(RejectExpressionUtil.rejectDealStart("shopping"));
                bannedRegExes.add(RejectExpressionUtil.rejectDealStart("ebay"));
                bannedRegExes.add(RejectExpressionUtil.rejectDealStart("best"));
                bannedRegExes.add(RejectExpressionUtil.rejectDealStart("£"));

                bannedRegExes.add(RejectExpressionUtil.rejectDealEnd("of the day"));
                bannedRegExes.add(RejectExpressionUtil.rejectDealEnd("discount"));

                bannedRegExes.addAll(RejectExpressionUtil.rejectDeal("sale"));
                bannedRegExes.addAll(RejectExpressionUtil.rejectDeal("amazon"));
                bannedRegExes.addAll(RejectExpressionUtil.rejectDeal("\\$"));
                bannedRegExes.addAll(RejectExpressionUtil.rejectDeal("today"));
                bannedRegExes.addAll(RejectExpressionUtil.rejectDeal("daily"));
            }
            final static List<String> bannedRegExesMaybe = Lists.newArrayList(// @formatter:off
                // win
                 ".*\\bprize.*\\bwin\\.*" 

                //deal
                ,".*\\bdeal.*\\% off.*"
            ); // @formatter:on
            static {
                // win
                bannedRegExesMaybe.add(RejectExpressionUtil.rejectWinStart("you could"));
                bannedRegExesMaybe.add(RejectExpressionUtil.rejectWinStart("ticket"));
                bannedRegExesMaybe.add(RejectExpressionUtil.rejectWinStart("check"));
                bannedRegExesMaybe.add(RejectExpressionUtil.rejectWinStart("free"));

                bannedRegExesMaybe.add(RejectExpressionUtil.rejectWinEnd("some"));
                bannedRegExesMaybe.add(RejectExpressionUtil.rejectWinEnd("chance"));

                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("\\$"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("€"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("discount"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("copy"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("gift"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("vote"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("voucher"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("submit"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("sale"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("sale"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("swag"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("giveaway"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("give-away"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("promo"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("coupon"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("bundle"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("cash"));

                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("supply")); // new
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("want to")); // new
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectWin("competition")); // new
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectStrictWin("tix")); // new
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectStrictWin("from @")); // new

                // deal
                bannedRegExesMaybe.add(RejectExpressionUtil.rejectDealStart("discount"));

                bannedRegExesMaybe.add(RejectExpressionUtil.rejectDealEnd("free"));
                bannedRegExesMaybe.add(RejectExpressionUtil.rejectDealEnd("price"));
                bannedRegExesMaybe.add(RejectExpressionUtil.rejectDealEnd("best"));
                bannedRegExesMaybe.add(RejectExpressionUtil.rejectDealEnd("shopping"));
                bannedRegExesMaybe.add(RejectExpressionUtil.rejectDealEnd("ebay"));
                bannedRegExesMaybe.add(RejectExpressionUtil.rejectDealEnd("£"));

                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectDeal("special"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectDeal("check"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectDeal("gift"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectDeal("code"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectDeal("only"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectDeal("bundle"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectDeal("coupon"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectDeal("voucher"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectDeal("buy"));
                bannedRegExesMaybe.addAll(RejectExpressionUtil.rejectDeal("€"));
            }
        }

    }

    final static List<String> bannedTwitterUsers = Lists.newArrayList(// @formatter:off
        "blogginginside", // in German - https://twitter.com/blogginginside
        "ulohjobs",  // jobs
        "heyyouapp"// crap
        // , 
        // "BigDataExpo", "CloudExpo" //temporary
    ); // @formatter:on

    private TwitterUtil() {
        throw new AssertionError();
    }

    // API - checks

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

    /**
     * - <b>local</b> <br/>
     */
    public static boolean isUserBannedFromRetweeting(final String username) {
        return bannedTwitterUsers.contains(username);
    }

    /**
     * - <b>local</b> <br/>
     * Tweet can be banned FOR ANALYSIS ONLY by: <br/>
     * - expression (multiple words) <br/>
     * - single word - contains <br/>
     * - single word - starts with <br/>
     * - regular expression - matches <br/>
    */
    public static boolean isTweetBannedForAnalysis(final String originalTweet) {
        // by expression
        final String textLowerCase = originalTweet.toLowerCase();

        if (isTweetBannedForCommercialAnalysis(originalTweet)) {
            return true;
        }

        for (final String bannedExpressionMaybe : ForAnalysis.bannedExpressionsMaybe) {
            if (textLowerCase.contains(bannedExpressionMaybe)) {
                logger.error("1 - Rejecting the following tweet because a token matches the maybe banned expression={}; tweet=\n{}", bannedExpressionMaybe, textLowerCase);
                return true;
            }
        }

        for (final String bannedExpression : ForAnalysis.bannedExpressions) {
            if (textLowerCase.contains(bannedExpression)) {
                logger.debug("Rejecting the following tweet because a token matches the banned expression={}; tweet=\n{}", bannedExpression, originalTweet);
                return true;
            }
        }

        // by contains keyword - maybe

        final List<String> tweetTokens = Lists.newArrayList(Splitter.on(CharMatcher.anyOf(ClassificationSettings.TWEET_TOKENIZER + "#")).split(textLowerCase));
        if (isRejectedByContainsKeywordMaybeForAnalysis(tweetTokens, originalTweet)) {
            return true;
        }

        // by contains keyword
        for (final String tweetToken : tweetTokens) {
            if (ForAnalysis.bannedContainsKeywords.contains(tweetToken.toLowerCase())) {
                logger.debug("Rejecting the following tweet because a token matches one of the banned keywords: token= {}; tweet= \n{}", tweetToken, originalTweet);
                return true;
            }
        }

        // by starts with keyword
        for (final String bannedStartsWith : ForAnalysis.bannedStartsWithExprs) {
            if (textLowerCase.startsWith(bannedStartsWith)) {
                logger.debug("Rejecting the following tweet because it starts with= {}; tweet= \n{}", bannedStartsWith, originalTweet);
                return true;
            }
        }

        // by regex
        if (isRejectedByBannedRegexExpressionsForAnalysis(originalTweet)) {
            return true;
        }

        return false;
    }

    public static boolean isTweetBannedForCommercialAnalysis(final String originalTweet) {
        // by expression
        final String textLowerCase = originalTweet.toLowerCase();

        for (final String bannedExpressionMaybe : ForAnalysis.Commercial.bannedExpressionsMaybe) {
            if (textLowerCase.contains(bannedExpressionMaybe)) {
                logger.error("1 - Rejecting the following tweet because a token matches the maybe banned expression={}; tweet=\n{}", bannedExpressionMaybe, textLowerCase);
                return true;
            }
        }

        for (final String bannedExpression : ForAnalysis.Commercial.bannedExpressions) {
            if (textLowerCase.contains(bannedExpression)) {
                logger.debug("Rejecting the following tweet because a token matches the banned expression={}; tweet=\n{}", bannedExpression, originalTweet);
                return true;
            }
        }

        // by regex // moved before the contains logic (11.11)

        if (isRejectedByBannedRegexExpressionsForCommercialAnalysis(originalTweet)) {
            return true;
        }

        // by contains keyword - maybe

        final List<String> tweetTokens = Lists.newArrayList(Splitter.on(CharMatcher.anyOf(ClassificationSettings.TWEET_TOKENIZER + "#")).split(textLowerCase));
        if (isRejectedByContainsKeywordMaybeForCommercialAnalysis(tweetTokens, originalTweet)) {
            return true;
        }

        // by contains keyword

        for (final String tweetToken : tweetTokens) {
            if (ForAnalysis.Commercial.bannedContainsKeywords.contains(tweetToken.toLowerCase())) {
                logger.debug("Rejecting the following tweet because a token matches one of the banned keywords: token= {}; tweet= \n{}", tweetToken, originalTweet);
                return true;
            }
        }

        // by starts with keyword
        for (final String bannedStartsWith : ForAnalysis.Commercial.bannedStartsWithExprs) {
            if (textLowerCase.startsWith(bannedStartsWith)) {
                logger.debug("Rejecting the following tweet because it starts with= {}; tweet= \n{}", bannedStartsWith, originalTweet);
                return true;
            }
        }

        return false;
    }

    /**
     * - <b>local</b> <br/>
     * Tweet can be banned FOR ANALYSIS AND TWEETING by: <br/>
     * - expression (multiple words) <br/>
     * - single word - contains <br/>
     * - single word - starts with <br/>
     * - regular expression - matches <br/>
    */
    public static boolean isTweetBannedForTweeting(final String originalTweet) {
        if (isTweetBannedForAnalysis(originalTweet)) {
            return true;
        }

        // by expression
        final String textLowerCase = originalTweet.toLowerCase();

        for (final String bannedExpressionMaybe : ForTweeting.bannedExpressionsMaybe) {
            if (textLowerCase.contains(bannedExpressionMaybe)) {
                logger.error("1 - Rejecting the following tweet because a token matches the maybe banned expression={}; tweet=\n{}", bannedExpressionMaybe, originalTweet);
                return true;
            }
        }

        for (final String bannedExpression : ForTweeting.bannedExpressions) {
            if (textLowerCase.contains(bannedExpression)) {
                logger.debug("Rejecting the following tweet because a token matches the banned expression={}; tweet=\n{}", bannedExpression, originalTweet);
                return true;
            }
        }

        // by contains keyword - maybe

        final List<String> tweetTokens = Lists.newArrayList(Splitter.on(CharMatcher.anyOf(ClassificationSettings.TWEET_TOKENIZER + "#")).split(originalTweet));
        if (isRejectedByContainsKeywordMaybeForTweeting(tweetTokens, originalTweet)) {
            return true;
        }

        // by contains keyword
        for (final String tweetToken : tweetTokens) {
            if (ForTweeting.bannedContainsKeywords.contains(tweetToken.toLowerCase())) {
                logger.debug("Rejecting the following tweet because a token matches one of the banned keywords: token= {}; tweet= \n{}", tweetToken, originalTweet);
                return true;
            }
        }

        // by starts with keyword
        for (final String bannedStartsWith : ForTweeting.bannedStartsWithExprs) {
            if (originalTweet.startsWith(bannedStartsWith)) {
                logger.debug("Rejecting the following tweet because it starts with= {}; tweet= \n{}", bannedStartsWith, originalTweet);
                return true;
            }
        }

        // by regex
        if (isRejectedByBannedRegexExpressionsForTweeting(originalTweet)) {
            return true;
        }

        return false;
    }

    // API - extract

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

    // utils - for analysis

    static boolean isRejectedByContainsKeywordMaybeForAnalysis(final List<String> tweetTokens, final String originalTweet) {
        final String textLowerCase = originalTweet.toLowerCase();

        for (final String tweetToken : tweetTokens) {
            if (ForAnalysis.bannedContainsKeywordsMaybe.contains(tweetToken.toLowerCase())) {
                // first - check if there are any overrides
                if (overrideFoundForContainsKeywordsForAnalysis(textLowerCase)) {
                    continue;
                }

                // try catch to at least get the stack
                try {
                    throw new IllegalStateException("I need the full stack - maybe keywords rejection");
                } catch (final Exception ex) {
                    logger.debug("2 - Rejecting the following tweet because a token matches one of the banned maybe keywords (go to debug for the whole stack): token= " + tweetToken + "; tweet= \n" + originalTweet, ex);
                    ErrorUtil.registerError(ErrorUtil.bannedContainsMaybeErrors, tweetToken, originalTweet);
                }
                return true;
            }
        }

        return false;
    }

    static boolean isRejectedByContainsKeywordMaybeForCommercialAnalysis(final List<String> tweetTokens, final String originalTweet) {
        final String textLowerCase = originalTweet.toLowerCase();

        for (final String tweetToken : tweetTokens) {
            if (ForAnalysis.Commercial.bannedContainsKeywordsMaybe.contains(tweetToken.toLowerCase())) {
                // first - check if there are any overrides
                if (overrideFoundForContainsKeywordsForCommercialAnalysis(textLowerCase)) {
                    continue;
                }

                // try catch to at least get the stack
                try {
                    throw new IllegalStateException("I need the full stack - maybe keywords rejection");
                } catch (final Exception ex) {
                    logger.debug("21 - Rejecting the following tweet because a token matches one of the banned maybe keywords (go to debug for the whole stack): token= " + tweetToken + "; tweet= \n" + originalTweet, ex);
                    ErrorUtil.registerError(ErrorUtil.bannedCommercialContainsMaybeErrors, tweetToken, originalTweet);
                }
                return true;
            }
        }

        return false;
    }

    /**
     * - <b>local</b> <br/>
     */
    public static boolean isRejectedByBannedRegexExpressionsForAnalysis(final String originalTweet) {
        final String textLowerCase = originalTweet.toLowerCase();

        for (final String hardAcceptedRegEx : ForAnalysis.acceptedRegExes) {
            if (textLowerCase.matches(hardAcceptedRegEx)) {
                // was error - is OK now - moving down - move back up when something is added into the accept list
                logger.info("(analysis-generic) - Hard Accept by regular expression (maybe)=  " + hardAcceptedRegEx + "; text= \n" + originalTweet);
                return false;
            }
        }

        for (final String bannedRegExMaybe : ForAnalysis.bannedRegExesMaybe) {
            if (textLowerCase.matches(bannedRegExMaybe)) {
                // logger.error("(analysis-generic) - Rejecting by regular expression (maybe)=  " + bannedRegExMaybe + "; text= \n" + originalTweet);
                ErrorUtil.registerError(ErrorUtil.bannedRegExesMaybeErrors, bannedRegExMaybe, originalTweet);
                return true;
            }
        }

        for (final String bannedRegEx : ForAnalysis.bannedRegExes) {
            if (textLowerCase.matches(bannedRegEx)) {
                return true;
            }
        }

        return false;
    }

    /**
     * - <b>local</b> <br/>
     */
    public static boolean isRejectedByBannedRegexExpressionsForCommercialAnalysis(final String originalTweet) {
        final String textLowerCase = originalTweet.toLowerCase();

        for (final String hardAcceptedRegEx : ForAnalysis.Commercial.acceptedRegExes) {
            if (textLowerCase.matches(hardAcceptedRegEx)) {
                // was error - is OK now - moving down - move back up when something is added into the accept list
                logger.info("(analysis-commercial) - Hard Accept by regular expression (maybe)=  " + hardAcceptedRegEx + "; text= \n" + originalTweet);
                return false;
            }
        }

        for (final String bannedRegExMaybe : ForAnalysis.Commercial.bannedRegExesMaybe) {
            if (textLowerCase.matches(bannedRegExMaybe)) {
                // logger.error("(analysis-commercial) - Rejecting by regular expression (maybe)=  " + bannedRegExMaybe + "; text= \n" + originalTweet);
                ErrorUtil.registerError(ErrorUtil.bannedRegExesMaybeErrors, bannedRegExMaybe, originalTweet);
                return true;
            }
        }

        for (final String bannedRegEx : ForAnalysis.Commercial.bannedRegExes) {
            if (textLowerCase.matches(bannedRegEx)) {
                return true;
            }
        }

        return false;
    }

    // utils - for tweeting

    static boolean isRejectedByContainsKeywordMaybeForTweeting(final List<String> tweetTokens, final String originalTweet) {
        if (isRejectedByContainsKeywordMaybeForAnalysis(tweetTokens, originalTweet)) {
            return true;
        }

        for (final String tweetToken : tweetTokens) {
            if (ForTweeting.bannedContainsKeywordsMaybe.contains(tweetToken.toLowerCase())) {
                // first - check if there are any overrides
                if (overrideFoundForContainsKeywordsForTweeting(originalTweet)) {
                    continue;
                }

                // try catch to at least get the stack
                try {
                    throw new IllegalStateException("I need the full stack - maybe keywords rejection");
                } catch (final Exception ex) {
                    logger.error("3 - Rejecting the following tweet because a token matches one of the banned maybe keywords: token= " + tweetToken + "; tweet= \n" + originalTweet);
                    logger.debug("3 - Rejecting the following tweet because a token matches one of the banned maybe keywords (go to debug for the whole stack): token= " + tweetToken + "; tweet= \n" + originalTweet, ex);
                }
                return true;
            }
        }

        return false;
    }

    /**
     * - <b>local</b> <br/>
     */
    static boolean isRejectedByBannedRegexExpressionsForTweeting(final String text) {
        if (isRejectedByBannedRegexExpressionsForAnalysis(text)) {
            return true;
        }

        for (final String bannedRegExMaybe : ForTweeting.bannedRegExesMaybe) {
            if (text.matches(bannedRegExMaybe)) {
                logger.error("(for tweeting) - Rejecting by regular expression (maybe)=  " + bannedRegExMaybe + "; text= \n" + text);
                return true;
            }
        }
        for (final String bannedRegEx : ForTweeting.bannedRegExes) {
            if (text.matches(bannedRegEx)) {
                return true;
            }
        }

        return false;
    }

    // utils - other

    static int countWordsToHash(final Iterable<String> tokens, final List<String> lowerCaseWordsToHash) {
        int countWordsToHash = 0;
        for (final String token : tokens) {
            if (lowerCaseWordsToHash.contains(token.toLowerCase())) {
                countWordsToHash++;
            }
        }

        return countWordsToHash;
    }

    private static boolean overrideFoundForContainsKeywordsForAnalysis(final String originalTweet) {
        final boolean overrideFoundForContainsKeywords = overrideFoundForContainsKeywords(originalTweet, ForAnalysis.acceptedContainsKeywordsOverrides);

        return overrideFoundForContainsKeywords;
    }

    private static boolean overrideFoundForContainsKeywordsForCommercialAnalysis(final String originalTweet) {
        final boolean overrideFoundForContainsCommercialKeywords = overrideFoundForContainsKeywords(originalTweet, ForAnalysis.Commercial.acceptedContainsKeywordsOverrides);

        return overrideFoundForContainsCommercialKeywords;
    }

    private static boolean overrideFoundForContainsKeywordsForTweeting(final String originalTweet) {
        return overrideFoundForContainsKeywords(originalTweet, ForTweeting.acceptedContainsKeywordsOverrides);
    }

    private static boolean overrideFoundForContainsKeywords(final String originalTweet, final Iterable<String> overrides) {
        for (final String override : overrides) {
            if (originalTweet.toLowerCase().contains(override)) {
                // was error - confirmed OK - moving down
                logger.debug("Found override= " + override + "; in tweet= \n" + originalTweet);
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
        final Set<String> extractedUrls = new LinkService().extractUrls(originalTweet);
        if (extractedUrls.size() != 1) {
            return null;
        }
        final String mainUrl = extractedUrls.iterator().next();
        final int indexOfMainUrl = originalTweet.indexOf(mainUrl);
        final String before = originalTweet.substring(0, indexOfMainUrl);
        final String after = originalTweet.substring(indexOfMainUrl + mainUrl.length());
        return new ImmutablePair<String, String>(before, after);
    }

}
