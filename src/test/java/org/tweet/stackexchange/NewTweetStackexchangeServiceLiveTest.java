package org.tweet.stackexchange;

import org.junit.Test;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.tweet.spring.SocialConfig;

public class NewTweetStackexchangeServiceLiveTest {

    // tests

    @Test
    public final void whenTweeting_thenNoExceptions() {
        final TwitterTemplate twitterTemplate = new TwitterTemplate(SocialConfig.CONSUMER_KEY, SocialConfig.CONSUMER_SECRET, SocialConfig.ACCESS_TOKEN, SocialConfig.ACCESS_TOKEN_SECRET);

        twitterTemplate.timelineOperations().updateStatus("First programatic tweet with Spring Social");
    }

}
