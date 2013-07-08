package org.tweet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.stackexchange.component.MinStackScoreRetrieverIntegrationTest;
import org.stackexchange.component.StackTagsExistsIntegrationTest;
import org.tweet.meta.component.TwitterMaxRtScoreExistsIntegrationTest;
import org.tweet.twitter.component.TwitterHashtagWordsIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({// @formatter:off
    TwitterHashtagWordsIntegrationTest.class, 
    MinStackScoreRetrieverIntegrationTest.class,
    TwitterMaxRtScoreExistsIntegrationTest.class, 
    StackTagsExistsIntegrationTest.class
}) // @formatter:on
// SetupBackupIntegrationTest - not to be included
public final class PropertiesExistIntegrationTestSuite {
    //
}
