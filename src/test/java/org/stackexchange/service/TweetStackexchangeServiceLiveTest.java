package org.stackexchange.service;

import static org.junit.Assert.assertTrue;
import static org.stackexchange.persistence.setup.TwitterAccountToStackAccount.twitterAccountToStackSite;
import static org.stackexchange.persistence.setup.TwitterAccountToStackAccount.twitterAccountToStackSites;

import java.io.IOException;

import org.common.service.LinkService;
import org.common.spring.CommonContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.api.constants.StackSite;
import org.stackexchange.component.StackExchangePageStrategy;
import org.stackexchange.spring.StackexchangeConfig;
import org.stackexchange.spring.StackexchangeContextConfig;
import org.stackexchange.spring.StackexchangePersistenceJPAConfig;
import org.stackexchange.util.GenericUtil;
import org.stackexchange.util.SimpleTwitterAccount;
import org.stackexchange.util.StackTag;
import org.stackexchange.util.StackTagAdvanced;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class, TwitterConfig.class, TwitterLiveConfig.class, StackexchangeContextConfig.class, StackexchangePersistenceJPAConfig.class, StackexchangeConfig.class })
public class TweetStackexchangeServiceLiveTest {

    @Autowired
    private TweetStackexchangeService tweetStackexchangeService;

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
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySite(twitterAccountToStackSite(SimpleTwitterAccount.SpringAtSO), SimpleTwitterAccount.SpringAtSO.name(), 1);
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTag_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.SpringAtSO), StackTag.spring.name(), SimpleTwitterAccount.SpringAtSO.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByDefaultTag_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.SpringAtSO), SimpleTwitterAccount.SpringAtSO.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagJava_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.JavaTopSO), StackTag.java.name(), SimpleTwitterAccount.JavaTopSO.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagClojure_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestClojure), StackTag.clojure.name(), SimpleTwitterAccount.BestClojure.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagScala_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestScala), StackTag.scala.name(), SimpleTwitterAccount.BestScala.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagAkka_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestScala), StackTag.akka.name(), SimpleTwitterAccount.BestScala.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagJquery_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.jQueryDaily), StackTag.jquery.name(), SimpleTwitterAccount.jQueryDaily.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagREST_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.RESTDaily), StackTag.rest.name(), SimpleTwitterAccount.RESTDaily.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagEclipse_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestEclipse), StackTag.eclipse.name(), SimpleTwitterAccount.BestEclipse.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagGit_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestGit), StackTag.git.name(), SimpleTwitterAccount.BestGit.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagMaven_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestMaven), StackTag.maven.name(), SimpleTwitterAccount.BestMaven.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagJPA_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestJPA), StackTag.jpa.name(), SimpleTwitterAccount.BestJPA.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagAlgorithm_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestAlgorithms), StackTag.algorithm.name(), SimpleTwitterAccount.BestAlgorithms.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagAWS_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestAWS), StackTagAdvanced.amazonwebservices, SimpleTwitterAccount.BestAWS.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByDefaultObjectiveCDailyTag_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.ObjectiveCDaily), SimpleTwitterAccount.ObjectiveCDaily.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagWordpress_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.LandOfWordpress), StackTag.wordpress.name(), SimpleTwitterAccount.LandOfWordpress.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagGoogle_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.GoogleDigest), StackTag.google.name(), SimpleTwitterAccount.GoogleDigest.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagApple_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.InTheAppleWorld), StackTag.apple.name(), SimpleTwitterAccount.InTheAppleWorld.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagHibernate_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.HibernateDaily), StackTag.hibernate.name(), SimpleTwitterAccount.HibernateDaily.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByRandomTag_thenNoExceptions() throws JsonProcessingException, IOException {
        final StackSite randomSite = GenericUtil.pickOneGeneric(twitterAccountToStackSites(SimpleTwitterAccount.BestBash));
        final boolean success = tweetStackexchangeService.tweetTopQuestionBySiteAndTag(randomSite, StackTag.bash.name(), SimpleTwitterAccount.BestBash.name());
        assertTrue(success);
    }

    // AskUbuntu

    @Test
    public final void whenTweetingOnAskUbuntu_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySite(twitterAccountToStackSite(SimpleTwitterAccount.AskUbuntuBest), SimpleTwitterAccount.AskUbuntuBest.name(), 1);
    }

    @Test
    public final void whenTweetingByDefaultTagOnBestBash_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(StackSite.AskUbuntu, StackTag.bash.name(), SimpleTwitterAccount.AskUbuntuBest.name());
    }

}
