package org.tweet.meta.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.social.twitter.api.TwitterProfile;
import org.tweet.meta.component.TwitterInteractionValuesRetriever;
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
    public final void when_thenNoExceptions() {
        //
    }

}
