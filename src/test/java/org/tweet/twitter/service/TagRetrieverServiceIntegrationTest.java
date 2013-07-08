package org.tweet.twitter.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.SimpleTwitterAccount;
import org.tweet.spring.TwitterConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TwitterConfig.class })
public class TagRetrieverServiceIntegrationTest {

    @Autowired
    private TagRetrieverService tagRetrieverService;

    // API

    @Test
    public final void whenAllTwitterTagsAreRetrieved_thenTwitterTagsExistForAllAccounts() {
        for (final SimpleTwitterAccount twitterAccount : SimpleTwitterAccount.values()) {
            assertNotNull(tagRetrieverService.twitterTagsRaw(twitterAccount.name()));
        }
    }

}
