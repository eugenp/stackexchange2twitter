package org.tweet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.tweet.twitter.util.specific.AcceptTweetsByWordDealManualTest;
import org.tweet.twitter.util.specific.AcceptTweetsByWordDealsManualTest;
import org.tweet.twitter.util.specific.AcceptTweetsByWordGenericManualTest;
import org.tweet.twitter.util.specific.AcceptTweetsByWordWinManualTest;

@RunWith(Suite.class)
@SuiteClasses({//@formatter:off
    
    // common
    
    // twitter
    AcceptTweetsByWordWinManualTest.class, 
    
    AcceptTweetsByWordGenericManualTest.class, 
    
    AcceptTweetsByWordDealManualTest.class, 
    AcceptTweetsByWordDealsManualTest.class, 
}) //@formatter:off
public final class ManualAcceptUnitTestSuite { // 72 failing
    //
}
