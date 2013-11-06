package org.tweet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.tweet.twitter.util.TwitterUtilBannedForAnalysisByRegexManualTest;
import org.tweet.twitter.util.TwitterUtilBannedForTweetingManualTest;

@RunWith(Suite.class)
@SuiteClasses({//@formatter:off
    
    // common
    
    // twitter
    ManualRejectUnitTestSuite.class, 
    ManualAcceptUnitTestSuite.class,
    
    TwitterUtilBannedForAnalysisByRegexManualTest.class, 
    TwitterUtilBannedForTweetingManualTest.class
}) //@formatter:off
public final class ManualUnitTestSuite {
    //
}
