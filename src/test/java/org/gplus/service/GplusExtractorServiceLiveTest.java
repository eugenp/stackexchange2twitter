package org.gplus.service;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.common.spring.CommonContextConfig;
import org.gplus.spring.GplusContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.Tag;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class, GplusContextConfig.class })
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
        final String bestTweetCandidate = gplusExtractorService.getBestTweetCandidate(Tag.clojure.name());
        System.out.println(bestTweetCandidate);
        assertNotNull(bestTweetCandidate);
    }

    @Test
    public final void whenTweetIsRetrievedScenario2_thenNoExceptions() throws IOException {
        final String bestTweetCandidate = gplusExtractorService.getBestTweetCandidate(Tag.jquery.name());
        System.out.println(bestTweetCandidate);
        assertNotNull(bestTweetCandidate);
    }

    @Test
    public final void whenTweetIsRetrievedScenario3_thenNoExceptions() throws IOException {
        final String bestTweetCandidate = gplusExtractorService.getBestTweetCandidate(Tag.scala.name());
        System.out.println(bestTweetCandidate);
        assertNotNull(bestTweetCandidate);
    }

}
