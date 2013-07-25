package org.tweet.meta.component;

import java.io.IOException;

import org.classification.spring.ClassificationConfig;
import org.common.spring.CommonContextConfig;
import org.common.spring.CommonPersistenceJPAConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.spring.TwitterMetaConfig;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {// @formatter:off
        KeyValPersistenceJPAConfig.class, 
        
        CommonPersistenceJPAConfig.class, 
        CommonContextConfig.class, 
        
        ClassificationConfig.class,
        
        TwitterConfig.class, 
        TwitterLiveConfig.class,
        
        TwitterMetaPersistenceJPAConfig.class, 
        TwitterMetaConfig.class 
}) // @formatter:on
@ActiveProfiles({ SpringProfileUtil.LIVE, SpringProfileUtil.DEPLOYED, SpringProfileUtil.PRODUCTION, SpringProfileUtil.WRITE })
public class MetaPollerLiveTest {

    @Autowired
    private MetaPoller metaPoller;

    // tests

    @Test
    public final void givenSingleAccount_whenCheckingDifferenceBetweenLiveAccountAndLocalData_thenNoExceptions() {
        metaPoller.checkRetweetsMatchOnAccount(TwitterAccountEnum.PerlDaily.name());
    }

    @Test
    public final void givenOnAllAccounts_whenCheckingDifferenceBetweenLiveAccountAndLocalData_thenNoExceptions() throws JsonProcessingException, IOException {
        metaPoller.checkRetweetsMatch();
    }

}
