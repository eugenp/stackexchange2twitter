package org.tweet.meta.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.classification.spring.ClassificationConfig;
import org.common.spring.CommonPersistenceJPAConfig;
import org.common.spring.CommonServiceConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.meta.persistence.service.RetweetEntityOps;
import org.tweet.meta.spring.TwitterMetaConfig;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.util.SpringProfileUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {// @formatter:off
        KeyValPersistenceJPAConfig.class, 
        
        CommonPersistenceJPAConfig.class, 
        CommonServiceConfig.class, 
        
        ClassificationConfig.class,
        
        TwitterConfig.class, 
        
        TwitterMetaPersistenceJPAConfig.class, 
        TwitterMetaConfig.class 
}) // @formatter:on
@ActiveProfiles({ SpringProfileUtil.LIVE, SpringProfileUtil.WRITE, SpringProfileUtil.WRITE_PRODUCTION, SpringProfileUtil.PERSISTENCE })
public class TweetMetaLocalServiceIntegrationTest {

    @Autowired
    private TweetMetaLocalService service;

    @Autowired
    private IRetweetJpaDAO retweetApi;

    @Autowired
    private RetweetEntityOps retweetEntityOps;

    // tests

    @Test
    public final void whenContextIsInitialized_thenNoExceptions() {
        //
    }

    // has already been tweeted

    @Test
    public final void givenTweetHasNotBeenTweeted_whenCheckingIfItHasAlreadyBeenTweeted_thenNo() {
        final Retweet retweet = retweetEntityOps.createNewEntity();
        final boolean hasIt = service.hasThisAlreadyBeenTweetedById(retweet);
        assertFalse(hasIt);
    }

    @Test
    public final void givenTweetHasBeenTweeted_whenCheckingIfItHasAlreadyBeenTweeted_thenNo() {
        final Retweet retweet = retweetEntityOps.createNewEntity();
        retweetApi.save(retweet);

        final boolean hasIt = service.hasThisAlreadyBeenTweetedById(retweet);
        assertTrue(hasIt);
    }

    @Test
    @Ignore("TODO: fix - failing only on Jenkins - works locally")
    public final void givenTweetHasBeenTweetedScenario1_whenCheckingIfItHasAlreadyBeenTweeted_thenYes() {
        final String text = "Check out some of the worst #iPhone cases out there! http://bit.ly/1bz9iu7";
        final String twitterAccount = TwitterAccountEnum.InTheAppleWorld.name();
        createIfNotExisting(text, twitterAccount);

        final String verifyText = "#TeamiPhone check out some of the worst #iPhone cases out there! http://bit.ly/1bz9iu7";
        final Retweet existing = service.findLocalCandidateAdvanced(verifyText, twitterAccount);
        assertNotNull(existing);
    }

    // utils

    private Retweet createIfNotExisting(final String text, final String twitterAccount) {
        final Retweet retweet = retweetEntityOps.createNewEntity(twitterAccount);
        if (retweetApi.findOneByTextAndTwitterAccount(text, twitterAccount) == null) {
            retweetApi.save(retweet);
        }
        return retweet;
    }

}
