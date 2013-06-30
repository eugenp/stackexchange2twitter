package org.tweet.test;

import org.common.http.HttpUtilLiveTest;
import org.common.service.ContentExtractorServiceLiveTest;
import org.common.service.classification.ClassificationServiceLiveTest;
import org.gplus.service.GplusExtractorServiceLiveTest;
import org.gplus.stackexchange.GPlusLiveTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.tweet.twitter.service.TwitterTemplateCreatorLiveTest;

@RunWith(Suite.class)
@SuiteClasses({ TwitterTemplateCreatorLiveTest.class, GplusExtractorServiceLiveTest.class, GPlusLiveTest.class, ClassificationServiceLiveTest.class, ContentExtractorServiceLiveTest.class, HttpUtilLiveTest.class })
// QuestionsApiLiveTest.class - not in the jar
public final class LiveReadOnlyTestSuite {
    //
}
