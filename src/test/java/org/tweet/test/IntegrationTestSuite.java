package org.tweet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.stackexchange.persistence.setup.StackexchangeSetupPersistenceIntegrationTest;
import org.tweet.meta.service.TweetMetaLocalServiceIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({// @formatter:off
    PersistenceIntegrationTestSuite.class, 
    
    TweetMetaLocalServiceIntegrationTest.class, 
    
    StackexchangeSetupPersistenceIntegrationTest.class, 
    
    PropertiesExistIntegrationTestSuite.class, 
}) // @formatter:on
// SetupBackupIntegrationTest - not to be included
public final class IntegrationTestSuite {
    //
}
