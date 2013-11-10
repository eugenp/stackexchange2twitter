package org.tweet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.tweet.twitter.util.commercial.AcceptTweetsByWordDealManualTest;
import org.tweet.twitter.util.commercial.AcceptTweetsByWordDealsManualTest;
import org.tweet.twitter.util.commercial.AcceptTweetsByWordGenericCommercialManualTest;
import org.tweet.twitter.util.commercial.AcceptTweetsByWordWinManualTest;
import org.tweet.twitter.util.generic.AcceptTweetsByWordGenericManualTest;

@RunWith(Suite.class)
@SuiteClasses({//@formatter:off
    
    // common
    
    // twitter
    AcceptTweetsByWordWinManualTest.class, 
    
    AcceptTweetsByWordGenericManualTest.class, 
    AcceptTweetsByWordGenericCommercialManualTest.class,
    
    AcceptTweetsByWordDealManualTest.class, 
    AcceptTweetsByWordDealsManualTest.class, 
}) //@formatter:off
public final class ManualAcceptUnitTestSuite { // 83 / 798
    //
}
