package org.tweet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.tweet.twitter.util.commercial.RejectTweetsByWordCommercialGenericManualTest;
import org.tweet.twitter.util.commercial.RejectTweetsByWordDealManualTest;
import org.tweet.twitter.util.commercial.RejectTweetsByWordDealsManualTest;
import org.tweet.twitter.util.commercial.RejectTweetsByWordWinManualTest;
import org.tweet.twitter.util.generic.RejectTweetsByWordGenericManualTest;

@RunWith(Suite.class)
@SuiteClasses({//@formatter:off
    
    // common
    
    // twitter
    RejectTweetsByWordWinManualTest.class,
    
    RejectTweetsByWordGenericManualTest.class,
    RejectTweetsByWordCommercialGenericManualTest.class,
    
    RejectTweetsByWordDealManualTest.class, 
    RejectTweetsByWordDealsManualTest.class, 
}) //@formatter:off
public final class ManualRejectUnitTestSuite { // 289 / 2870
    //
}
