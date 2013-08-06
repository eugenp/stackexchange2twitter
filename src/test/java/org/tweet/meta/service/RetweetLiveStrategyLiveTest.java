package org.tweet.meta.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.classification.spring.ClassificationConfig;
import org.common.spring.CommonPersistenceJPAConfig;
import org.common.spring.CommonServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.meta.spring.TwitterMetaConfig;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.live.TwitterReadLiveService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {// @formatter:off
    KeyValPersistenceJPAConfig.class, 
    
    CommonPersistenceJPAConfig.class, 
    CommonServiceConfig.class, 
    
    ClassificationConfig.class,
    
    TwitterConfig.class, 
    TwitterLiveConfig.class,
    TwitterMetaPersistenceJPAConfig.class, 
        
    TwitterMetaConfig.class 
}) // @formatter:on
@ActiveProfiles(SpringProfileUtil.LIVE)
public final class RetweetLiveStrategyLiveTest {

    @Autowired
    private RetweetLiveStrategy retweetLiveStrategy;

    @Autowired
    private TwitterReadLiveService twitterReadLiveService;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoException() {
        assertNotNull(retweetLiveStrategy);
    }

    //

    @Test
    public final void whenCheckingIfTweetShouldBeRetweeted_thenNo() {
        final Tweet tweet = twitterReadLiveService.findOne(50510953187516416l);
        assertFalse(retweetLiveStrategy.shouldRetweet(tweet));
    }

}
