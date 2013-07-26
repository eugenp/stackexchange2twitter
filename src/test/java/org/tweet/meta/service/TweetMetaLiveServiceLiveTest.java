package org.tweet.meta.service;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.classification.spring.ClassificationConfig;
import org.common.spring.CommonContextConfig;
import org.common.spring.CommonPersistenceJPAConfig;
import org.common.spring.MyApplicationContextInitializerProv;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.TwitterAccountEnum;
import org.stackexchange.util.TwitterTag;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
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
public class TweetMetaLiveServiceLiveTest {

    static {
        System.setProperty(MyApplicationContextInitializerProv.PERSISTENCE_TARGET_KEY, "prod");
    }

    @Autowired
    private TweetMetaLiveService tweetMetaService;

    @Autowired
    private IRetweetJpaDAO retweetApi;

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

    // production scenarios

    @Test
    public final void whenTweetingSimilarToProductionScenario1_thenShouldNotTweet(){
        tweetMetaService.tryTweetOne("Five Tips to Improve Your #AWS Security by @Cloud_Optimize ▸ http://t.co/BAMMqlxNUc #Cloud #CloudComputing", url, twitterAccount, customDetails)
    }

}
