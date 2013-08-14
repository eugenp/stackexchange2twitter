package org.tweet.meta.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.classification.spring.ClassificationConfig;
import org.common.spring.CommonPersistenceJPAConfig;
import org.common.spring.CommonServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.meta.spring.TwitterMetaConfig;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;

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

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoException() {
        assertNotNull(userInteractionService);
    }

    // no

    @Test
    public final void whenTestingIfShouldInteractWithUser1_thenNo() {
        assertFalse(userInteractionService.isUserWorthInteractingWith("johnhike"));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser2_thenNo() {
        // no retweets
        assertFalse(userInteractionService.isUserWorthInteractingWith("Moz"));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser3_thenNo() {
        assertTrue(userInteractionService.isUserWorthInteractingWith("HuffPostTech"));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser4_thenNo() {
        assertTrue(userInteractionService.isUserWorthInteractingWith("mitsuhiko"));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser5_thenNo() {
        assertTrue(userInteractionService.isUserWorthInteractingWith("gopivotal"));
    }

    // barely

    @Test
    public final void whenTestingIfShouldInteractWithUser1_thenYesButBarely() {
        assertTrue(userInteractionService.isUserWorthInteractingWith("chalkers"));
    }

    // yes

    @Test
    public final void whenTestingIfShouldInteractWithUser1_thenYes() {
        assertTrue(userInteractionService.isUserWorthInteractingWith("russmiles")); // for lisp
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser2_thenYes() {
        assertTrue(userInteractionService.isUserWorthInteractingWith("petrikainulaine"));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser3_thenYes() {
        assertTrue(userInteractionService.isUserWorthInteractingWith("SpringSource"));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser4_thenYes() {
        assertTrue(userInteractionService.isUserWorthInteractingWith("skillsmatter"));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser5_thenYes() {
        assertTrue(userInteractionService.isUserWorthInteractingWith("jameschesters"));
    }

    @Test
    public final void whenTestingIfShouldInteractWithUser6_thenYes() {
        userInteractionService.decideBestInteractionWithAuthorLive("javacodegeeks");
    }

}
