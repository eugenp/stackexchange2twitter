package org.tweet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.stackexchange.component.MinStackScoreTagRetrieverIntegrationTest;
import org.tweet.twitter.component.MinRtScoreForTwitterTagsExistsIntegrationTest;
import org.tweet.twitter.component.TwitterHashtagWordsIntegrationTest;
import org.tweet.twitter.service.TagRetrieverServiceIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({// @formatter:off
    TwitterHashtagWordsIntegrationTest.class, 
    MinStackScoreTagRetrieverIntegrationTest.class,
    MinRtScoreForTwitterTagsExistsIntegrationTest.class, 
    TagRetrieverServiceIntegrationTest.class
}) // @formatter:on
// SetupBackupIntegrationTest - not to be included
public final class NonTechnicalPropertiesExistIntegrationTestSuite {
    //
}
