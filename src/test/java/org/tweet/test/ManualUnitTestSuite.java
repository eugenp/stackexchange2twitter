package org.tweet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.tweet.twitter.util.specific.AcceptTweetsByWordDealManualTest;
import org.tweet.twitter.util.specific.AcceptTweetsByWordDealsManualTest;
import org.tweet.twitter.util.specific.AcceptTweetsByWordWinManualTest;
import org.tweet.twitter.util.specific.RejectTweetsByWordDealManualTest;
import org.tweet.twitter.util.specific.RejectTweetsByWordDealsManualTest;
import org.tweet.twitter.util.specific.RejectTweetsByWordWinManualTest;

@RunWith(Suite.class)
@SuiteClasses({//@formatter:off
    
    // common
    
    // twitter
    RejectTweetsByWordWinManualTest.class,
    AcceptTweetsByWordWinManualTest.class, 
    RejectTweetsByWordWinManualTest.class, 
    
    AcceptTweetsByWordDealManualTest.class, 
    AcceptTweetsByWordDealsManualTest.class, 
    RejectTweetsByWordDealManualTest.class, 
    RejectTweetsByWordDealsManualTest.class
}) //@formatter:off
public final class ManualUnitTestSuite {
    //
}
