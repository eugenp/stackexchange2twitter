package org.tweet.test;

import org.classification.service.ClassificationServiceIntegrationTest;
import org.classification.util.SpecificClassificationDataUtilIntegrationTest;
import org.common.service.LinkServiceIntegrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.stackexchange.persistence.setup.StackexchangeSetupPersistenceIntegrationTest;
import org.tweet.meta.service.TweetMetaLocalServiceIntegrationTest;
import org.tweet.twitter.service.TweetServiceIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({// @formatter:off
    PersistenceIntegrationTestSuite.class, 
    PropertiesExistIntegrationTestSuite.class,
    
    // classif
    ClassificationServiceIntegrationTest.class,
    SpecificClassificationDataUtilIntegrationTest.class,
    
    // tweet meta
    TweetMetaLocalServiceIntegrationTest.class, 
    
    // tweet
    TweetServiceIntegrationTest.class,
    
    // stack
    StackexchangeSetupPersistenceIntegrationTest.class, 
    
    // common
    LinkServiceIntegrationTest.class
}) // @formatter:on
// SetupBackupIntegrationTest - not to be included
public final class IntegrationTestSuite {
    //
}
