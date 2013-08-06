package org.tweet.test;

import org.classification.service.ClassificationUnitTest;
import org.common.text.LinkUtilUnitTest;
import org.common.text.TextUtilsUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.stackexchange.component.StackExchangePageStrategyUnitTest;
import org.tweet.meta.service.RetweetLiveStrategyUnitTest;
import org.tweet.twitter.service.TweetMentionServiceUnitTest;
import org.tweet.twitter.service.TweetServiceUnitTest;
import org.tweet.twitter.util.HashtagUnitTest;
import org.tweet.twitter.util.TwitterUtilUnitTest;

@RunWith(Suite.class)
@SuiteClasses({//@formatter:off
    // common
    TextUtilsUnitTest.class, 
    LinkUtilUnitTest.class,

    // twitter
    HashtagUnitTest.class,
    TwitterUtilUnitTest.class,
    TweetServiceUnitTest.class, 
    TweetMentionServiceUnitTest.class,
    
    // stack
    StackExchangePageStrategyUnitTest.class,
    
    // classif
    ClassificationUnitTest.class,
    
    // meta
    RetweetLiveStrategyUnitTest.class,
    
}) //@formatter:off
public final class UnitTestSuite {
    //
}
