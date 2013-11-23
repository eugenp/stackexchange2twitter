package org.tweet.twitter.service;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.common.spring.CommonServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.TwitterAccountEnum;
import org.stackexchange.util.TwitterTag;
import org.tweet.spring.TwitterConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonServiceConfig.class, TwitterConfig.class })
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

    @Test
    public final void givenAllTwitterTagsAreRetrieved_whenCheckingIfEachIsDefinedAsTag_thenYes() {
        for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
            final List<String> twitterTags = tagRetrieverService.twitterTags(twitterAccount.name());
            for (final String twitterTag : twitterTags) {
                if (!twitterTag.isEmpty()) {
                    assertNotNull(TwitterTag.valueOf(twitterTag));
                }
            }
        }
    }
}
