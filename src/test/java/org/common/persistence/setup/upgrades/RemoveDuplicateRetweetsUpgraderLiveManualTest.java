package org.common.persistence.setup.upgrades;

import org.common.persistence.setup.upgrades.live.nolonger.IRemoveDuplicateRetweetsUpgrader;
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
@ActiveProfiles({ SpringProfileUtil.DEPLOYED, SpringProfileUtil.LIVE })
public class RemoveDuplicateRetweetsUpgraderLiveManualTest {

    static {
        System.setProperty(MyApplicationContextInitializerProv.PERSISTENCE_TARGET_KEY, "prod");
    }

    @Autowired
    private IRemoveDuplicateRetweetsUpgrader removeDuplicateRetweetsUpgrader;

    // fixtures

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoException() {
        //
    }

    // some unexpected additional logging messages:
    // CloudDaily, ScalaFact, GoogleDigest
    // wrong results: GoogleDigest
    @Test
    public final void whenRecreatingTheRetweetsOfSingleAccount1_thenNoExceptions() {
        removeDuplicateRetweetsUpgrader.removeDuplicateRetweetsOnAccount(TwitterAccountEnum.CloudDaily.name());
    }

    @Test
    public final void whenRecreatingTheRetweetsOfSingleAccount2_thenNoExceptions() {
        removeDuplicateRetweetsUpgrader.removeDuplicateRetweetsOnAccount(TwitterAccountEnum.GoogleDigest.name());
    }

    @Test
    public final void whenRecreatingTheRetweetsOfAllAccounts_thenNoExceptions() {
        removeDuplicateRetweetsUpgrader.removeDuplicateLocalRetweets();
    }

}
