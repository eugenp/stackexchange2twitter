package org.tweet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.keyval.persistence.service.KeyValPersistenceIntegrationTest;
import org.rss.service.RssEntryPersistenceIntegrationTest;
import org.stackexchange.persistence.service.QuestionTweetPersistenceIntegrationTest;
import org.stackexchange.persistence.setup.StackexchangeSetupPersistenceIntegrationTest;
import org.tweet.meta.persistence.service.RetweetPersistenceIntegrationTest;
import org.tweet.meta.service.TweetMetaLocalServiceIntegrationTest;
import org.tweet.twitter.service.TweetServiceLiveTest;

@RunWith(Suite.class)
@SuiteClasses({// @formatter:off
    QuestionTweetPersistenceIntegrationTest.class, 
    RssEntryPersistenceIntegrationTest.class, 
    RetweetPersistenceIntegrationTest.class,
    KeyValPersistenceIntegrationTest.class,
    
    TweetMetaLocalServiceIntegrationTest.class, 
    
    StackexchangeSetupPersistenceIntegrationTest.class, 
    
    PropertiesExistIntegrationTestSuite.class, 
    
    TweetServiceLiveTest.class
}) // @formatter:on
// SetupBackupIntegrationTest - not to be included
public final class IntegrationTestSuite {
    //
}
