package org.tweet.meta.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.tweet.meta.TwitterUserSnapshot;
import org.tweet.meta.component.TwitterInteractionValuesRetriever;
import org.tweet.test.TweetFixture;
import org.tweet.twitter.service.TweetMentionService;
import org.tweet.twitter.util.TwitterInteraction;

public final class InteractionLiveServiceUnitTest {

    private InteractionLiveService instance;

    // fixtures

    @Before
    public final void before() {
        instance = new InteractionLiveService();
        instance.tweetMentionService = mock(TweetMentionService.class);

        instance.twitterInteractionValuesRetriever = mock(TwitterInteractionValuesRetriever.class);
        when(instance.twitterInteractionValuesRetriever.getMinSmallAccountRetweetsPercentage()).thenReturn(90);
        when(instance.twitterInteractionValuesRetriever.getMaxRetweetsForTweet()).thenReturn(15);
        when(instance.twitterInteractionValuesRetriever.getMinFolowersOfValuableUser()).thenReturn(300);
        when(instance.twitterInteractionValuesRetriever.getMinRetweetsPercentageOfValuableUser()).thenReturn(4);
        when(instance.twitterInteractionValuesRetriever.getMinRetweetsPlusMentionsOfValuableUser()).thenReturn(10);
        when(instance.twitterInteractionValuesRetriever.getMinTweetsOfValuableUser()).thenReturn(300);
    }

    // tests

    // best interaction - user

    @Test
    public final void givenUserHasHighSelfMentionRetweetRate_whenDecidingInteractionWithUser_thenMention() {
        final int selfRetweetPercentage = 30;
        final TwitterInteraction bestInteractionWithUser = instance.decideAndScoreBestInteractionWithUser(new TwitterUserSnapshot(10, 30, selfRetweetPercentage, 20, 10), TweetFixture.createTwitterProfile()).getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.Mention));
    }

    @Test
    public final void givenUserHasLowSelfMentionRetweetRate_whenDecidingInteractionWithUser_thenRetweet() {
        final int selfRetweetPercentage = 1;
        final TwitterInteraction bestInteractionWithUser = instance.decideAndScoreBestInteractionWithUser(new TwitterUserSnapshot(10, 30, selfRetweetPercentage, 20, 10), TweetFixture.createTwitterProfile()).getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.Retweet));
    }

    // best interaction - tweet

    // public final void givenTweetHasNoValuableMentions_whenDecidingInteractionWithTweet_thenRetweet() {

}
