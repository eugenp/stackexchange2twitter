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
import org.springframework.social.twitter.api.TwitterProfile;
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
public final class InteractionServiceLiveTest {

    @Autowired
    private InteractionLiveService userInteractionService;

    @Autowired
    private TwitterReadLiveService twitterReadLiveService;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoException() {
        assertNotNull(userInteractionService);
    }

    // no

    @Test
    public final void whenTestingIfShouldInteractWithUser1_thenNo() {
        final String userHandle = "johnhike";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = userInteractionService.decideBestInteractionWithAuthorLive(user, userHandle).getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser2_thenNo() {
        final String userHandle = "Moz";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = userInteractionService.decideBestInteractionWithAuthorLive(user, userHandle).getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser3_thenNo() {
        final String userHandle = "HuffPostTech";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = userInteractionService.decideBestInteractionWithAuthorLive(user, userHandle).getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser4_thenNo() {
        final String userHandle = "mitsuhiko";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = userInteractionService.decideBestInteractionWithAuthorLive(user, userHandle).getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser5_thenNo() {
        final String userHandle = "gopivotal";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = userInteractionService.decideBestInteractionWithAuthorLive(user, userHandle).getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser6_thenNo() {
        final String userHandle = "WIRED";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = userInteractionService.decideBestInteractionWithAuthorLive(user, userHandle).getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser7_thenNo() {
        final String userHandle = "chalkers";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = userInteractionService.decideBestInteractionWithAuthorLive(user, userHandle).getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.None));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser8_thenNo() {
        final String userHandle = "javacodegeeks";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = userInteractionService.decideBestInteractionWithAuthorLive(user, userHandle).getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.None));
    }

    // barely - sometimes yes and sometimes no

    @Test
    public final void whenTestingIfShouldInteractWithUser1_thenBarelyNo() {
        final String userHandle = "petrikainulaine";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = userInteractionService.decideBestInteractionWithAuthorLive(user, userHandle).getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.None));
    }

    // yes

    @Test
    public final void whenTestingIfShouldInteractWithUser1_thenYes() {
        final String userHandle = "russmiles"; // for lisp
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = userInteractionService.decideBestInteractionWithAuthorLive(user, userHandle).getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.Mention));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser2_thenYes() {
        final String userHandle = "SpringSource";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = userInteractionService.decideBestInteractionWithAuthorLive(user, userHandle).getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.Mention));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser3_thenYes() {
        final String userHandle = "skillsmatter";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = userInteractionService.decideBestInteractionWithAuthorLive(user, userHandle).getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.Mention));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser4_thenYes() {
        final String userHandle = "jameschesters";
        final TwitterProfile user = twitterReadLiveService.getProfileOfUser(userHandle);
        final TwitterInteraction bestInteractionWithUser = userInteractionService.decideBestInteractionWithAuthorLive(user, userHandle).getTwitterInteraction();
        assertThat(bestInteractionWithUser, equalTo(TwitterInteraction.Mention));
    }

}
