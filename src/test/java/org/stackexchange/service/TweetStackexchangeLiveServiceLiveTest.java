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
import org.stackexchange.api.client.QuestionsApi;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.api.client.util.Preconditions;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {// @formatter:off 
    CommonServiceConfig.class,
    
    TwitterConfig.class, 
    TwitterLiveConfig.class,
    
    StackexchangeContextConfig.class, 
    StackexchangePersistenceJPAConfig.class, 
    StackexchangeConfig.class 
}) // @formatter:on
@ActiveProfiles({ SpringProfileUtil.LIVE, SpringProfileUtil.WRITE, SpringProfileUtil.WRITE_PRODUCTION, SpringProfileUtil.PERSISTENCE })
public class TweetStackexchangeLiveServiceLiveTest {

    @Autowired
    private TweetStackexchangeLiveService instance;

    @Autowired
    private LinkService linkService;

    @Autowired
    private StackExchangePageStrategy pageStrategy;

    @Autowired
    private QuestionsApi questionsApi;

    // tests

    @Test
    public final void whenContextIsInitalized_thenNoExceptions() {
        //
    }

    @Test
    public final void whenTweeting_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySite(twitterAccountToStackSite(TwitterAccountEnum.SpringTip), TwitterAccountEnum.SpringTip.name(), 1);
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTag_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.SpringTip), StackTag.spring.name(), TwitterAccountEnum.SpringTip.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByDefaultTag_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.SpringTip), TwitterAccountEnum.SpringTip.name());
        assertTrue(success);
    }

    // by specific tag

    @Test
    public final void whenTweetingByTagAkka_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestScala), StackTag.akka.name(), TwitterAccountEnum.BestScala.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagAlgorithm_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestAlgorithms), StackTag.algorithm.name(), TwitterAccountEnum.BestAlgorithms.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagAWS_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestAWS), StackTagAdvanced.amazonwebservices, TwitterAccountEnum.BestAWS.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagApple_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.InTheAppleWorld), StackTag.apple.name(), TwitterAccountEnum.InTheAppleWorld.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagClojure_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestClojure), StackTag.clojure.name(), TwitterAccountEnum.BestClojure.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagEclipse_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.EclipseFacts), StackTag.eclipse.name(), TwitterAccountEnum.EclipseFacts.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagFacebook_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.FacebookDigest), StackTag.facebook.name(), TwitterAccountEnum.FacebookDigest.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagGit_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestGit), StackTag.git.name(), TwitterAccountEnum.BestGit.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagGoogle_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.GoogleDigest), StackTag.google.name(), TwitterAccountEnum.GoogleDigest.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagHibernate_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.HibernateDaily), StackTag.hibernate.name(), TwitterAccountEnum.HibernateDaily.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagJava_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.JavaTopSO), StackTag.java.name(), TwitterAccountEnum.JavaTopSO.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagJquery_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.jQueryDaily), StackTag.jquery.name(), TwitterAccountEnum.jQueryDaily.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagJPA_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestJPA), StackTag.jpa.name(), TwitterAccountEnum.BestJPA.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagMaven_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestMaven), StackTag.maven.name(), TwitterAccountEnum.BestMaven.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagREST_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.RESTDaily), StackTag.rest.name(), TwitterAccountEnum.RESTDaily.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagScala_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.BestScala), StackTag.scala.name(), TwitterAccountEnum.BestScala.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByTagWordpress_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.LandOfWordpress), StackTag.wordpress.name(), TwitterAccountEnum.LandOfWordpress.name());
        assertTrue(success);
    }

    // by tag

    @Test
    public final void whenTweetingByRandomTag_thenNoExceptions() throws JsonProcessingException, IOException {
        final StackSite randomSite = GenericUtil.pickOneGeneric(twitterAccountToStackSites(TwitterAccountEnum.BestBash));
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(randomSite, StackTag.bash.name(), TwitterAccountEnum.BestBash.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByDefaultObjectiveCDailyTag_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = instance.tweetAnyTopQuestionBySiteAndTag(twitterAccountToStackSite(TwitterAccountEnum.ObjectiveCDaily), TwitterAccountEnum.ObjectiveCDaily.name());
        assertTrue(success);
    }

    // AskUbuntu

    @Test
    public final void whenTweetingOnAskUbuntu_thenNoExceptions() throws JsonProcessingException, IOException {
        instance.tweetAnyTopQuestionBySite(twitterAccountToStackSite(TwitterAccountEnum.AskUbuntuBest), TwitterAccountEnum.AskUbuntuBest.name(), 1);
    }

    @Test
    public final void whenTweetingByDefaultTagOnBestBash_thenNoExceptions() throws JsonProcessingException, IOException {
        instance.tweetAnyTopQuestionBySiteAndTag(StackSite.AskUbuntu, StackTag.bash.name(), TwitterAccountEnum.AskUbuntuBest.name());
    }

    // tweet individual SO question

    // failing - not sure why (will post on the Spring Security Forum soon)
    @Test
    public final void whenTweetingIndividualQuestion_thenNoExceptions() throws JsonProcessingException, IOException {
        final String questionById = questionsApi.questionById(StackSite.StackOverflow, 16621738);
        final ArrayNode singleQuestionAsArray = (ArrayNode) new ObjectMapper().readTree(questionById).get("items");
        Preconditions.checkState(singleQuestionAsArray.size() <= 1);
        final JsonNode singleQuestionAsJson = singleQuestionAsArray.get(0);
        final String questionId = singleQuestionAsJson.get(QuestionsApi.QUESTION_ID).toString();
        final String title = singleQuestionAsJson.get(QuestionsApi.TITLE).toString();
        final String link = singleQuestionAsJson.get(QuestionsApi.LINK).toString();

        final boolean success = instance.tryTweetOnePrepare(title, link, questionId, StackSite.StackOverflow, TwitterAccountEnum.RegexDaily.name());
        assertTrue(success);
    }

}
