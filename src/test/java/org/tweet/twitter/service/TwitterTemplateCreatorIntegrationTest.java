package org.tweet.twitter.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.TwitterConfig;
import org.tweet.stackexchange.util.SimpleTwitterAccount;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TwitterConfig.class })
public class TwitterTemplateCreatorIntegrationTest {

    @Autowired
    private TwitterTemplateCreator twitterTemplateCreator;

    // API

    @Test(expected = RuntimeException.class)
    public final void givenInvalidAccount_whenRetrievingTwitterClient_thenException() {
        twitterTemplateCreator.getTwitterTemplate(randomAlphabetic(6));
    }

    @Test
    public final void givenValidAccount1_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.ServerFaultBest.name());
    }

    @Test
    public final void givenValidAccount2_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.AskUbuntuBest.name());
    }

    @Test
    public final void givenValidAccount3_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.BestBash.name());
    }

    @Test
    public final void givenValidAccount4_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.SpringAtSO.name());
    }

    @Test
    public final void givenValidAccount5_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.JavaTopSO.name());
    }

}