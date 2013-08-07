package org.tweet.test;

import org.classification.service.ClassificationServiceLiveTest;
import org.common.service.ContentExtractorServiceLiveTest;
import org.common.service.live.HttpServiceLiveTest;
import org.gplus.service.GplusExtractorServiceLiveTest;
import org.gplus.stackexchange.GPlusLiveTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.rss.service.RssServiceReadOnlyLiveTest;
import org.stackexchange.TwitterReadLiveServiceReadOnlyLiveTest;
import org.tweet.meta.service.UserInteractionServiceLiveTest;
import org.tweet.twitter.service.TweetServiceReadOnlyLiveTest;
import org.tweet.twitter.service.TwitterTemplateCreatorLiveTest;

@RunWith(Suite.class)
@SuiteClasses({// @formatter:off
    
    // common
    ContentExtractorServiceLiveTest.class, 
    HttpServiceLiveTest.class, 
    
    // twitter
    TwitterTemplateCreatorLiveTest.class, 
    TweetServiceReadOnlyLiveTest.class,
    
    // twitter meta
    UserInteractionServiceLiveTest.class,
    
    // gplus
    GplusExtractorServiceLiveTest.class, 
    GPlusLiveTest.class, 
    
    // classif
    ClassificationServiceLiveTest.class, 
    
    // rss
    RssServiceReadOnlyLiveTest.class, 
    
    // stack
    TwitterReadLiveServiceReadOnlyLiveTest.class
}) // @formatter:on
// QuestionsApiLiveTest.class - not in the jar
public final class LiveReadOnlyTestSuite {
    //
}
