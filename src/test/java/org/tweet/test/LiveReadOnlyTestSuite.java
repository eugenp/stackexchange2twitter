package org.tweet.test;

import org.common.classification.ClassificationLiveTest;
import org.common.http.HttpUtilLiveTest;
import org.common.service.ContentExtractorServiceLiveTest;
import org.gplus.service.GplusExtractorServiceLiveTest;
import org.gplus.stackexchange.GPlusLiveTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.tweet.twitter.service.TwitterTemplateCreatorLiveTest;

@RunWith(Suite.class)
@SuiteClasses({ TwitterTemplateCreatorLiveTest.class, GplusExtractorServiceLiveTest.class, GPlusLiveTest.class, ClassificationLiveTest.class, ContentExtractorServiceLiveTest.class, HttpUtilLiveTest.class })
// SetupBackupIntegrationTest - not to be included
// QuestionsApiLiveTest.class - not in the jar
public final class LiveReadOnlyTestSuite {
    //
}
