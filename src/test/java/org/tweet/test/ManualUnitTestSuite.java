package org.tweet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.tweet.twitter.util.TwitterUtilBannedForAnalysisByRegexManualTest;
import org.tweet.twitter.util.TwitterUtilBannedForTweetingManualTest;
import org.tweet.twitter.util.specific.AcceptTweetsByWordDealManualTest;
import org.tweet.twitter.util.specific.AcceptTweetsByWordDealsManualTest;
import org.tweet.twitter.util.specific.AcceptTweetsByWordGenericManualTest;
import org.tweet.twitter.util.specific.AcceptTweetsByWordWinManualTest;
import org.tweet.twitter.util.specific.RejectTweetsByWordDealManualTest;
import org.tweet.twitter.util.specific.RejectTweetsByWordDealsManualTest;
import org.tweet.twitter.util.specific.RejectTweetsByWordGenericManualTest;
import org.tweet.twitter.util.specific.RejectTweetsByWordWinManualTest;

@RunWith(Suite.class)
@SuiteClasses({//@formatter:off
    
    // common
    
    // twitter
    RejectTweetsByWordWinManualTest.class,
    AcceptTweetsByWordWinManualTest.class, 
    
    RejectTweetsByWordGenericManualTest.class,
    AcceptTweetsByWordGenericManualTest.class, 
    
    AcceptTweetsByWordDealManualTest.class, 
    RejectTweetsByWordDealManualTest.class, 
    AcceptTweetsByWordDealsManualTest.class, 
    RejectTweetsByWordDealsManualTest.class, 
    
    TwitterUtilBannedForAnalysisByRegexManualTest.class, 
    TwitterUtilBannedForTweetingManualTest.class
}) //@formatter:off
public final class ManualUnitTestSuite {
    //
}
