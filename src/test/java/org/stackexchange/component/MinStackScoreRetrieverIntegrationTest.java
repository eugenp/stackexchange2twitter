package org.stackexchange.component;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.api.constants.StackSite;
import org.stackexchange.component.MinStackScoreRetriever;
import org.stackexchange.spring.StackexchangeConfig;
import org.stackexchange.util.StackTag;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { StackexchangeConfig.class })
public class MinStackScoreRetrieverIntegrationTest {

    @Autowired
    private MinStackScoreRetriever minStackScoreRetriever;

    // API

    /**
     * twitterInternal.properties
     */
    @Test
    public final void givenOnStackOverFlowOnly_whenRetrievingMinStackExchangeScoreForTag_thenFound() {
        for (final StackTag stackTag : StackTag.values()) {
            assertNotNull("No min score for tag " + stackTag, minStackScoreRetriever.minScoreRaw(stackTag.name(), StackSite.StackOverflow));
        }
    }

}
