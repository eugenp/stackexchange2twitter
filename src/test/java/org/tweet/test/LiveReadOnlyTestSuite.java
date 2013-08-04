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
import org.tweet.twitter.service.TweetServiceReadOnlyLiveTest;
import org.tweet.twitter.service.TwitterTemplateCreatorLiveTest;

@RunWith(Suite.class)
@SuiteClasses({// @formatter:off
    TwitterTemplateCreatorLiveTest.class, 
    
    GplusExtractorServiceLiveTest.class, 
    GPlusLiveTest.class, 
    
    ClassificationServiceLiveTest.class, 
    
    RssServiceReadOnlyLiveTest.class, 
    
    ContentExtractorServiceLiveTest.class, 
    HttpServiceLiveTest.class, 
    
    TwitterReadLiveServiceReadOnlyLiveTest.class, 
    TweetServiceReadOnlyLiveTest.class
}) // @formatter:on
// QuestionsApiLiveTest.class - not in the jar
public final class LiveReadOnlyTestSuite {
    //
}
