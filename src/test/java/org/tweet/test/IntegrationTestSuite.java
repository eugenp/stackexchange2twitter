package org.tweet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.stackexchange.persistence.service.QuestionTweetPersistenceIntegrationTest;
import org.stackexchange.persistence.setup.StackexchangeSetupPersistenceIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({ QuestionTweetPersistenceIntegrationTest.class, StackexchangeSetupPersistenceIntegrationTest.class })
// SetupBackupIntegrationTest - not to be included
public final class IntegrationTestSuite {
    //
}
