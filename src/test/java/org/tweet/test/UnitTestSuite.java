package org.tweet.test;

import org.classification.service.ClassificationUnitTest;
import org.common.text.TextUtilsUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.tweet.meta.service.RetweetStrategyUnitTest;
import org.tweet.twitter.util.HashtagUnitTest;
import org.tweet.twitter.util.TwitterUtilUnitTest;

@RunWith(Suite.class)
@SuiteClasses({//@formatter:off
    HashtagUnitTest.class, 
    TwitterUtilUnitTest.class, 
    TextUtilsUnitTest.class, 
    ClassificationUnitTest.class, 
    RetweetStrategyUnitTest.class
}) //@formatter:off
public final class UnitTestSuite {
    //
}
