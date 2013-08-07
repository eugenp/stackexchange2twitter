package org.tweet.meta.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.test.util.ReflectionTestUtils;
import org.tweet.twitter.util.TwitterInteraction;

public final class InteractionLiveStrategyUnitTest {

    private InteractionLiveStrategy instance;

    // fixtures

    @Before
    public final void before() {
        this.instance = new InteractionLiveStrategy();
        this.instance.logger = mock(Logger.class);
        final InteractionLiveService userInteractionLiveServiceMock = mock(InteractionLiveService.class);
        this.instance.userInteractionLiveService = userInteractionLiveServiceMock;
        when(this.instance.userInteractionLiveService.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(TwitterInteraction.None);
    }

    // tests

    @Test
    public final void whenCheckingIfTweetIsToBeRetweeted_thenNoExceptions() {
        final Tweet tweet = createTweet(10);
        instance.shouldRetweetOld(tweet);
    }

    @Test
    public final void givenTweetWith100Rts_whenCheckingIfItShouldBeRetweeted_thenNo() {
        final Tweet tweet = createTweet(100);
        assertFalse(instance.shouldRetweetOld(tweet));
    }

    // determine best interaction

    // - tweet itself has mention value

    @Test
    public final void givenTweetHasMentionValue_BestInteractionWithAuthorIsNone_whenDeterminingBestInteraction_thenNone() {
        when(this.instance.userInteractionLiveService.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(TwitterInteraction.Mention);
        when(this.instance.userInteractionLiveService.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(TwitterInteraction.None);

        final TwitterInteraction bestInteraction = instance.decideBestInteraction(createTweet(20));
        assertThat(bestInteraction, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void givenTweetHasMentionValue_BestInteractionWithAuthorIsMention_whenDeterminingBestInteraction_thenUnclear() {
        when(this.instance.userInteractionLiveService.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(TwitterInteraction.Mention);
        when(this.instance.userInteractionLiveService.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(TwitterInteraction.Mention);

        final TwitterInteraction bestInteraction = instance.decideBestInteraction(createTweet(20));
        assertThat(bestInteraction, equalTo(TwitterInteraction.Mention)); // or None
    }

    @Test
    public final void givenTweetHasMentionValue_BestInteractionWithAuthorIsRetweet_whenDeterminingBestInteraction_thenNone() {
        when(this.instance.userInteractionLiveService.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(TwitterInteraction.Mention);
        when(this.instance.userInteractionLiveService.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(TwitterInteraction.Retweet);

        final TwitterInteraction bestInteraction = instance.decideBestInteraction(createTweet(20));
        assertThat(bestInteraction, equalTo(TwitterInteraction.None));
    }

    // - tweet itself has No mention value - None

    @Test
    public final void givenTweetHasValueNone_BestInteractionWithAuthorIsNone_whenDeterminingBestInteraction_thenNone() {
        when(this.instance.userInteractionLiveService.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(TwitterInteraction.None);
        when(this.instance.userInteractionLiveService.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(TwitterInteraction.None);

        final TwitterInteraction bestInteraction = instance.decideBestInteraction(createTweet(20));
        assertThat(bestInteraction, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void givenTweetHasValueNone_BestInteractionWithAuthorIsMention_whenDeterminingBestInteraction_thenMention() {
        when(this.instance.userInteractionLiveService.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(TwitterInteraction.None);
        when(this.instance.userInteractionLiveService.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(TwitterInteraction.Mention);

        final TwitterInteraction bestInteraction = instance.decideBestInteraction(createTweet(20));
        assertThat(bestInteraction, equalTo(TwitterInteraction.Mention));
    }

    @Test
    public final void givenTweetHasValueNone_BestInteractionWithAuthorIsRetweet_whenDeterminingBestInteraction_thenNone() {
        when(this.instance.userInteractionLiveService.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(TwitterInteraction.None);
        when(this.instance.userInteractionLiveService.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(TwitterInteraction.Retweet);

        final TwitterInteraction bestInteraction = instance.decideBestInteraction(createTweet(20));
        assertThat(bestInteraction, equalTo(TwitterInteraction.Retweet));
    }

    // - tweet itself has No mention value - Retweet

    @Test
    /*?*/public final void givenTweetHasValueRetweet_BestInteractionWithAuthorIsNone_whenDeterminingBestInteraction_thenNone() {
        when(this.instance.userInteractionLiveService.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(TwitterInteraction.Retweet);
        when(this.instance.userInteractionLiveService.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(TwitterInteraction.None);

        final TwitterInteraction bestInteraction = instance.decideBestInteraction(createTweet(20));
        assertThat(bestInteraction, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void givenTweetHasValueRetweet_BestInteractionWithAuthorIsMention_whenDeterminingBestInteraction_thenMention() {
        when(this.instance.userInteractionLiveService.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(TwitterInteraction.Retweet);
        when(this.instance.userInteractionLiveService.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(TwitterInteraction.Mention);

        final TwitterInteraction bestInteraction = instance.decideBestInteraction(createTweet(20));
        assertThat(bestInteraction, equalTo(TwitterInteraction.Mention));
    }

    @Test
    public final void givenTweetHasValueRetweet_BestInteractionWithAuthorIsRetweet_whenDeterminingBestInteraction_thenNone() {
        when(this.instance.userInteractionLiveService.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(TwitterInteraction.Retweet);
        when(this.instance.userInteractionLiveService.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(TwitterInteraction.Retweet);

        final TwitterInteraction bestInteraction = instance.decideBestInteraction(createTweet(20));
        assertThat(bestInteraction, equalTo(TwitterInteraction.Retweet));
    }

    // util

    private final Tweet createTweet(final int retweetCount) {
        final Tweet tweet = new Tweet(0l, randomAlphabetic(6), new Date(), null, null, null, 0l, "en", null);
        final TwitterProfile user = new TwitterProfile(0l, randomAlphabetic(6), randomAlphabetic(6), null, null, null, null, new Date());
        ReflectionTestUtils.setField(user, "language", "en");
        ReflectionTestUtils.setField(user, "screenName", randomAlphabetic(6));
        tweet.setUser(user);
        tweet.setRetweetCount(retweetCount);
        return tweet;
    }

}
