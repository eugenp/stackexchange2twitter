package org.tweet.test;

import org.common.classification.ClassificationLiveTest;
import org.common.http.HttpUtilLiveTest;
import org.common.service.ContentExtractorServiceLiveTest;
import org.gplus.service.GplusExtractorServiceLiveTest;
import org.gplus.stackexchange.GPlusLiveTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.stackexchange.api.client.QuestionsApiLiveTest;
import org.tweet.twitter.service.TwitterTemplateCreatorLiveTest;

@RunWith(Suite.class)
@SuiteClasses({ TwitterTemplateCreatorLiveTest.class, GplusExtractorServiceLiveTest.class, GPlusLiveTest.class, QuestionsApiLiveTest.class, ClassificationLiveTest.class, ContentExtractorServiceLiveTest.class, HttpUtilLiveTest.class })
// SetupBackupIntegrationTest - not to be included
public final class LiveReadOnlyTestSuite {
    //
}
