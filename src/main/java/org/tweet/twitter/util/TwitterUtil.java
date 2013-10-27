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
            // 
        );// @formatter:on
        final static List<String> bannedContainsKeywordsMaybe = Lists.newArrayList(// @formatter:off
            "dance"// it's OK for analysis, but not for tweeting - leaving it in the maybe pile for a bit, then move up (06.10)
            ,"trial" // it's OKish for analysis (06.10)
            ,"win" // definitely rejecting for tweeting just in case (13.10)
            ,"israel", "israeli", "palestine", "zionism"
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
            "buy", "discount", 
            "freelance", "job", "consulting", "hire", "hiring", "careers", 
            "football", 
            // "exclusive", // no hits yet, but it does create some false positives in my manual tests - commenting out for now
            // "dumb", "dumber", // were on maybe - didn't really find to many wrong tweets - for now - they're OK (06.10)
            "gift", "highheels",
            "djmix", "housemusic",
            "escort", "escorts", "xxx", "porn", "fuck", "boobs", "breastfeeding", 
            "islamic", "islam", "muslim", "muslims", "pakistan", "egypt", "syria", "jewish", "jew",
            "snake", // python snake...yes, it happened
            "followback", 
            "free trial" // identified from the trial keyword - all selling something
        );// @formatter:on
        final static List<String> bannedContainsKeywordsMaybe = Lists.newArrayList(// @formatter:off
            // "buy", // was here, I'm sufficiently convinced that it's not good 
            "#deal", "#deals" // new - including this with the hashcode here - all of them should be validly rejected - if they are - move to the bannedContainsKeywords
            ,"wife"
            ,"killed"
            ,"remix"  
            ,"cheep" // trying it out
            ,"lucky" 
            ,"fpl" // fantasy player league
            // ,"deals" // working on it
            // ,"deal" // working on it
            ,"priced" // new
            ,"promo" 
            ,"kurd", "kurds", "afganistan", "palestinians" // other political stuff
            ,"hindus" // new (13.10)
            ,"$3.99", "$2.99", "$1.99", "$0.99" 
            ,"thugs" // new
            ,"racial"
        );// @formatter:on

        /**
         * These are special cases that are OK <br/>
         * - <b>ex</b>: `killed it` is a special case for `killed` that is OK
         */
        final static List<String> acceptedContainsKeywordsOverrides = Lists.newArrayList(// @formatter:off
            "killed it", 
            "win-win", "win/win"
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
            ,"for sale" 
            ,"rt if" 
            ,"win a " 
            // ,"to win" // now that there are regexes covering this - this should go 
            ,"win one" 
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
             ".*\\bdeal with.*"
            ,".*deal.*merger.*", ".*merger.*deal.*"
        ); // @formatter:on

        final static List<String> bannedRegExes = Lists.newArrayList(// @formatter:off
            "Get (.)* on Amazon.*" // Get 42% off Secrets of the #JavaScript Ninja on Amazon http://amzn.to/12kkaUn @jeresig
            ,"I'm broadcasting .* on .*" // I'm broadcasting #LIVE on #HangWith for #iPhone! Come Hang w/souljaboy! http://bit.ly/hangwsocial
            ,"Follow us on (Linkedin|Twitter|G+) .*" // Follow us on Linkedin - http://linkd.in/V4Fxa5  #Android #iOS #PS3 #Xbox360 #Apps #GameDev #IDRTG #Video #Game #Developer
            ,".*R(T|t)[ .!@\\-].*R(T|t)([ .!@\\-]|\\Z).*" // 2 RTs
            ,".*(?i)FREE[ .!@\\-].*R(T|t)([ .!@\\-]|\\Z).*" // Free ... RT
            ,".*(f|F)ollow (&|and|AND) R(T|t).*" // Follow & RT
            ,".*R(T|t) .* (f|F)ollow(ed)? .*" // RT this if you want me to follow you
            ,".*\\d(\\d)?% (o|O)ff.*" // 97% Off
            ,"(?i).*follow @.*"
            // win - commercial stuff
            ,".*win.*£.*", ".*£.*win.*"
            , ".*win.*contest.*", ".*contest.*win.*"
            ,".*win.*giving away.*", ".*giving away.*win.*"
            ,".*deal(s)?\\b.*today.*", ".*\\btoday\\b.*deal(s)?\\b.*" // +1 +1 +1 +1 +1 +1 +1 +1 +1 +1 +1
        ); // @formatter:on

        final static List<String> bannedRegExesMaybe = Lists.newArrayList(// @formatter:off
            // win
            ".*win.*\\$.*", ".*\\$.*win.*" // +1 +1 
            // counterexample:  $(document).ready vs. $(window).load « 4 Lines of Code http://t.co/cEd6Huyh #soudev #soufront
            // counter, counter example: DOWNLOAD MY SINGLE FOR ONLY $0.50 ♫  Dboy Swagg -  Various Artists. Listen @cdbaby http://t.co/7JfpQOqJrO @nwdragonwing @Pro2colRecords
            ,".*win.*€.*", ".*€.*win.*"
            ,".*win.*chance.*", ".*chance.*win.*" // +1 +1
            ,".*win.*prize.*", ".*prize.*win.*" 
            ,".*win.*sale.*", ".*sale.*win.*"
            ,".*win.*swag.*", ".*swag.*win.*" 
            ,".*win.*giveaway.*", ".*giveaway.*win.*" // +1 +1
            ,".*win.*give-away.*", ".*give-away.*win.*"
            ,".*win.*promo.*", ".*promo.*win.*"
            ,".*win.*ticket.*", ".*ticket.*win.*"
            ,".*win.*check.*", ".*check.*win.*"
            ,".*win.*vote.*", ".*vote.*win.*"
            ,".*win.*submit.*", ".*submit.*win.*"
            ,".*win\\b.*some.*" // 
            ,".*you could.*win.*"
            
            //deal
            ,".*deal.*of the day.*" // +1  
            ,".*deal.*\\% off.*", ".*\\% off.*deal.*"
            ,".*deal.*free\\b.*", ".*free\\b.*deal.*"
            // John Bolton knocks Iran nuclear deal as ‘pure propaganda’ http://t.co/QGJDOyC1jA #iran #freethe7
            
            ,".*deal.*sale\\b.*", ".*sale\\b.*deal.*"
            ,".*deal.*special.*", ".*special.*deal.*" // +1
            ,".*deal.*discount.*", ".*discount.*deal.*"
            ,".*deal.*check.*", ".*check.*deal.*"
            ,".*deal.*bundle.*", ".*bundle.*deal.*"
            ,".*deal.*price\\b.*", ".*price.*deal.*"
            ,".*deal.*code.*", ".*code.*deal.*"
            
            ,".*deal.*best.*", ".*best.*deal.*"
            ,".*deal.*daily.*", ".*daily.*deal.*"
            ,".*deal.*only.*", ".*only.*deal.*"
            ,".*deal.*shopping.*", ".*shopping.*deal.*"
            ,".*deal.*€.*", ".*€.*deal.*"
            // ,".*deal.*£.*", ".*£.*deal.*" // to many false positives - ignoring for now
            // ,".*deal.*\\$.*", ".*\\$.*deal.*" // to many false positives - ignoring for now
            
            // other commercial
            ,".*only\\b.*$.*"
        ); // @formatter:on

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

        for (final String bannedExpressionMaybe : ForAnalysis.bannedExpressionsMaybe) {
            if (textLowerCase.contains(bannedExpressionMaybe)) {
                logger.error("1 - Rejecting the following tweet because a token matches the maybe banned expression={}; tweet=\n{}", bannedExpressionMaybe, textLowerCase);
                return true;
            }
        }

        for (final String bannedExpression : ForAnalysis.bannedExpressions) {
            if (textLowerCase.contains(bannedExpression)) {
                logger.debug("Rejecting the following tweet because a token matches the banned expression={}; tweet=\n{}", bannedExpression, textLowerCase);
                return true;
            }
        }

        // by contains keyword - maybe

        final List<String> tweetTokens = Lists.newArrayList(Splitter.on(CharMatcher.anyOf(ClassificationSettings.TWEET_TOKENIZER + "#")).split(textLowerCase));
        if (isRejectedByContainsKeywordMaybeForAnalysis(tweetTokens, textLowerCase)) {
            return true;
        }

        // by contains keyword
        for (final String tweetToken : tweetTokens) {
            if (ForAnalysis.bannedContainsKeywords.contains(tweetToken.toLowerCase())) {
                logger.debug("Rejecting the following tweet because a token matches one of the banned keywords: token= {}; tweet= \n{}", tweetToken, textLowerCase);
                return true;
            }
        }

        // by starts with keyword
        for (final String bannedStartsWith : ForAnalysis.bannedStartsWithExprs) {
            if (textLowerCase.startsWith(bannedStartsWith)) {
                logger.debug("Rejecting the following tweet because it starts with= {}; tweet= \n{}", bannedStartsWith, textLowerCase);
                return true;
            }
        }

        // by regex
        if (isRejectedByBannedRegexExpressionsForAnalysis(textLowerCase)) {
            return true;
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
        for (final String tweetToken : tweetTokens) {
            if (ForAnalysis.bannedContainsKeywordsMaybe.contains(tweetToken.toLowerCase())) {
                // first - check if there are any overrides
                if (overrideFoundForContainsKeywordsForAnalysis(originalTweet)) {
                    continue;
                }

                // try catch to at least get the stack
                try {
                    throw new IllegalStateException("I need the full stack - maybe keywords rejection");
                } catch (final Exception ex) {
                    logger.error("2 - Rejecting the following tweet because a token matches one of the banned maybe keywords: token= " + tweetToken + "; tweet= \n" + originalTweet);
                    logger.debug("Rejecting the following tweet because a token matches one of the banned maybe keywords (go to debug for the whole stack): token= " + tweetToken + "; tweet= \n" + originalTweet, ex);
                }
                return true;
            }
        }

        return false;
    }

    /**
     * - <b>local</b> <br/>
     */
    static boolean isRejectedByBannedRegexExpressionsForAnalysis(final String text) {
        for (final String hardAcceptedRegEx : ForAnalysis.acceptedRegExes) {
            if (text.matches(hardAcceptedRegEx)) {
                // was error - is OK now - moving down - move back up when something is added into the accept list
                logger.info("(for analysis) - Hard Accept by regular expression (maybe)=  " + hardAcceptedRegEx + "; text= \n" + text);
                return false;
            }
        }
        for (final String bannedRegExMaybe : ForAnalysis.bannedRegExesMaybe) {
            if (text.matches(bannedRegExMaybe)) {
                logger.error("(for analysis) - Rejecting by regular expression (maybe)=  " + bannedRegExMaybe + "; text= \n" + text);
                return true;
            }
        }
        for (final String bannedRegEx : ForAnalysis.bannedRegExes) {
            if (text.matches(bannedRegEx)) {
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
                    logger.error("2 - Rejecting the following tweet because a token matches one of the banned maybe keywords: token= " + tweetToken + "; tweet= \n" + originalTweet);
                    logger.debug("Rejecting the following tweet because a token matches one of the banned maybe keywords (go to debug for the whole stack): token= " + tweetToken + "; tweet= \n" + originalTweet, ex);
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
        return overrideFoundForContainsKeywords(originalTweet, ForAnalysis.acceptedContainsKeywordsOverrides);
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
