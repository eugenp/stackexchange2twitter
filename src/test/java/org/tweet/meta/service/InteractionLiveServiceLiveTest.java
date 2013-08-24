package org.tweet.meta.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.classification.spring.ClassificationConfig;
import org.common.spring.CommonPersistenceJPAConfig;
import org.common.spring.CommonServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.meta.spring.TwitterMetaConfig;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.live.TwitterReadLiveService;
import org.tweet.twitter.util.TwitterInteraction;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {// @formatter:off
    KeyValPersistenceJPAConfig.class, 
    
    CommonPersistenceJPAConfig.class, 
    CommonServiceConfig.class, 
    
    ClassificationConfig.class,
    
    TwitterConfig.class, 
    TwitterLiveConfig.class,
    TwitterMetaPersistenceJPAConfig.class, 
        
    TwitterMetaConfig.class 
}) // @formatter:on
@ActiveProfiles(SpringProfileUtil.LIVE)
public final class InteractionLiveServiceLiveTest {

    @Autowired
    private InteractionLiveService interactionLiveService;

    @Autowired
    private TwitterReadLiveService twitterReadLiveService;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoException() {
        assertNotNull(interactionLiveService);
        assertNotNull(twitterReadLiveService);
    }

    // decide ...

    @Test
    public final void whenDecidingBestInteractionWithTweet1_thenNone() {
        final Tweet tweet = twitterReadLiveService.findOne(50510953187516416l);
        final TwitterInteraction bestInteraction = interactionLiveService.determineBestInteraction(tweet, "").getTwitterInteraction();
        assertThat(bestInteraction, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void whenDecidingBestInteractionWithTweet2_thenNone() {
        final Tweet tweet = twitterReadLiveService.findOne(362887164880629762l);
        final TwitterInteraction bestInteraction = interactionLiveService.determineBestInteraction(tweet, "").getTwitterInteraction();
        assertThat(bestInteraction, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void whenDecidingBestInteractionWithTweet3_thenNone() {
        final Tweet tweet = twitterReadLiveService.findOne(364420762922668032l);
        final TwitterInteraction bestInteraction = interactionLiveService.determineBestInteraction(tweet, "").getTwitterInteraction();
        assertThat(bestInteraction, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void whenDecidingBestInteractionWithTweet4_thenNone() {
        final Tweet tweet = twitterReadLiveService.findOne(364424663704670208l);
        final TwitterInteraction bestInteraction = interactionLiveService.determineBestInteraction(tweet, "").getTwitterInteraction();
        assertThat(bestInteraction, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void whenDecidingBestInteractionWithTweet5_thenNone() {
        final Tweet tweet = twitterReadLiveService.findOne(367675643120467968l);
        final TwitterInteraction bestInteraction = interactionLiveService.determineBestInteraction(tweet, "").getTwitterInteraction();
        assertThat(bestInteraction, equalTo(TwitterInteraction.Mention));
    }

}
