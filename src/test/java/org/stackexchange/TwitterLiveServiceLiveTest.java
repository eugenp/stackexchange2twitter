package org.stackexchange;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.spring.StackexchangeContextConfig;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.TwitterConfig;
import org.tweet.twitter.service.TwitterLiveService;
import org.tweet.twitter.service.TwitterTemplateCreator;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TwitterConfig.class, StackexchangeContextConfig.class })
@Ignore("by default, there should be no component that is not deployed in production, configured to tweet")
public class TwitterLiveServiceLiveTest {

    @Autowired
    private TwitterLiveService twitterService;

    @Autowired
    private TwitterTemplateCreator twitterCreator;

    // tests

    @Test
    public final void whenTweeting_thenNoExceptions() {
        twitterService.tweet(TwitterAccountEnum.BestBash.name(), "What are Unity's keyboard and mouse shortcuts?");
    }

    @Test
    public final void whenUnescapingTextWithEscapedApostrophe_thenGoodOutput() {
        final String unescapedHtml4 = StringEscapeUtils.unescapeHtml4("What are Unity&#39;s keyboard and mouse shortcuts?");
        assertThat(unescapedHtml4, equalTo("What are Unity's keyboard and mouse shortcuts?"));
    }

    // list tweets

    @Test
    public final void whenListingTweets_thenNoExceptions() throws JsonProcessingException, IOException {
        final List<String> tweets = twitterService.listTweetsOfInternalAccount(TwitterAccountEnum.JavaTopSO.name());
        System.out.println(tweets);
    }

    @Test
    public final void whenListingTweetsForHash_thenNoExceptions() throws JsonProcessingException, IOException {
        final List<Tweet> tweets = twitterService.listTweetsOfHashtag(TwitterAccountEnum.JavaTopSO.name(), "java");
        System.out.println(tweets);
    }

}
