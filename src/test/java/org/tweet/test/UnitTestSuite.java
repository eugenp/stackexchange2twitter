package org.tweet.test;

import org.classification.service.ClassificationUnitTest;
import org.common.text.LinkUtilUnitTest;
import org.common.text.TextUtilsUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.stackexchange.component.StackExchangePageStrategyUnitTest;
import org.tweet.meta.service.RetweetStrategyUnitTest;
import org.tweet.twitter.service.TweetServiceUnitTest;
import org.tweet.twitter.util.HashtagUnitTest;
import org.tweet.twitter.util.TwitterUtilUnitTest;

@RunWith(Suite.class)
@SuiteClasses({//@formatter:off
    // twitter
    HashtagUnitTest.class,
    TwitterUtilUnitTest.class,
    TweetServiceUnitTest.class, 
    
    // common
    TextUtilsUnitTest.class, 
    LinkUtilUnitTest.class,
    
    // stack
    StackExchangePageStrategyUnitTest.class,
    
    // classif
    ClassificationUnitTest.class,
    
    // meta
    RetweetStrategyUnitTest.class,
    
}) //@formatter:off
public final class UnitTestSuite {
    //
}
