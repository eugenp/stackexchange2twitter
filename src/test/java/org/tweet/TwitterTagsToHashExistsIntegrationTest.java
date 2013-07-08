package org.tweet;

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
public class TwitterTagsToHashExistsIntegrationTest {

    @Autowired
    private Environment env;

    // API

    @Test
    public final void whenRetrievingMaxRtScoresForTag_thenFound() {
        for (final SimpleTwitterAccount twitterAccount : SimpleTwitterAccount.values()) {
            assertNotNull("No twitter tags to hash for twitterAccount= " + twitterAccount, env.getProperty(twitterAccount.name() + ".hash"));
        }
    }

}
