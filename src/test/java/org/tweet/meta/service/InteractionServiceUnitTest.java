package org.tweet.meta.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.tweet.meta.TwitterUserSnapshot;
import org.tweet.twitter.util.TwitterInteraction;

public final class InteractionServiceUnitTest {

    private InteractionLiveService instance;

    // fixtures

    @Before
    public final void before() {
        instance = new InteractionLiveService();
    }

    // tests

    @Test
    public final void givenUserHasHighSelfMentionRetweetRate_whenDecidingInteractionWithUser_thenNoExceptions() {
        final int selfRetweetPercentage = 20;
        final TwitterInteraction bestInteractionWithUser = instance.decideBestInteractionWithUser(new TwitterUserSnapshot(10, 30, selfRetweetPercentage, 20));
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.Mention));
    }

    @Test
    public final void givenUserHasLowSelfMentionRetweetRate_whenDecidingInteractionWithUser_thenNoExceptions() {
        final int selfRetweetPercentage = 1;
        final TwitterInteraction bestInteractionWithUser = instance.decideBestInteractionWithUser(new TwitterUserSnapshot(10, 30, selfRetweetPercentage, 20));
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.Retweet));
    }

}
