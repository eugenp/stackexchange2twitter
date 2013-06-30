package org.tweet.test;

import org.common.service.classification.ClassificationUnitTest;
import org.common.text.TextUtilsUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.tweet.twitter.util.HashtagUnitTest;
import org.tweet.twitter.util.TwitterUtilUnitTest;

@RunWith(Suite.class)
@SuiteClasses({ HashtagUnitTest.class, TwitterUtilUnitTest.class, TextUtilsUnitTest.class, ClassificationUnitTest.class })
public final class UnitTestSuite {
    //
}
