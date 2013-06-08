package org.tweet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.tweet.stackexchange.persistence.service.QuestionTweetPersistenceIntegrationTest;
import org.tweet.stackexchange.persistence.setup.StackexchangeSetupPersistenceIntegrationTest;
import org.tweet.twitter.service.TwitterTemplateCreatorLiveTest;

@RunWith(Suite.class)
@SuiteClasses({ TwitterTemplateCreatorLiveTest.class, QuestionTweetPersistenceIntegrationTest.class, StackexchangeSetupPersistenceIntegrationTest.class })
// SetupBackupIntegrationTest - not to be included
public final class IntegrationTestSuite {
    //
}
