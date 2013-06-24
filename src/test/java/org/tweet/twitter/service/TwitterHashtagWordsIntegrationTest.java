package org.tweet.twitter.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.SimpleTwitterAccount;
import org.tweet.spring.TwitterConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TwitterConfig.class })
public class TwitterHashtagWordsIntegrationTest {

    @Autowired
    private Environment env;

    // API

    @Test
    public final void whenRetrievingWordsToHashForAccount_thenFound() {
        for (final SimpleTwitterAccount account : SimpleTwitterAccount.values()) {
            assertNotNull("No words to hash for account " + account, env.getProperty(account.name() + ".hash"));
        }
    }

}
