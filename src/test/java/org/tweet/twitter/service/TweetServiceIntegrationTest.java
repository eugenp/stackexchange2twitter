package org.tweet.twitter.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.classification.util.SpecificClassificationDataUtil;
import org.common.spring.CommonServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.TwitterConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonServiceConfig.class, TwitterConfig.class })
// @ActiveProfiles(SpringProfileUtil.LIVE)
public class TweetServiceIntegrationTest {

    @Autowired
    private TweetService instance;

    // tests

    @Test
    public final void whenContextIsInitialized_thenNoExceptions() {
        //
    }

    // process

    @Test
    public final void whenTextIsNotProcessedCorrectlyScenario1_thenNoExceptions() {
        instance.processPreValidity("SearchHub: Join @ErikHatcher at the #Lucene/Solr Meetup in São Paulo, June 11th. Don't miss out! http://wp.me/p3cNDk-2vx");
    }

    @Test
    public final void whenTextIsNotProcessedCorrectlyScenario2_thenNoExceptions() {
        instance.processPreValidity("Verivo is looking for a Sr. Build Engineer with #Maven & #Jenkins experience; interested? Apply here: http://ow.ly/h8naT #jobs");
    }

    @Test
    public final void whenAllNonCommercialTweetsFromClassifierAreProcessed_thenNoExceptions() throws IOException {
        final InputStream is = SpecificClassificationDataUtil.class.getResourceAsStream(SpecificClassificationDataUtil.Training.NONCOMMERCIAL);
        final List<String> tweets = IOUtils.readLines(new BufferedReader(new InputStreamReader(is)));
        for (final String tweet : tweets) {
            instance.processPreValidity(tweet);
        }
    }

    @Test
    public final void whenAllCommercialTweetsFromClassifierAreProcessed_thenNoExceptions() throws IOException {
        final InputStream is = SpecificClassificationDataUtil.class.getResourceAsStream(SpecificClassificationDataUtil.Training.COMMERCIAL);
        final List<String> tweets = IOUtils.readLines(new BufferedReader(new InputStreamReader(is)));
        for (final String tweet : tweets) {
            instance.processPreValidity(tweet);
        }
    }

}
