package org.tweet.meta.component;

import java.io.IOException;

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

import com.fasterxml.jackson.core.JsonProcessingException;

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
public class MetaPollerLiveManualTest {

    @Autowired
    private MetaPoller metaPoller;

    // tests

    @Test
    public final void givenSingleAccount_whenCheckingDifferenceBetweenLiveAccountAndLocalData_thenNoExceptions() {
        metaPoller.checkRetweetsMatchOnAccount(TwitterAccountEnum.BestClojure.name());
    }

    @Test
    public final void givenOnAllAccounts_whenCheckingDifferenceBetweenLiveAccountAndLocalData_thenNoExceptions() throws JsonProcessingException, IOException, InterruptedException {
        metaPoller.checkRetweetsMatch();
    }

    // 20:48:43.085 [main] INFO org.tweet.meta.component.MetaPoller - On twitterAccount= InTheAppleWorld, SMALL difference between the live and the local retweets is: 5
    // 20:48:24.730 [main] INFO org.tweet.meta.component.MetaPoller - On twitterAccount= GoogleDigest, SMALL difference between the live and the local retweets is: 5

    // 20:47:43.648 [main] INFO org.tweet.meta.component.MetaPoller - On twitterAccount= BestOfCloud, SMALL difference between the live and the local retweets is: 4
    // 20:47:54.818 [main] INFO org.tweet.meta.component.MetaPoller - On twitterAccount= BestOfLinux, SMALL difference between the live and the local retweets is: 4
    // 20:48:11.120 [main] INFO org.tweet.meta.component.MetaPoller - On twitterAccount= BestScala, SMALL difference between the live and the local retweets is: 4

    // 20:47:28.402 [main] INFO org.tweet.meta.component.MetaPoller - On twitterAccount= BestJSON, SMALL difference between the live and the local retweets is: 3
    // 20:48:31.448 [main] INFO org.tweet.meta.component.MetaPoller - On twitterAccount= HTMLdaily, SMALL difference between the live and the local retweets is: 3
    // 20:48:34.791 [main] INFO org.tweet.meta.component.MetaPoller - On twitterAccount= BestOfHTML5, SMALL difference between the live and the local retweets is: 3
    // 20:48:47.652 [main] INFO org.tweet.meta.component.MetaPoller - On twitterAccount= jQueryDaily, SMALL difference between the live and the local retweets is: 3

}
