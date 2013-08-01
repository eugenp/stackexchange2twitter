package org.stackexchange.service;

import static org.junit.Assert.assertTrue;
import static org.stackexchange.persistence.setup.TwitterAccountToStackAccount.twitterAccountToStackSite;
import static org.stackexchange.persistence.setup.TwitterAccountToStackAccount.twitterAccountToStackSites;

import java.io.IOException;

import org.common.service.LinkService;
import org.common.spring.CommonServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.api.constants.StackSite;
import org.stackexchange.component.StackExchangePageStrategy;
import org.stackexchange.spring.StackexchangeConfig;
import org.stackexchange.spring.StackexchangeContextConfig;
import org.stackexchange.spring.StackexchangePersistenceJPAConfig;
import org.stackexchange.util.GenericUtil;
import org.stackexchange.util.StackTag;
import org.stackexchange.util.StackTagAdvanced;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonServiceConfig.class, TwitterConfig.class, TwitterLiveConfig.class, StackexchangeContextConfig.class, StackexchangePersistenceJPAConfig.class, StackexchangeConfig.class })
@ActiveProfiles({ SpringProfileUtil.LIVE, SpringProfileUtil.WRITE, SpringProfileUtil.WRITE_PRODUCTION })
public class TweetStackexchangeServiceLiveTest {

    @Autowired
    private TweetStackexchangeLiveService tweetStackexchangeService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private StackExchangePageStrategy pageStrategy;

    // tests

    @Test
    public final void whenContextIsInitalized_thenNoExceptions() {
        //
    }

    @Test
    public final void whenTweeting_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySite(twitterAccountToStackSite(TwitterAccountEnum.SpringAtSO), TwitterAccountEnum.SpringAtSO.name(), 1);
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTag_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.SpringAtSO), StackTag.spring.name(), TwitterAccountEnum.SpringAtSO.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByDefaultTag_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.SpringAtSO), TwitterAccountEnum.SpringAtSO.name());
        assertTrue(success);
    }

    // by specific tag

    @Test
    public final void whenTweetingByTagAkka_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestScala), StackTag.akka.name(), TwitterAccountEnum.BestScala.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagAlgorithm_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestAlgorithms), StackTag.algorithm.name(), TwitterAccountEnum.BestAlgorithms.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagAWS_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestAWS), StackTagAdvanced.amazonwebservices, TwitterAccountEnum.BestAWS.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagApple_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.InTheAppleWorld), StackTag.apple.name(), TwitterAccountEnum.InTheAppleWorld.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagClojure_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestClojure), StackTag.clojure.name(), TwitterAccountEnum.BestClojure.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagEclipse_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestEclipse), StackTag.eclipse.name(), TwitterAccountEnum.BestEclipse.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagFacebook_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.FacebookDigest), StackTag.facebook.name(), TwitterAccountEnum.FacebookDigest.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagGit_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestGit), StackTag.git.name(), TwitterAccountEnum.BestGit.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagGoogle_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.GoogleDigest), StackTag.google.name(), TwitterAccountEnum.GoogleDigest.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagHibernate_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.HibernateDaily), StackTag.hibernate.name(), TwitterAccountEnum.HibernateDaily.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagJava_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.JavaTopSO), StackTag.java.name(), TwitterAccountEnum.JavaTopSO.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagJquery_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.jQueryDaily), StackTag.jquery.name(), TwitterAccountEnum.jQueryDaily.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagJPA_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestJPA), StackTag.jpa.name(), TwitterAccountEnum.BestJPA.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagMaven_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestMaven), StackTag.maven.name(), TwitterAccountEnum.BestMaven.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagREST_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.RESTDaily), StackTag.rest.name(), TwitterAccountEnum.RESTDaily.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagScala_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestScala), StackTag.scala.name(), TwitterAccountEnum.BestScala.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagWordpress_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.LandOfWordpress), StackTag.wordpress.name(), TwitterAccountEnum.LandOfWordpress.name());
        assertTrue(success);
    }

    // by tag

    @Test
    public final void whenTweetingByRandomTag_thenNoExceptions() throws JsonProcessingException, IOException {
        final StackSite randomSite = GenericUtil.pickOneGeneric(twitterAccountToStackSites(TwitterAccountEnum.BestBash));
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(randomSite, StackTag.bash.name(), TwitterAccountEnum.BestBash.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByDefaultObjectiveCDailyTag_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.ObjectiveCDaily), TwitterAccountEnum.ObjectiveCDaily.name());
        assertTrue(success);
    }

    // AskUbuntu

    @Test
    public final void whenTweetingOnAskUbuntu_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySite(twitterAccountToStackSite(TwitterAccountEnum.AskUbuntuBest), TwitterAccountEnum.AskUbuntuBest.name(), 1);
    }

    @Test
    public final void whenTweetingByDefaultTagOnBestBash_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(StackSite.AskUbuntu, StackTag.bash.name(), TwitterAccountEnum.AskUbuntuBest.name());
    }

}
