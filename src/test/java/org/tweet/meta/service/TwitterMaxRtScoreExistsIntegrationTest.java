package org.tweet.meta.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.Tag;
import org.tweet.spring.TwitterConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TwitterConfig.class })
public class TwitterMaxRtScoreExistsIntegrationTest {

    @Autowired
    private Environment env;

    // API

    @Test
    public final void whenRetrievingMaxRtScoresForTag_thenFound() {
        for (final Tag tag : Tag.values()) {
            assertNotNull("No maxrt for tag " + tag, env.getProperty(tag.name() + ".maxrt"));
        }
    }

}
