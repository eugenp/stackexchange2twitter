package org.common.persistence.setup.upgrades;

import static org.junit.Assert.assertNotNull;

import org.common.persistence.setup.upgrades.local.ICleanTextOfRetweetsUpgrader;
import org.common.spring.CommonPersistenceJPAConfig;
import org.common.spring.CommonServiceConfig;
import org.common.spring.MyApplicationContextInitializerProv;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.spring.StackexchangePersistenceJPAConfig;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.spring.TwitterMetaConfig;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {//@formatter:off
    CommonServiceConfig.class, 
    CommonPersistenceJPAConfig.class, 
    
    KeyValPersistenceJPAConfig.class, 
    
    StackexchangePersistenceJPAConfig.class, 
    
    TwitterMetaPersistenceJPAConfig.class,
    TwitterMetaConfig.class,
    
    TwitterConfig.class, 
    TwitterLiveConfig.class
})//@formatter:on
@ActiveProfiles({ SpringProfileUtil.DEPLOYED, SpringProfileUtil.LIVE, SpringProfileUtil.PERSISTENCE })
public class CleanTextOfRetweetsUpgraderManualTest {

    static {
        System.setProperty(MyApplicationContextInitializerProv.PERSISTENCE_TARGET_KEY, "prod");
    }

    @Autowired
    private ICleanTextOfRetweetsUpgrader cleanTextOfRetweetsUpgrader;

    // fixtures

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoException() {
        assertNotNull(cleanTextOfRetweetsUpgrader);
    }

    @Test
    public final void whenCleaningTextOfRetweetsOnSingleAccount_thenNoExceptions() {
        cleanTextOfRetweetsUpgrader.cleanTextOfRetweetsOnAccount(TwitterAccountEnum.PerlDaily.name());
    }

    @Test
    public final void whenCleaningTextOfRetweetsOnAllAccounts_thenNoExceptions() {
        cleanTextOfRetweetsUpgrader.cleanTextOfRetweets();
    }

}

// run - 20.08.2013 - nothing
