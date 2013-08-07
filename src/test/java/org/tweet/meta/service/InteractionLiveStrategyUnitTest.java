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
import org.tweet.meta.component.TwitterInteractionValuesRetriever;
import org.tweet.twitter.util.TwitterInteraction;

public final class InteractionLiveStrategyUnitTest {

    private InteractionLiveStrategy instance;

    // fixtures

    @Before
    public final void before() {
        this.instance = new InteractionLiveStrategy();
        this.instance.logger = mock(Logger.class);
        this.instance.userInteractionLiveService = mock(UserInteractionLiveService.class);
        when(this.instance.userInteractionLiveService.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(TwitterInteraction.None);

        this.instance.twitterInteraction = mock(TwitterInteractionValuesRetriever.class);
        when(this.instance.twitterInteraction.getMinFolowersOfValuableUser()).thenReturn(300);
        when(this.instance.twitterInteraction.getMaxRetweetsForTweet()).thenReturn(15);
        when(this.instance.twitterInteraction.getMaxLargeAccountRetweetsPercentage()).thenReturn(90);
        when(this.instance.twitterInteraction.getMinRetweetsPercentageOfValuableUser()).thenReturn(4);
        when(this.instance.twitterInteraction.getMinRetweetsPlusMentionsOfValuableUser()).thenReturn(10);
        when(this.instance.twitterInteraction.getMinTweetsOfValuableUser()).thenReturn(300);
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

    @Test
    public final void givenTweetHasToManyRetweetsAndHasNoMentionValue_whenDeterminingBestInteraction_thenNone() {
        final TwitterInteraction bestInteraction = instance.decideBestInteraction(createTweet(20));
        assertThat(bestInteraction, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void givenTweetHasToManyRetweetsAndHasMentionValue_whenDeterminingBestInteraction_thenMention() {
        when(this.instance.userInteractionLiveService.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(TwitterInteraction.Mention);
        final TwitterInteraction bestInteraction = instance.decideBestInteraction(createTweet(20));
        assertThat(bestInteraction, equalTo(TwitterInteraction.Mention));
    }

    @Test
    public final void givenAuthorHasValueAndTweetHasToManyRetweets_whenDeterminingBestInteraction_thenNone() {
        when(this.instance.userInteractionLiveService.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(TwitterInteraction.Retweet);
        final TwitterInteraction bestInteraction = instance.decideBestInteraction(createTweet(20));
        assertThat(bestInteraction, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void givenAuthorHasValue_whenDeterminingBestInteraction_thenRetweet() {
        when(this.instance.userInteractionLiveService.decideBestInteractionWithAuthorLive(any(TwitterProfile.class), anyString())).thenReturn(TwitterInteraction.Retweet);
        final TwitterInteraction bestInteraction = instance.decideBestInteraction(createTweet(5));
        assertThat(bestInteraction, equalTo(TwitterInteraction.Retweet));
    }

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
