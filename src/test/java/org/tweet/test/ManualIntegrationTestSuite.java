package org.tweet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.tweet.meta.service.TweetMetaLocalServiceManualTest;

@RunWith(Suite.class)
@SuiteClasses({//@formatter:off
    
    TweetMetaLocalServiceManualTest.class
    
}) //@formatter:off
public final class ManualIntegrationTestSuite {
    //
}
