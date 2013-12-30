package org.tweet.twitter.util;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.common.service.LinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tweet.twitter.evaluator.ChainingEvaluator;
import org.tweet.twitter.evaluator.impl.ForAnalysisEvaluator;
import org.tweet.twitter.evaluator.impl.ForCommercialAnalysisEvaluator;
import org.tweet.twitter.evaluator.impl.ForNonTechnicalEvaluator;
import org.tweet.twitter.evaluator.impl.ForTweetingEvaluator;
import org.tweet.twitter.service.TweetType;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public final class TwitterUtil {
    final static Logger logger = LoggerFactory.getLogger(TwitterUtil.class);

    public final static Splitter splitter = Splitter.on(' ').omitEmptyStrings().trimResults(); // if this would include more chars, then recreating the tweet would not be exact
    public final static Joiner joiner = Joiner.on(' ');

    public static final class ForTweeting {

        // by contains

        public final static List<String> bannedContainsKeywords = Lists.newArrayList(// @formatter:off
            "wife" // it's OK to skip these
        );// @formatter:on
        public final static List<String> bannedContainsKeywordsMaybe = Lists.newArrayList(// @formatter:off
            "dance"// it's OK for analysis, but not for tweeting - leaving it in the maybe pile for a bit, then move up (06.10)
            ,"trial" // it's OKish for analysis (06.10)
            ,"win" // definitely rejecting for tweeting just in case (13.10)
            ,"israel", "israeli", "palestine", "zionism"
            ,"sudan"
            ,"racial"
            ,"voucher"
            ,"meetup"
            ,"sponsored"
            ,"sorry" // new - OK? (25.11)
            
            // new - OK? (01.12)
            ,"retweets" 
            ,"blackfriday"
            ,"mixtape"
            ,"starving"
        );// @formatter:on

        public final static List<String> acceptedContainsKeywordsOverrides = Lists.newArrayList(// @formatter:off
            //
        );// @formatter:on

        // by starts with

        public final static List<String> bannedStartsWithExprs = Lists.newArrayList(// @formatter:off
            // 
        );// @formatter:on

        // by expression

        public final static List<String> bannedExpressions = Lists.newArrayList(// @formatter:off
            // 
        ); // @formatter:on

        public final static List<String> bannedExpressionsMaybe = Lists.newArrayList(// @formatter:off
             "our love"
        ); // @formatter:on

        // by regex

        /** if this matches, the banned expressions are no longer evaluated */
        public final static List<String> acceptedRegExes = Lists.newArrayList(// @formatter:off
             // 
        ); // @formatter:on

        public final static List<String> bannedRegExesMaybe = Lists.newArrayList(// @formatter:off
             ".*\\bI made\\b.*"
            ,".*\\bon my\\b.*"
            ,".*\\bI just published\\b.*"
            ,".*\\bI will be presenting\\b.*"
            ,".*\\bwith me\\b.*"
        ); // @formatter:on
        public final static List<String> bannedRegExes = Lists.newArrayList(// @formatter:off
            //
        ); // @formatter:on

        /**
         * These are general checks that only apply for the non-technical accounts
         */
        public static final class NonTechnicalOnly {

            // by contains

            public final static List<String> bannedContainsKeywords = Lists.newArrayList(// @formatter:off
                // 
            );// @formatter:on
            public final static List<String> bannedContainsKeywordsMaybe = Lists.newArrayList(// @formatter:off
                 "stolen" 
                ,"cries"
                ,"puppy love"
                ,"karma" // Justin Biebers dog 
                ,"justin", "justin's" ,"justins"
            );// @formatter:on

            /**
             * These are special cases that are OK <br/>
             * - <b>ex</b>: `killed it` is a special case for `killed` that is OK
             */
            public final static List<String> acceptedContainsKeywordsOverrides = Lists.newArrayList(// @formatter:off
                // 
            );// @formatter:on

            // by starts with

            public final static List<String> bannedStartsWithExprs = Lists.newArrayList(// @formatter:off
                // 
            );// @formatter:on

            // by expression

            public final static List<String> bannedExpressions = Lists.newArrayList(// @formatter:off
                // 
            ); // @formatter:on

            public final static List<String> bannedExpressionsMaybe = Lists.newArrayList(// @formatter:off
                // 
            ); // @formatter:on

            // by regex

            // note: move the logging of this back up to error if something new is added
            /** if this matches, the banned expressions are no longer evaluated */
            public final static List<String> acceptedRegExes = Lists.newArrayList(// @formatter:off
                 // 
            ); // @formatter:on

            public final static List<String> bannedRegExes = Lists.newArrayList(// @formatter:off
                // 
            ); // @formatter:on
            static {
                //
                // bannedRegExes.add(RejectExpressionUtil.rejectWinStart("chance"));
            }
            public final static List<String> bannedRegExesMaybe = Lists.newArrayList(// @formatter:off
                //
            ); // @formatter:on
            static {
                //
                // bannedRegExesMaybe.add(RejectExpressionUtil.rejectWinStart("you could"));
            }
        }

    }

    public static final class ForAnalysis {

        // by contains

        public final static List<String> bannedContainsKeywords = Lists.newArrayList(// @formatter:off
             "freelance", "job", "consulting", "hire", "hiring", "careers" 
            ,"football" 
            // ,"exclusive" // no hits yet, but it does create some false positives in my manual tests - commenting out for now
            // ,"dumb", "dumber" // were on maybe - didn't really find to many wrong tweets - for now - they're OK (06.10)
            ,"highheels"
            ,"djmix", "housemusic"
            ,"escort", "escorts", "xxx", "porn", "fuck", "boobs", "breastfeeding", "bitch", "whore", "slut", "cunt" 
            ,"killed"
            ,"islamic", "islam", "muslim", "muslims", "pakistan", "egypt", "syria", "jewish", "jew"
            ,"snake" // python snake...yes, it happened
            ,"followback"
            ,"remix"
        );// @formatter:on
        public final static List<String> bannedContainsKeywordsMaybe = Lists.newArrayList(// @formatter:off
             "fpl" // fantasy player league
            ,"kurd", "kurds", "afganistan", "palestinians" // other political stuff
            ,"iran", "irak", "bosnia", "boycott"
            ,"hindus" // new (13.10)
            ,"thugs" // new
            ,"racial"
            ,"lips"
        );// @formatter:on

        /**
         * These are special cases that are OK <br/>
         * - <b>ex</b>: `killed it` is a special case for `killed` that is OK
         */
        public final static List<String> acceptedContainsKeywordsOverrides = Lists.newArrayList(// @formatter:off
            "killed it"
        );// @formatter:on

        // by starts with

        public final static List<String> bannedStartsWithExprs = Lists.newArrayList(// @formatter:off
            "photo: "
        );// @formatter:on

        // by expression

        public final static List<String> bannedExpressions = Lists.newArrayList(// @formatter:off
            "web developer", "web developers"
            ,"application engineer", "application engineers" 
            ,"python developer", "java developer", "php developer", "clojure developer", "c# developer", "c++ developer" 
            ,"backend developer", "back end developer", "frontend developer", "front end developer", "fullstack developer", "full stack developer" 
            ,"on strike" 
            ,"rt if" 
            ,"i need", "we need", "need help", "need someone" 
            ,"music video"
        ); // @formatter:on

        public final static List<String> bannedExpressionsMaybe = Lists.newArrayList(// @formatter:off
            // 
        ); // @formatter:on

        // by regex

        // note: move the logging of this back up to error if something new is added
        /** if this matches, the banned expressions are no longer evaluated */
        public final static List<String> acceptedRegExes = Lists.newArrayList(// @formatter:off
             // 
        ); // @formatter:on

        public final static List<String> bannedRegExes = Lists.newArrayList(// @formatter:off
             "I'm broadcasting .* on .*" // I'm broadcasting #LIVE on #HangWith for #iPhone! Come Hang w/souljaboy! http://bit.ly/hangwsocial
            ,"Follow us on (Linkedin|Twitter|G+) .*" // Follow us on Linkedin - http://linkd.in/V4Fxa5  #Android #iOS #PS3 #Xbox360 #Apps #GameDev #IDRTG #Video #Game #Developer
            ,".*R(T|t)[ .!@\\-].*R(T|t)([ .!@\\-]|\\Z).*" // 2 RTs
            ,".*(?i)FREE[ .!@\\-].*R(T|t)([ .!@\\-]|\\Z).*" // Free ... RT
            ,".*(f|F)ollow (&|and|AND) R(T|t).*" // Follow & RT
            ,".*(R|r)(T|t) .* (f|F)ollow(ed)? .*" // RT this if you want me to follow you
            ,"(?i)(pls|please) rt.*" // pls rt
            ,".*\\d(\\d)?% (o|O)ff.*" // 97% Off
            ,"(?i).*follow @.*"
        ); // @formatter:on

        public final static List<String> bannedRegExesMaybe = Lists.newArrayList(// @formatter:off
            // other commercial
            ".*only\\b.*\\$.*" // +1 +1 +1 +1 -1 -1
        ); // @formatter:on

        public static final class Commercial {

            // by contains

            public final static List<String> bannedContainsKeywords = Lists.newArrayList(// @formatter:off
                "buy" 
                ,"discount" // newly reactivated (10.11) - I don't think there are to many false positive for this word 
                // ,"gift" // more fine grained stuff in use now
                ,"promo" // newly moved (02.11)
                ,"free trial" // identified from the trial keyword - all selling something
                ,"giveaway" // verified - moved (11.11)
                ,"lucky" // moved - it's OK to skip these
            );// @formatter:on
            public final static List<String> bannedContainsKeywordsMaybe = Lists.newArrayList(// @formatter:off
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
            public final static List<String> acceptedContainsKeywordsOverrides = Lists.newArrayList(// @formatter:off
                "win-win", "win/win"
            );// @formatter:on

            // by starts with

            public final static List<String> bannedStartsWithExprs = Lists.newArrayList(// @formatter:off
                "#win"
            );// @formatter:on

            // by expression

            public final static List<String> bannedExpressions = Lists.newArrayList(// @formatter:off
                // 
            ); // @formatter:on

            public final static List<String> bannedExpressionsMaybe = Lists.newArrayList(// @formatter:off
                // 
            ); // @formatter:on

            // by regex

            // note: move the logging of this back up to error if something new is added
            /** if this matches, the banned expressions are no longer evaluated */
            public final static List<String> acceptedRegExes = Lists.newArrayList(// @formatter:off
                 ".*\\bdeal\\b with.*"
                ,".*deal.*merger.*", ".*merger.*deal.*"
                ,".*win.*merger.*", ".*merger.*win.*"
            ); // @formatter:on

            public final static List<String> bannedRegExes = Lists.newArrayList(// @formatter:off
                // win - commercial stuff
                 "Get .* on Amazon.*" // Get 42% off Secrets of the #JavaScript Ninja on Amazon http://amzn.to/12kkaUn @jeresig
                    
                // deal - commercial stuff
                ,".*\\% \\boff\\b.*\\bdeal.*"
                
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
            public final static List<String> bannedRegExesMaybe = Lists.newArrayList(// @formatter:off
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

    // tweet banned

    /**
     * - <b>local</b> <br/>
     * Tweet can be banned FOR ANALYSIS AND TWEETING by: <br/>
     * - expression (multiple words) <br/>
     * - single word - contains <br/>
     * - single word - starts with <br/>
     * - regular expression - matches <br/>
    */
    public static boolean isTweetBannedForTweeting(final String originalTweet, final TweetType tweetType) {
        if (isTweetBannedForAnalysis(originalTweet)) {
            return true;
        }

        if (new ForTweetingEvaluator().isTweetBanned(originalTweet)) {
            return true;
        }

        if (tweetType == TweetType.NonTech && new ForNonTechnicalEvaluator().isTweetBanned(originalTweet)) {
            return true;
        }

        return false;
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
        return new ChainingEvaluator(new ForCommercialAnalysisEvaluator(), new ForAnalysisEvaluator()).isTweetBanned(originalTweet);
    }

    /*testing only*/public static boolean isTweetBannedForCommercialAnalysis(final String originalTweet) {
        return new ForCommercialAnalysisEvaluator().isTweetBanned(originalTweet);
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
        return new ForAnalysisEvaluator().isRejectedByContainsKeywordMaybe(tweetTokens, originalTweet);
    }

    /**
     * - <b>local</b> <br/>
     */
    static boolean isRejectedByBannedRegexExpressionsForAnalysis(final String originalTweet) {
        return new ForAnalysisEvaluator().isRejectedByBannedRegexExpressions(originalTweet);
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
