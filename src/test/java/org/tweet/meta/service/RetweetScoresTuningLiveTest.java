package org.tweet.meta.service;

import java.util.List;

import org.classification.spring.ClassificationConfig;
import org.common.service.LinkService;
import org.common.spring.CommonContextConfig;
import org.common.spring.CommonPersistenceJPAConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    ClassificationConfig.class,
    
    TwitterConfig.class, 
    TwitterLiveConfig.class,
    TwitterMetaPersistenceJPAConfig.class, 
        
    TwitterMetaConfig.class 
}) // @formatter:on
public class RetweetScoresTuningLiveTest {

    @Autowired
    private TwitterLiveService twitterService;

    @Autowired
    private LinkService linkService;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoException() {
        //
    }

    @Test
    public final void whenOneAccountIsAnalyzed_thenScoreSuggestionsAreGiven() {
        analyzeScoresForAccount(SimpleTwitterAccount.BestAWS);
    }

    @Test
    public final void whenAllAccountsAreAnalyzed_thenScoreSuggestionsAreGiven() {
        for (final SimpleTwitterAccount account : SimpleTwitterAccount.values()) {
            analyzeScoresForAccount(account);
        }
    }

    private void analyzeScoresForAccount(final SimpleTwitterAccount account) {
        int numberOfTweetsRetrieved;
        final List<String> latestTweetsOnAccount = twitterService.listTweetsOfInternalAccount(account.name(), 12);
        numberOfTweetsRetrieved = latestTweetsOnAccount.size();

        final int relevantLinks1 = linkService.countLinksToDomain(latestTweetsOnAccount, "http://stackoverflow.com/");
        final int relevantLinks2 = linkService.countLinksToDomain(latestTweetsOnAccount, "http://askubuntu.com/");
        final int relevantLinks3 = linkService.countLinksToDomain(latestTweetsOnAccount, "http://superuser.com/");
        final int totalRelevantLinks = relevantLinks1 + relevantLinks2 + relevantLinks3;
        final int totalLinksNotToSo = numberOfTweetsRetrieved - totalRelevantLinks;
        if (totalLinksNotToSo < 2) {
            System.out.println("Scores (minrt) are probably to high for account= " + account.name());
        }
    }
}
