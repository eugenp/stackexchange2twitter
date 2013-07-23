package org.tweet.twitter.service;

import org.common.spring.CommonContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class, TwitterConfig.class, TwitterLiveConfig.class })
public class TwitterAnalysisLiveServiceLiveTest {

    @Autowired
    private TwitterAnalysisLiveService twitterAnalysisLiveService;

    // API

    @Test
    public final void whenAnalyzingAccount_thenNoExceptions() {
        twitterAnalysisLiveService.analyzeAccount(TwitterAccountEnum.jQueryDaily.name());
        System.out.println();
    }

}
