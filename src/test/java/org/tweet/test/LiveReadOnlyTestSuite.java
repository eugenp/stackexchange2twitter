package org.tweet.test;

import org.classification.service.ClassificationServiceLiveTest;
import org.common.service.ContentExtractorServiceLiveTest;
import org.common.service.HttpServiceLiveTest;
import org.gplus.service.GplusExtractorServiceLiveTest;
import org.gplus.stackexchange.GPlusLiveTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.stackexchange.TwitterReadOnlyLiveTest;
import org.tweet.twitter.service.TwitterTemplateCreatorLiveTest;

@RunWith(Suite.class)
@SuiteClasses({// @formatter:off
    TwitterTemplateCreatorLiveTest.class, 
    GplusExtractorServiceLiveTest.class, 
    GPlusLiveTest.class, 
    ClassificationServiceLiveTest.class, 
    ContentExtractorServiceLiveTest.class, 
    HttpServiceLiveTest.class, 
    TwitterReadOnlyLiveTest.class }) // @formatter:on
// QuestionsApiLiveTest.class - not in the jar
public final class LiveReadOnlyTestSuite {
    //
}
