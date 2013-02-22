package org.tweet.stackexchange;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.ContextConfig;
import org.tweet.spring.PersistenceJPAConfig;
import org.tweet.spring.StackexchangeConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.stackexchange.util.SimpleTwitterAccount;
import org.tweet.twitter.service.TwitterService;
import org.tweet.twitter.service.TwitterTemplateCreator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TwitterConfig.class, ContextConfig.class, PersistenceJPAConfig.class, StackexchangeConfig.class })
@Ignore("by default, there should be no component that is not deployed in production, configured to tweet")
public class TweetServiceLiveTest {

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private TwitterTemplateCreator twitterCreator;

    // tests

    @Test
    public final void whenTweeting_thenNoExceptions() {
        twitterService.tweet(twitterCreator.getTwitterTemplate(SimpleTwitterAccount.BestBash.name()), "What are Unity's keyboard and mouse shortcuts?");
    }

    @Test
    public final void whenUnescapingTextWithEscapedApostrophe_thenGoodOutput() {
        final String unescapedHtml4 = StringEscapeUtils.unescapeHtml4("What are Unity&#39;s keyboard and mouse shortcuts?");
        assertThat(unescapedHtml4, equalTo("What are Unity's keyboard and mouse shortcuts?"));
    }

}
