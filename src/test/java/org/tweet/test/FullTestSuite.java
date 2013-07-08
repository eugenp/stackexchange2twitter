package org.tweet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ UnitTestSuite.class, LiveReadOnlyTestSuite.class, IntegrationTestSuite.class })
public final class FullTestSuite {
    //
}
