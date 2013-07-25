package org.tweet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.keyval.persistence.service.KeyValPersistenceIntegrationTest;
import org.rss.service.RssEntryPersistenceIntegrationTest;
import org.stackexchange.persistence.service.QuestionTweetPersistenceIntegrationTest;
import org.stackexchange.persistence.setup.StackexchangeSetupPersistenceIntegrationTest;
import org.tweet.meta.persistence.service.RetweetPersistenceIntegrationTest;
import org.tweet.meta.service.TweetMetaServiceIntegrationTest;
import org.tweet.twitter.service.TweetServiceIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({// @formatter:off
    QuestionTweetPersistenceIntegrationTest.class, 
    RssEntryPersistenceIntegrationTest.class, 
    RetweetPersistenceIntegrationTest.class,
    KeyValPersistenceIntegrationTest.class,
    
    TweetMetaServiceIntegrationTest.class, 
    
    StackexchangeSetupPersistenceIntegrationTest.class, 
    
    PropertiesExistIntegrationTestSuite.class, 
    
    TweetServiceIntegrationTest.class
}) // @formatter:on
// SetupBackupIntegrationTest - not to be included
public final class IntegrationTestSuite {
    //
}
