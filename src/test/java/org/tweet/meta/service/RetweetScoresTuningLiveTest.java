package org.tweet.meta.service;

import java.util.List;

import org.common.spring.CommonContextConfig;
import org.common.spring.CommonPersistenceJPAConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.SimpleTwitterAccount;
import org.tweet.meta.spring.TwitterMetaConfig;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.twitter.service.TwitterLiveService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {// @formatter:off
        KeyValPersistenceJPAConfig.class, 
        
        CommonPersistenceJPAConfig.class, 
        CommonContextConfig.class, 
        
        TwitterConfig.class, 
        TwitterLiveConfig.class,
        TwitterMetaPersistenceJPAConfig.class, 
        
        TwitterMetaConfig.class 
}) // @formatter:on
public class RetweetScoresTuningLiveTest {

    @Autowired
    private TwitterLiveService twitterService;

    // tests

    @Test
    public final void whenContextIsInitialized_thenNoExceptions() {
        for (final SimpleTwitterAccount account : SimpleTwitterAccount.values()) {
            final List<Tweet> latestTweetsOnAccount = twitterService.listTweetsOfInternalAccountRaw(account.name(), 12);

        }
    }
}
