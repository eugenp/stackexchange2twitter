package org.stackexchange;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.common.spring.CommonServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.spring.StackexchangeContextConfig;
import org.stackexchange.util.TwitterAccountEnum;
import org.stackexchange.util.TwitterTag;
import org.tweet.meta.service.TweetByRtComparator;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.live.TwitterReadLiveService;
import org.tweet.twitter.util.TweetUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {//@formatter:off
    CommonServiceConfig.class, 
    
    TwitterConfig.class, 
    TwitterLiveConfig.class, 
    
    StackexchangeContextConfig.class 
}) //@formatter:on
@ActiveProfiles(SpringProfileUtil.LIVE)
public class TwitterReadLiveServiceReadOnlyLiveTest {

    @Autowired
    private TwitterReadLiveService instance;

    // tests

    // list tweets

    @Test
    public final void whenListingTweets_thenNoExceptions() throws JsonProcessingException, IOException {
        final List<String> tweets = instance.listTweetsOfInternalAccount(TwitterAccountEnum.JavaTopSO.name());
        System.out.println(tweets);
    }

    @Test
    public final void whenListingTweetsForHash_thenNoExceptions() throws JsonProcessingException, IOException {
        final List<Tweet> tweets = instance.listTweetsOfHashtag(TwitterAccountEnum.JavaTopSO.name(), "java");
        System.out.println(tweets);
    }

    @Test
    public final void whenRetrievingTwitterProfile_thenNoxceptions() throws JsonProcessingException, IOException {
        final TwitterProfile profileOfUser = instance.getProfileOfUser("selsaber");
        System.out.println(profileOfUser);
        System.out.println(profileOfUser.getLanguage());
    }

    @Test
    public final void whenTweetIsRetrieved_thenTextContainsFullLink() {
        final Tweet tweet = instance.findOne(363444216342786048l);
        System.out.println(TweetUtil.getText(tweet));
    }

    @Test
    public final void whenTweetsIsRetrievedByTag_thenAllTweetsContainsFullLinks() {
        final List<Tweet> tweetsOfHashtag = instance.listTweetsOfHashtag(TwitterTag.mysql.name());
        Collections.sort(tweetsOfHashtag, new TweetByRtComparator());
        System.out.println(tweetsOfHashtag);
    }

    // friends

    @Test
    public final void whenFriendIdsAreRetrievedFromAccount1_thenCorrect() {
        final TwitterProfile account = instance.getProfileOfUser("GreatestQuotes");
        final Collection<Long> friendsOfHashtag = instance.getFriendIds(account, 2);
        assertThat(friendsOfHashtag.size(), equalTo(10000));
    }

    @Test
    public final void whenFriendIdsAreRetrievedFromAccount2_thenCorrect() {
        final TwitterProfile account = instance.getProfileOfUser("skillsmatter");
        final Collection<Long> friendsOfHashtag = instance.getFriendIds(account, 2);
        assertThat(friendsOfHashtag.size(), greaterThan(3300));
    }

    @Test
    public final void whenFriendIdsAreRetrievedFromAccount3_thenCorrect() {
        final TwitterProfile account = instance.getProfileOfUser("davesbargains");
        final Collection<Long> friendsOfHashtag = instance.getFriendIds(account, 2);
        assertThat(friendsOfHashtag.size(), greaterThan(8000));
    }

    // list operations

    @Test
    public final void whenOver200TweetsAreListedFromAccount1_thenCorrect() {
        final int howManyPages = 2;
        final List<Tweet> tweetsOfHashtag = instance.listTweetsOfAccountMultiRequestRaw("Moz", howManyPages);
        assertThat(tweetsOfHashtag, hasSize(200 * howManyPages));
    }

    @Test
    public final void whenOver200TweetsAreListedFromAccount2_thenCorrect() {
        final int howManyPages = 3;
        final List<Tweet> tweetsOfHashtag = instance.listTweetsOfAccountMultiRequestRaw("newsycombinator", howManyPages);
        assertThat(tweetsOfHashtag, hasSize(200 * howManyPages));
    }

    @Test
    public final void whenOver200TweetsAreListedFromAccount3_thenCorrect() {
        final int howManyPages = 3;
        final List<Tweet> tweetsOfHashtag = instance.listTweetsOfAccountMultiRequestRaw("africatechie", howManyPages);
        assertThat(tweetsOfHashtag, hasSize(greaterThan(200 * (howManyPages - 1))));
    }

    // list operations - not allowed

    @Test
    public final void givenAccountProtected_whenAreListedFromAccount1_thenNotAllowed() {
        final List<Tweet> list = instance.listTweetsOfAccountMultiRequestRaw("bannahhaker", 1);
        assertThat(list, empty());
    }

}
