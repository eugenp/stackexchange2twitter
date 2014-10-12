package org.common.persistence.setup.upgrades;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.common.persistence.setup.upgrades.live.IRecreateMissingQuestionTweetsUpgrader;
import org.common.spring.CommonPersistenceJPAConfig;
import org.common.spring.CommonServiceConfig;
import org.common.spring.MyApplicationContextInitializerProv;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
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
import org.tweet.twitter.service.live.TwitterReadLiveService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {//@formatter:off
    CommonServiceConfig.class, 
    CommonPersistenceJPAConfig.class,
    
    StackexchangePersistenceJPAConfig.class,
    
    KeyValPersistenceJPAConfig.class, 
    
    TwitterMetaPersistenceJPAConfig.class,
    TwitterMetaConfig.class,
    
    TwitterConfig.class, 
    TwitterLiveConfig.class
})//@formatter:on
@ActiveProfiles({ SpringProfileUtil.DEPLOYED, SpringProfileUtil.LIVE })
public class RecreateMissingQuestionTweetsUpgraderLiveManualTest {

    static {
        System.setProperty(MyApplicationContextInitializerProv.PERSISTENCE_TARGET_KEY, "prod");
    }

    @Autowired
    private IRecreateMissingQuestionTweetsUpgrader recreateMissingQuestionTweetsUpgrader;

    @Autowired
    private TwitterReadLiveService twitterReadLiveService;

    // fixtures

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoException() {
        assertNotNull(twitterReadLiveService);
        assertNotNull(recreateMissingQuestionTweetsUpgrader);
    }

    @Test
    public final void whenExtractingQuestionIdFromUri_thenOK() {
        final Tweet tweet = twitterReadLiveService.findOne(368009178063073280l);
        final String extractedQid = recreateMissingQuestionTweetsUpgrader.extractQuestionIdFromTweet(tweet);
        assertThat(extractedQid, equalTo("8937743"));
    }

    @Test
    public final void whenRecreatingTheRetweetsOfSingleAccount_thenNoExceptions() {
        recreateMissingQuestionTweetsUpgrader.recreateLocalQuestionTweetsOnAccount(TwitterAccountEnum.BestBash.name());
    }

    @Test
    public final void whenRecreatingTheRetweetsOfAllAccounts_thenNoExceptions() {
        recreateMissingQuestionTweetsUpgrader.recreateLocalQuestionTweetsFromLiveTweets();
    }

}
