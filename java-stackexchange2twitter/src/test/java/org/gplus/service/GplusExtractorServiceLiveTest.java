package org.gplus.service;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.common.spring.CommonServiceConfig;
import org.gplus.spring.GplusContextConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.TwitterTag;
import org.tweet.spring.util.SpringProfileUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonServiceConfig.class, GplusContextConfig.class })
@ActiveProfiles(SpringProfileUtil.LIVE)
@Ignore("G+ is not in use")
public class GplusExtractorServiceLiveTest {

    @Autowired
    private GplusExtractorService gplusExtractorService;

    // tests

    @Test
    public final void whenContextIsInitialized_thenNoExceptions() {
        //
    }

    @Test
    public final void whenTweetIsRetrievedScenario1_thenNoExceptions() throws IOException {
        final String bestTweetCandidate = gplusExtractorService.getBestTweetCandidate(TwitterTag.clojure.name());
        System.out.println("Best Tweet: " + bestTweetCandidate + "\n\n");
        assertNotNull(bestTweetCandidate);
    }

    @Test
    // this has failed at some point because of some live data - java.net.URISyntaxException: Illegal character in query at index
    public final void whenTweetIsRetrievedScenario2_thenNoExceptions() throws IOException {
        final String bestTweetCandidate = gplusExtractorService.getBestTweetCandidate(TwitterTag.jquery.name());
        System.out.println("Best Tweet: " + bestTweetCandidate + "\n\n");
        assertNotNull(bestTweetCandidate);
    }

    @Test
    public final void whenTweetIsRetrievedScenario3_thenNoExceptions() throws IOException {
        final String bestTweetCandidate = gplusExtractorService.getBestTweetCandidate(TwitterTag.scala.name());
        System.out.println("Best Tweet: " + bestTweetCandidate + "\n\n");
        assertNotNull(bestTweetCandidate);
    }

    @Test
    public final void whenTweetIsRetrievedScenario4_thenNoExceptions() throws IOException {
        final String bestTweetCandidate = gplusExtractorService.getBestTweetCandidate(TwitterTag.neo4j.name());
        System.out.println("Best Tweet: " + bestTweetCandidate + "\n\n");
        assertNotNull(bestTweetCandidate);
    }

    @Test
    public final void whenTweetIsRetrievedScenario5_thenNoExceptions() throws IOException {
        final String bestTweetCandidate = gplusExtractorService.getBestTweetCandidate(TwitterTag.mongodb.name());
        System.out.println("Best Tweet: " + bestTweetCandidate + "\n\n");
        assertNotNull(bestTweetCandidate);
    }

}
