package org.tweet.twitter.service.live;

import org.common.spring.CommonServiceConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.live.TwitterAnalysisLiveService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonServiceConfig.class, TwitterConfig.class, TwitterLiveConfig.class })
@ActiveProfiles(SpringProfileUtil.LIVE)
@Ignore("only dev - to many requests against the twitter APIs")
public class TwitterAnalysisLiveServiceLiveTest {

    @Autowired
    private TwitterAnalysisLiveService twitterAnalysisLiveService;

    // API

    @Test
    public final void whenAnalyzingAccount_thenNoExceptions() {
        twitterAnalysisLiveService.calculateLiveStatisticsForAccount(TwitterAccountEnum.jQueryDaily.name());
    }

}
