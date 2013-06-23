package org.tweet.twitter.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.SimpleTwitterAccount;
import org.tweet.spring.TwitterConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TwitterConfig.class })
public class TwitterTemplateCreatorLiveTest {

    @Autowired
    private TwitterTemplateCreator twitterTemplateCreator;

    // API

    @Test(expected = RuntimeException.class)
    public final void givenInvalidAccount_whenRetrievingTwitterClient_thenException() {
        twitterTemplateCreator.getTwitterTemplate(randomAlphabetic(6));
    }

    @Test
    public final void givenValidAccountServerFaultBest_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.ServerFaultBest.name());
    }

    @Test
    public final void givenValidAccountAskUbuntuBest_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.AskUbuntuBest.name());
    }

    @Test
    public final void givenValidAccountBestBash_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.BestBash.name());
    }

    @Test
    public final void givenValidAccountSpringAtSO_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.SpringAtSO.name());
    }

    @Test
    public final void givenValidAccountJavaTopSO_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.JavaTopSO.name());
    }

    @Test
    public final void givenValidAccountBestClojure_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.BestClojure.name());
    }

    @Test
    public final void givenValidAccountBestScala_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.BestScala.name());
    }

    @Test
    public final void givenValidAccountjQueryDaily_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.jQueryDaily.name());
    }

    @Test
    public final void givenValidAccountRESTDaily_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.RESTDaily.name());
    }

    @Test
    public final void givenValidAccountBestEclipse_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.BestEclipse.name());
    }

    @Test
    public final void givenValidAccountBestGit_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.BestGit.name());
    }

    @Test
    public final void givenValidAccountBestMaven_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.BestMaven.name());
    }

    @Test
    public final void givenValidAccountBestJPA_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.BestJPA.name());
    }

    @Test
    public final void givenValidAccountBestAlgorithms_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.BestAlgorithms.name());
    }

    @Test
    public final void givenValidAccountBestJSON_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.BestJSON.name());
    }

    @Test
    public final void givenValidAccountBestOfJava_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.BestOfJava.name());
    }

    @Test
    public final void givenValidAccountBestSQL_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.BestSQL.name());
    }

    @Test
    public final void givenValidAccountBestNoSQL_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.BestNoSQL.name());
    }

    @Test
    public final void givenValidAccountRegexDaily_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.RegexDaily.name());
    }

    @Test
    public final void givenValidAccountBestPHP_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.BestPHP.name());
    }

    @Test
    public final void givenValidAccountPythonDaily_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.PythonDaily.name());
    }

    @Test
    public final void givenValidAccountBestAWS_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.BestAWS.name());
    }

    @Test
    public final void givenValidAccountObjectiveCDaily_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.ObjectiveCDaily.name());
    }

    @Test
    public final void givenValidAccountiOSdigest_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.iOSdigest.name());
    }

    @Test
    public final void givenValidAccountBestJavaScript_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.BestJavaScript.name());
    }

    @Test
    public final void givenValidAccountPerlDaily_whenRetrievingTwitterClient_thenNoException() {
        twitterTemplateCreator.getTwitterTemplate(SimpleTwitterAccount.PerlDaily.name());
    }

}
