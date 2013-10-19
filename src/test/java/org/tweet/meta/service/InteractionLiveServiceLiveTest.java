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
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.TwitterAccountEnum;
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
@ActiveProfiles({ SpringProfileUtil.LIVE, SpringProfileUtil.PERSISTENCE })
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

    @Test
    public final void whenDecidingBestInteractionWithTweet6_thenNone() {
        final Tweet tweet = twitterReadLiveService.findOne(371618484087566336l);
        final TwitterInteraction bestInteraction = interactionLiveService.determineBestInteraction(tweet, TwitterAccountEnum.BestOfCloud.name()).getTwitterInteraction();
        assertThat(bestInteraction, equalTo(TwitterInteraction.Mention));
    }

    @Test
    public final void whenDecidingBestInteractionWithTweet7_thenNone() {
        final Tweet tweet = twitterReadLiveService.findOne(372034273286443008l);
        final TwitterInteraction bestInteraction = interactionLiveService.determineBestInteraction(tweet, TwitterAccountEnum.BestOfCloud.name()).getTwitterInteraction();
        assertThat(bestInteraction, equalTo(TwitterInteraction.Mention));
    }

    @Test
    public final void whenDecidingBestInteractionWithTweet8_thenNone() {
        final Tweet tweet = twitterReadLiveService.findOne(373031256323522560l);
        final TwitterInteraction bestInteraction = interactionLiveService.determineBestInteraction(tweet, TwitterAccountEnum.BestOfCloud.name()).getTwitterInteraction();
        assertThat(bestInteraction, equalTo(TwitterInteraction.None));
    }

    // determine best interaction with author

    @Test
    public final void whenDeterminngBestInteractionWithAuthor1_thenOK() {
        interactionLiveService.determineBestInteractionWithAuthorLive("Passoker", TwitterAccountEnum.BestOfCloud.name());
    }

    // no

    @Test
    public final void whenTestingIfShouldInteractWithUser1_thenNo() {
        final String userHandle = "johnhike";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = interactionLiveService.decideBestInteractionWithAuthorLive(user, userHandle, "").getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser2_thenNo() {
        final String userHandle = "Moz";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = interactionLiveService.decideBestInteractionWithAuthorLive(user, userHandle, "").getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser3_thenNo() {
        final String userHandle = "HuffPostTech";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = interactionLiveService.decideBestInteractionWithAuthorLive(user, userHandle, "").getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser4_thenNo() {
        final String userHandle = "mitsuhiko";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = interactionLiveService.decideBestInteractionWithAuthorLive(user, userHandle, "").getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.Retweet));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser5_thenNo() {
        final String userHandle = "gopivotal";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = interactionLiveService.decideBestInteractionWithAuthorLive(user, userHandle, "").getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.Retweet));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser6_thenNo() {
        final String userHandle = "WIRED";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = interactionLiveService.decideBestInteractionWithAuthorLive(user, userHandle, "").getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser7_thenNo() {
        final String userHandle = "chalkers";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = interactionLiveService.decideBestInteractionWithAuthorLive(user, userHandle, "").getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.Retweet));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser8_thenNo() {
        final String userHandle = "javacodegeeks";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = interactionLiveService.decideBestInteractionWithAuthorLive(user, userHandle, "").getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser9_thenNo() {
        final String userHandle = "SoftwareHollis";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = interactionLiveService.decideBestInteractionWithAuthorLive(user, userHandle, "").getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.None));
    }

    // barely - sometimes yes and sometimes no

    @Test
    public final void whenTestingIfShouldInteractWithUser1_thenBarelyNo() {
        final String userHandle = "petrikainulaine";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = interactionLiveService.decideBestInteractionWithAuthorLive(user, userHandle, "").getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser2_thenBarelyNo() {
        final String userHandle = "BBCNews";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = interactionLiveService.decideBestInteractionWithAuthorLive(user, userHandle, "").getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.None));
    }

    // yes

    @Test
    public final void whenTestingIfShouldInteractWithUser1_thenYes() {
        final String userHandle = "russmiles"; // for lisp
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = interactionLiveService.decideBestInteractionWithAuthorLive(user, userHandle, "").getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.Mention));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser2_thenYes() {
        final String userHandle = "springcentral";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = interactionLiveService.decideBestInteractionWithAuthorLive(user, userHandle, "").getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.Retweet));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser3_thenYes() {
        final String userHandle = "skillsmatter";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = interactionLiveService.decideBestInteractionWithAuthorLive(user, userHandle, "").getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.Mention));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser4_thenYes() {
        final String userHandle = "jameschesters";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = interactionLiveService.decideBestInteractionWithAuthorLive(user, userHandle, "").getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.Retweet));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser5_thenYes() {
        final String userHandle = "CloudExpo";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = interactionLiveService.decideBestInteractionWithAuthorLive(user, userHandle, "").getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.Mention));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser6_thenYes() {
        final String userHandle = "hortonworks";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = interactionLiveService.decideBestInteractionWithAuthorLive(user, userHandle, "").getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.Mention));
    }

}
