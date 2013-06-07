package org.tweet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.tweet.twitter.util.HashtagUnitTest;
import org.tweet.twitter.util.TwitterUtilUnitTest;

@RunWith(Suite.class)
@SuiteClasses({ HashtagUnitTest.class, TwitterUtilUnitTest.class })
public final class UnitTestSuite {
    //
}
