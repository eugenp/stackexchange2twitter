package org.tweet.meta;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.common.spring.CommonPersistenceJPAConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.meta.spring.TwitterMetaContextConfig;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.stackexchange.util.SimpleTwitterAccount;
import org.tweet.stackexchange.util.Tag;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TwitterConfig.class, TwitterMetaPersistenceJPAConfig.class, TwitterMetaContextConfig.class, CommonPersistenceJPAConfig.class })
public class TweetMetaServiceLiveTest {

    @Autowired
    private TweetMetaService tweetMetaService;

    // tests

    @Test
    public final void whenContextIsInitialized_thenNoExceptions() {
        //
    }

    @Test
    public final void whenTweeting_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetByHashtag(SimpleTwitterAccount.jQueryDaily.name(), Tag.jquery.name());
        assertTrue(success);
    }

}
