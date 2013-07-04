package org.tweet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.stackexchange.StackTagsExistsIntegrationTest;
import org.stackexchange.service.TwitterMinScoreIntegrationTest;
import org.tweet.TwitterTagsToHashExistsIntegrationTest;
import org.tweet.meta.service.TwitterMaxRtScoreExistsIntegrationTest;
import org.tweet.twitter.service.TwitterHashtagWordsIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({// @formatter:off
    TwitterHashtagWordsIntegrationTest.class, 
    TwitterMinScoreIntegrationTest.class,
    TwitterMaxRtScoreExistsIntegrationTest.class, 
    TwitterTagsToHashExistsIntegrationTest.class, 
    StackTagsExistsIntegrationTest.class
}) // @formatter:on
// SetupBackupIntegrationTest - not to be included
public final class PropertiesExistIntegrationTestSuite {
    //
}
