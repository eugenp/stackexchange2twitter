package org.tweet.meta.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.tweet.meta.component.TwitterInteractionValuesRetriever;
import org.tweet.test.TweetFixture;
import org.tweet.twitter.service.TweetMentionService;
import org.tweet.twitter.service.live.TwitterReadLiveService;
import org.tweet.twitter.util.TwitterInteraction;
import org.tweet.twitter.util.TwitterInteractionWithValue;

public final class InteractionLiveServiceMockUnitTest {

    private InteractionLiveService instance;

    // fixtures

    @Before
    public final void before() {
        this.instance = mock(InteractionLiveService.class);
        this.instance.logger = mock(Logger.class);
        this.instance.twitterInteractionValuesRetriever = mock(TwitterInteractionValuesRetriever.class);
        this.instance.twitterReadLiveService = mock(TwitterReadLiveService.class);
        this.instance.tweetMentionService = mock(TweetMentionService.class);

        when(this.instance.twitterInteractionValuesRetriever.getPagesToAnalyze()).thenReturn(1);
        when(this.instance.twitterInteractionValuesRetriever.getMaxRetweetsForTweet()).thenReturn(15);
        when(this.instance.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.None, 0));
    }

    // tests

    // determine best interaction

    // - tweet itself has mention value

    @Test
    public final void givenTweetHasMentionValue_BestInteractionWithAuthorIsNone_whenDeterminingBestInteraction_thenNone() {
        when(this.instance.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.Mention, 0));
        when(this.instance.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.None, 0));

        final TwitterInteraction bestInteraction = instance.determineBestInteractionRaw(TweetFixture.createTweet(20)).getTwitterInteraction();
        assertThat(bestInteraction, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void givenTweetHasMentionValue_BestInteractionWithAuthorIsMention_whenDeterminingBestInteraction_thenUnclear() {
        when(this.instance.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.Mention, 0));
        when(this.instance.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.Mention, 0));

        final TwitterInteraction bestInteraction = instance.determineBestInteractionRaw(TweetFixture.createTweet(20)).getTwitterInteraction();
        assertThat(bestInteraction, equalTo(TwitterInteraction.Mention)); // or None
    }

    @Test
    public final void givenTweetHasMentionValue_BestInteractionWithAuthorIsRetweet_whenDeterminingBestInteraction_thenNone() {
        when(this.instance.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.Mention, 0));
        when(this.instance.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.Retweet, 0));

        final TwitterInteraction bestInteraction = instance.determineBestInteractionRaw(TweetFixture.createTweet(20)).getTwitterInteraction();
        assertThat(bestInteraction, equalTo(TwitterInteraction.None));
    }

    // - tweet itself has No mention value - None

    @Test
    public final void givenTweetHasValueNone_BestInteractionWithAuthorIsNone_whenDeterminingBestInteraction_thenNone() {
        when(this.instance.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.None, 0));
        when(this.instance.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.None, 0));

        final TwitterInteraction bestInteraction = instance.determineBestInteractionRaw(TweetFixture.createTweet(20)).getTwitterInteraction();
        assertThat(bestInteraction, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void givenTweetHasValueNone_BestInteractionWithAuthorIsMention_whenDeterminingBestInteraction_thenMention() {
        when(this.instance.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.None, 0));
        when(this.instance.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.Mention, 0));

        final TwitterInteraction bestInteraction = instance.determineBestInteractionRaw(TweetFixture.createTweet(20)).getTwitterInteraction();
        assertThat(bestInteraction, equalTo(TwitterInteraction.Mention));
    }

    @Test
    public final void givenTweetHasValueNone_BestInteractionWithAuthorIsRetweet_whenDeterminingBestInteraction_thenNone() {
        when(this.instance.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.None, 0));
        when(this.instance.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.Retweet, 0));

        final TwitterInteraction bestInteraction = instance.determineBestInteractionRaw(TweetFixture.createTweet(20)).getTwitterInteraction();
        assertThat(bestInteraction, equalTo(TwitterInteraction.None));
    }

    // - tweet itself has No mention value - Retweet

    @Test
    /*?*/public final void givenTweetHasValueRetweet_BestInteractionWithAuthorIsNone_whenDeterminingBestInteraction_thenNone() {
        when(this.instance.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.Retweet, 0));
        when(this.instance.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.None, 0));

        final TwitterInteraction bestInteraction = instance.determineBestInteractionRaw(TweetFixture.createTweet(20)).getTwitterInteraction();
        assertThat(bestInteraction, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void givenTweetHasValueRetweet_BestInteractionWithAuthorIsMention_whenDeterminingBestInteraction_thenMention() {
        when(this.instance.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.Retweet, 0));
        when(this.instance.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.Mention, 0));

        final TwitterInteraction bestInteraction = instance.determineBestInteractionRaw(TweetFixture.createTweet(20)).getTwitterInteraction();
        assertThat(bestInteraction, equalTo(TwitterInteraction.Mention));
    }

    @Test
    public final void givenTweetHasValueRetweet_BestInteractionWithAuthorIsRetweet_whenDeterminingBestInteraction_thenNone() {
        when(this.instance.decideBestInteractionWithTweetNotAuthorLive(any(Tweet.class))).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.Retweet, 0));
        when(this.instance.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(new TwitterInteractionWithValue(TwitterInteraction.Retweet, 0));

        final TwitterInteraction bestInteraction = instance.determineBestInteractionRaw(TweetFixture.createTweet(20)).getTwitterInteraction();
        assertThat(bestInteraction, equalTo(TwitterInteraction.Retweet));
    }

}
