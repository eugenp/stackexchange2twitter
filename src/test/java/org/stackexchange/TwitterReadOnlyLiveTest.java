package org.stackexchange;

import java.io.IOException;
import java.util.List;

import org.common.spring.CommonContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.spring.StackexchangeContextConfig;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.twitter.service.TwitterReadLiveService;
import org.tweet.twitter.service.TwitterTemplateCreator;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class, TwitterConfig.class, TwitterLiveConfig.class, StackexchangeContextConfig.class })
public class TwitterReadOnlyLiveTest {

    @Autowired
    private TwitterReadLiveService twitterService;

    @Autowired
    private TwitterTemplateCreator twitterCreator;

    // tests

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

    @Test
    public final void whenRetrievingTwitterProfile_thenNoxceptions() throws JsonProcessingException, IOException {
        final TwitterProfile profileOfUser = twitterService.getProfileOfUser("selsaber");
        System.out.println(profileOfUser);
        System.out.println(profileOfUser.getLanguage());
    }

}
