package org.tweet.meta.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import org.stackexchange.util.IDUtil;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.meta.spring.TwitterMetaConfig;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.util.SpringProfileUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {// @formatter:off
        KeyValPersistenceJPAConfig.class, 
        
        CommonPersistenceJPAConfig.class, 
        CommonContextConfig.class, 
        
        ClassificationConfig.class,
        
        TwitterConfig.class, 
        
        TwitterMetaPersistenceJPAConfig.class, 
        TwitterMetaConfig.class 
}) // @formatter:on
@ActiveProfiles({ SpringProfileUtil.PRODUCTION })
public class TweetMetaLocalServiceIntegrationTest {

    @Autowired
    private TweetMetaLocalService service;

    @Autowired
    private IRetweetJpaDAO retweetApi;

    // tests

    @Test
    public final void whenContextIsInitialized_thenNoExceptions() {
        //
    }

    // has already been tweeted

    @Test
    public final void givenTweetHasNotBeenTweeted_whenCheckingIfItHasAlreadyBeenTweeted_thenNo() {
        final Retweet retweet = new Retweet(IDUtil.randomPositiveLong(), TwitterAccountEnum.BestAlgorithms.name(), randomAlphabetic(6));
        final boolean hasIt = service.hasThisAlreadyBeenTweetedById(retweet);
        assertFalse(hasIt);
    }

    @Test
    public final void givenTweetHasBeenTweeted_whenCheckingIfItHasAlreadyBeenTweeted_thenNo() {
        final Retweet retweet = new Retweet(IDUtil.randomPositiveLong(), TwitterAccountEnum.BestAlgorithms.name(), randomAlphabetic(6));
        retweetApi.save(retweet);

        final boolean hasIt = service.hasThisAlreadyBeenTweetedById(retweet);
        assertTrue(hasIt);
    }

}
