package org.tweet.twitter.service;

import static org.junit.Assert.assertFalse;

import org.common.spring.CommonContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.TwitterConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class, TwitterConfig.class })
public class TweetServiceIntegrationTest {

    @Autowired
    private TweetService instance;

    // tests

    @Test
    public final void givenTextShouldNotBeTweeted_whenCheckingOnScenario1_thenRejected() {
        assertFalse(instance.isTweetTextWorthTweetingByItself("PETZ NAOTO FAL ELLY #junkmania #ldh #exile #3jsb #yeezy #hba #beentrill #chanel #snapback #python\u2026 http://instagram.com/p/bWL_vVwziM/"));
    }

    @Test
    public final void givenTextShouldNotBeTweeted_whenCheckingOnScenario2_thenRejected() {
        assertFalse(instance.isTweetTextWorthTweetingByItself("Beach day..\uD83D\uDE0D #greece #ios #beautiful @runwaydreamz http://instagram.com/p/bEvmJsI1dn/"));
    }

    @Test
    public final void givenTextShouldNotBeTweeted_whenCheckingOnScenario3_thenRejected() {
        assertFalse(instance.isTweetTextWorthTweetingByItself("#PHP \"@MarceloLopez84: The picture of Ronaldo which i took at Manchester Airport http://twitter.com/MarceloLopez84/status/357459318175588352/photo/1pic.twitter.com/Tx70a6MNqS \""));
    }

    @Test
    public final void givenTextShouldNotBeTweeted_whenCheckingOnScenario4_thenRejected() {
        assertFalse(instance.isTweetTextWorthTweetingByItself("#PHP \"@MarceloLopez84: Heres proof!! pic.twitter.com/HDHf2t1W4A\""));
    }

    @Test
    public final void givenTextShouldNotBeTweeted_whenCheckingOnScenario5_thenRejected() {
        assertFalse(instance.isTweetTextWorthTweetingByItself("#dark #cloud http://instagram.com/p/b6EFkGPgok/"));
    }

}
