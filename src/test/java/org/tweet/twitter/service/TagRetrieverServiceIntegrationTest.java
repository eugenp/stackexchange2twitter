package org.tweet.twitter.service;

import static org.junit.Assert.assertNotNull;

import org.common.spring.CommonContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.util.SpringProfileUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class, TwitterConfig.class })
// TODO: profile temporarily active - fix
@ActiveProfiles(SpringProfileUtil.LIVE)
public class TagRetrieverServiceIntegrationTest {

    @Autowired
    private TagRetrieverService tagRetrieverService;

    // API

    @Test
    public final void whenAllTwitterTagsAreRetrieved_thenTwitterTagsExistForAllAccounts() {
        for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
            assertNotNull(tagRetrieverService.twitterTagsRaw(twitterAccount.name()));
        }
    }

}
