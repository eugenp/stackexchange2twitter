package org.tweet.meta.component;

import org.classification.spring.ClassificationConfig;
import org.common.spring.CommonPersistenceJPAConfig;
import org.common.spring.CommonServiceConfig;
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
@ContextConfiguration(classes = {// @formatter:off
        KeyValPersistenceJPAConfig.class, 
        
        StackexchangePersistenceJPAConfig.class, 
        
        CommonPersistenceJPAConfig.class, 
        CommonServiceConfig.class, 
        
        ClassificationConfig.class,
        
        TwitterConfig.class, 
        TwitterLiveConfig.class,
        
        TwitterMetaPersistenceJPAConfig.class, 
        TwitterMetaConfig.class 
}) // @formatter:on
@ActiveProfiles({ SpringProfileUtil.LIVE, SpringProfileUtil.DEPLOYED, SpringProfileUtil.WRITE_PRODUCTION, SpringProfileUtil.WRITE, SpringProfileUtil.DEPLOYED_POLLER, SpringProfileUtil.Live.ANALYSIS })
public class InteractionHistoryPollerLiveManualTest {

    @Autowired
    private InteractionHistoryPoller interactionHistoryPoller;

    // tests

    @Test
    public final void whenUpdatingInteractionHistoryOfOneAccount_thenNoExceptions() {
        interactionHistoryPoller.checkAndUpdateHistoryOnAccount(TwitterAccountEnum.LandOfWordpress.name());
    }

    @Test
    public final void whenUpdatingInteractionHistoryOfAllAccounts_thenNoExceptions() throws InterruptedException {
        interactionHistoryPoller.checkAndUpdateHistory();
    }

}
