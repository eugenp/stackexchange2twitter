package org.tweet.meta.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.classification.spring.ClassificationConfig;
import org.common.spring.CommonContextConfig;
import org.common.spring.CommonPersistenceJPAConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.TwitterAccountEnum;
import org.stackexchange.util.TwitterTag;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.meta.spring.TwitterMetaConfig;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {// @formatter:off
        KeyValPersistenceJPAConfig.class, 
        
        CommonPersistenceJPAConfig.class, 
        CommonContextConfig.class, 
        
        ClassificationConfig.class,
        
        TwitterConfig.class, 
        TwitterLiveConfig.class,
        
        TwitterMetaPersistenceJPAConfig.class, 
        TwitterMetaConfig.class 
}) // @formatter:on
@ActiveProfiles({ SpringProfileUtil.LIVE, SpringProfileUtil.WRITE, SpringProfileUtil.PRODUCTION })
public class TweetMetaServiceLiveTest {

    static {
        System.setProperty("persistenceTarget", "prod");
    }

    @Autowired
    private TweetMetaService tweetMetaService;

    // tests

    @Test
    public final void whenContextIsInitialized_thenNoExceptions() {
        //
    }

    @Test
    public final void whenTweetingAboutJQuery_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetByHashtag(TwitterAccountEnum.jQueryDaily.name(), TwitterTag.jquery.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutClojure_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetByHashtag(TwitterAccountEnum.BestClojure.name(), TwitterTag.clojure.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutScala_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetByHashtag(TwitterAccountEnum.BestScala.name(), TwitterTag.scala.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutGit_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetByHashtag(TwitterAccountEnum.BestGit.name(), TwitterTag.git.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutLisp_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetByHashtag(TwitterAccountEnum.LispDaily.name(), TwitterTag.lisp.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutCloud_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetByHashtag(TwitterAccountEnum.BestOfCloud.name(), TwitterTag.ec2.name());
        assertTrue(success);
    }

    @Test
    // this is for discovery only - Eclipse should only be tweeted from the predefined accounts
    public final void whenTweetingAboutEclipse_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetByHashtag(TwitterAccountEnum.BestEclipse.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingFromPredefinedAccountAboutEclipse_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetByHashtagOnlyFromPredefinedAccounts(TwitterAccountEnum.BestEclipse.name());
        assertTrue(success);
    }

    // checks

    @Test
    public final void whenCheckingIfSomethingHasAlreadyBeenRetrweetedScenario1_thenCorrectAnswer() {
        final String text = "RT @vmbrasseur: This regex MUST become a t-shirt. #osb13 #perl #biking / Credit to @nickpatch http://pic.twitter.com/OrOCsWL2BC";
        final Retweet existing = tweetMetaService.hasThisAlreadyBeenTweetedByText(text, TwitterAccountEnum.PerlDaily.name());
        assertNotNull(existing);
    }

    @Test
    public final void whenCheckingIfSomethingHasAlreadyBeenRetrweetedScenario2_thenCorrectAnswer() {
        final String text = "Blogged: #Scala #Redis client goes non blocking : uses #Akka IO .. http://debasishg.blogspot.in/2013/07/scala-redis-client-goes-non-blocking.html …";
        final Retweet existing = tweetMetaService.hasThisAlreadyBeenTweetedByText(text, TwitterAccountEnum.BestScala.name());
        assertNotNull(existing);
    }

    @Test
    public final void whenCheckingIfSomethingHasAlreadyBeenRetrweetedScenario3_thenCorrectAnswer() {
        final String text = "Recipes for #Akka Dependency Injection - http://tmblr.co/ZlHOLwq7PxvL  by @typesafe";
        final Retweet existing = tweetMetaService.hasThisAlreadyBeenTweetedByText(text, TwitterAccountEnum.BestScala.name());
        assertNotNull(existing);
    }

    @Test
    public final void whenCheckingIfSomethingHasAlreadyBeenRetrweetedScenario4_thenCorrectAnswer() {
        final String text = "#OOCSS + #Sass = The best way to #CSS - http://buff.ly/198JbJv  #webdev #html";
        final Retweet existing = tweetMetaService.hasThisAlreadyBeenTweetedByText(text, TwitterAccountEnum.HTMLdaily.name());
        assertNotNull(existing);
    }

    @Test
    @Ignore("it has just been tweeted - this will pass soon")
    public final void whenCheckingIfSomethingHasAlreadyBeenRetrweetedScenario5_thenCorrectAnswer() {
        final String text = "Morning all! RT @ContractHire http://t.co/dxKq3cl03U follow and you could win a new #iPad Mini! #comp #Apple #ipad";
        final Retweet existing = tweetMetaService.hasThisAlreadyBeenTweetedByText(text, TwitterAccountEnum.InTheAppleWorld.name());
        assertNotNull(existing);
    }

}
