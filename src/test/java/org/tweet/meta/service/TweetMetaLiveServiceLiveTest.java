package org.tweet.meta.service;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.classification.spring.ClassificationConfig;
import org.common.spring.CommonPersistenceJPAConfig;
import org.common.spring.CommonServiceConfig;
import org.common.spring.MyApplicationContextInitializerProv;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.impl.SearchParameters;
import org.springframework.social.twitter.api.impl.SearchParameters.ResultType;
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
import org.tweet.twitter.service.live.TwitterReadLiveService;

import com.fasterxml.jackson.core.JsonProcessingException;

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
@ActiveProfiles({ SpringProfileUtil.LIVE, SpringProfileUtil.WRITE, SpringProfileUtil.WRITE_PRODUCTION })
public class TweetMetaLiveServiceLiveTest {

    static {
        System.setProperty(MyApplicationContextInitializerProv.PERSISTENCE_TARGET_KEY, "prod");
    }

    @Autowired
    private TweetMetaLiveService tweetMetaService;

    @Autowired
    private TwitterReadLiveService twitterReadLiveService;

    @Autowired
    private IRetweetJpaDAO retweetApi;

    // tests

    @Test
    public final void whenContextIsInitialized_thenNoExceptions() {
        //
    }

    @Test
    public final void whenTweetingAboutJQuery_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetAnyByHashtag(TwitterAccountEnum.jQueryDaily.name(), TwitterTag.jquery.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutClojure_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetAnyByHashtag(TwitterAccountEnum.BestClojure.name(), TwitterTag.clojure.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutScala_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetAnyByHashtag(TwitterAccountEnum.BestScala.name(), TwitterTag.scala.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutGit_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetAnyByHashtag(TwitterAccountEnum.BestGit.name(), TwitterTag.git.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutLisp_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetAnyByHashtag(TwitterAccountEnum.LispDaily.name(), TwitterTag.lisp.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutCloud_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetAnyByHashtag(TwitterAccountEnum.BestOfCloud.name(), TwitterTag.ec2.name());
        assertTrue(success);
    }

    @Test
    // this is for discovery only - Eclipse should only be tweeted from the predefined accounts
    public final void whenTweetingAboutEclipse_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetAnyByHashtag(TwitterAccountEnum.BestEclipse.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingFromPredefinedAccountAboutEclipse_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetAnyByHashtagOnlyFromPredefinedAccounts(TwitterAccountEnum.BestEclipse.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutMath_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetAnyByHashtag(TwitterAccountEnum.MathDaily.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutAlgorithms_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaService.retweetAnyByHashtag(TwitterAccountEnum.BestAlgorithms.name());
        assertTrue(success);
    }

    // new type of search

    public final void whenNewTypeOfDataIsSearched_thenCorrect() {
        // the goal here is to pick: https://twitter.com/csdhall/status/364424663704670208
        final String hashtag = "javascript";
        final SearchParameters searchParameters = new SearchParameters("Join #" + hashtag).lang("en").count(100).includeEntities(false).resultType(ResultType.MIXED);
        final SearchResults searchResults = twitterReadLiveService.readOnlyTwitterApi().searchOperations().search(searchParameters);
        // filter out the ones that do not have `Join @`
        // evaluate the rest based on interaction value and use the best
        System.out.println(searchResults);
    }

    // production scenarios

    @Test
    @Ignore("not yet done")
    public final void whenTweetingSimilarToProductionScenario1_thenShouldNotTweet() {
        final String twitterAccount = TwitterAccountEnum.BestOfCloud.name();
        tweetMetaService.tryTweetOne("Five Tips to Improve Your #AWS Security by @Cloud_Optimize ▸ http://t.co/BAMMqlxNUc #Cloud #CloudComputing", null, twitterAccount, null);
    }

}
