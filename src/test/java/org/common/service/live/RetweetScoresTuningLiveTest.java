package org.common.service.live;

import java.util.List;

import org.classification.spring.ClassificationConfig;
import org.common.spring.CommonPersistenceJPAConfig;
import org.common.spring.CommonServiceConfig;
import org.common.util.LinkUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
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
@ActiveProfiles({ SpringProfileUtil.LIVE, SpringProfileUtil.PERSISTENCE })
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
        analyzeNumberOfTweetsFromSeForAccount(TwitterAccountEnum.AlgorithmsFact.name());
    }

    @Test
    public final void whenAllAccountsAreAnalyzedForLinksToSE_thenScoreSuggestionsAreGiven() {
        for (final TwitterAccountEnum account : TwitterAccountEnum.values()) {
            if (account.isRt()) {
                analyzeNumberOfTweetsFromSeForAccount(account.name());
            }
        }
    }

    @Test
    public final void whenAnalyzingForRtHadoopDaily_thenScoreSuggestionsAreGiven() {
        analyzeNumberOfRetweetsForAccount(TwitterAccountEnum.HadoopDaily.name());
    }

    @Test
    public final void whenAllAccountsAreAnalyzedForRTs_thenScoreSuggestionsAreGiven() {
        for (final TwitterAccountEnum account : TwitterAccountEnum.values()) {
            if (account.isRt()) {
                analyzeNumberOfRetweetsForAccount(account.name());
            }
        }
    }

    private void analyzeNumberOfTweetsFromSeForAccount(final String account) {
        int numberOfTweetsRetrieved;
        final List<String> latestTweetsOnAccount = twitterService.listTweetsOfInternalAccount(account, 20);
        numberOfTweetsRetrieved = latestTweetsOnAccount.size();

        final int totalRelevantLinks = linkService.countLinksToAnyDomain(latestTweetsOnAccount, LinkUtil.seDomains);
        final int totalLinksNotToSo = numberOfTweetsRetrieved - totalRelevantLinks;

        if (totalLinksNotToSo <= 10) {
            logger.info("Small number of links not to SO for account= " + account + " is= " + totalLinksNotToSo);
            logger.warn("Scores (minrt) are probably to HIGH for account= " + account);
            System.out.println("Scores (minrt) are probably to HIGH for account= " + account);
        } else if (totalLinksNotToSo >= 15) {
            logger.info("Large Number of links not to SO for account= " + account + " is= " + totalLinksNotToSo);
            logger.warn("Scores (minrt) are probably to LOW for account= " + account);
            System.out.println("Scores (minrt) are probably to LOW for account= " + account);
        } else {
            // logger.debug("Number of links not to SO for account= " + account + " is= " + totalLinksNotToSo);
            // logger.debug("Scores (minrt) look OK for account= " + account);
            System.out.println("Scores (minrt) look OK for account= " + account);
        }
    }

    private final void analyzeNumberOfRetweetsForAccount(final String account) {
        final List<Tweet> latestTweetsOnAccount = twitterService.listTweetsOfInternalAccountRaw(account, 20);

        int countRt = 0;
        for (final Tweet tweet : latestTweetsOnAccount) {
            if (tweet.isRetweet()) {
                countRt++;
            }
        }

        if (countRt <= 3) {
            logger.info("Small number of RTs for account= " + account + " is= " + countRt);
        } else if (countRt >= 6) {
            logger.info("Large number of RTs for account= " + account + " is= " + countRt);
        } else {
            logger.info("OK number of RTs for account= " + account + " is= " + countRt);
        }
    }

}
