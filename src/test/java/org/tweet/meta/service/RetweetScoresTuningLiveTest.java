package org.tweet.meta.service;

import java.util.List;

import org.classification.spring.ClassificationConfig;
import org.common.service.live.LinkLiveService;
import org.common.spring.CommonContextConfig;
import org.common.spring.CommonPersistenceJPAConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.tweet.twitter.service.live.TwitterReadLiveService;

import com.google.common.collect.Lists;

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
@ActiveProfiles(SpringProfileUtil.LIVE)
public class RetweetScoresTuningLiveTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterReadLiveService twitterService;

    @Autowired
    private LinkLiveService linkService;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoException() {
        //
    }

    /*
     * AspnetDaily - Twitter introduces a weird link to ASP.NET (which should not be a link)
    */
    @Test
    public final void whenOneAccountIsAnalyzed_thenScoreSuggestionsAreGiven() {
        analyzeScoresForAccount(TwitterAccountEnum.BestAlgorithms.name());
    }

    @Test
    public final void whenAllAccountsAreAnalyzed_thenScoreSuggestionsAreGiven() {
        for (final TwitterAccountEnum account : TwitterAccountEnum.values()) {
            if (account.isRt()) {
                analyzeScoresForAccount(account.name());
            }
        }
    }

    private void analyzeScoresForAccount(final String account) {
        int numberOfTweetsRetrieved;
        final List<String> latestTweetsOnAccount = twitterService.listTweetsOfInternalAccount(account, 14);
        numberOfTweetsRetrieved = latestTweetsOnAccount.size();

        final List<String> relevantDomains = Lists.newArrayList("http://stackoverflow.com/", "http://askubuntu.com/", "http://superuser.com/");
        final int totalRelevantLinks = linkService.countLinksToAnyDomain(latestTweetsOnAccount, relevantDomains);
        final int totalLinksNotToSo = numberOfTweetsRetrieved - totalRelevantLinks;

        if (totalLinksNotToSo <= 2) {
            logger.info("Number of links not to SO for account= " + account + " is= " + totalLinksNotToSo);
            logger.warn("Scores (minrt) are probably to HIGH for account= " + account);
            System.out.println("Scores (minrt) are probably to HIGH for account= " + account);
        } else if (totalLinksNotToSo >= 6) {
            logger.info("Number of links not to SO for account= " + account + " is= " + totalLinksNotToSo);
            logger.warn("Scores (minrt) are probably to LOW for account= " + account);
            System.out.println("Scores (minrt) are probably to LOW for account= " + account);
        } else {
            // logger.debug("Number of links not to SO for account= " + account + " is= " + totalLinksNotToSo);
            // logger.debug("Scores (minrt) look OK for account= " + account);
            System.out.println("Scores (minrt) look OK for account= " + account);
        }
    }
}
