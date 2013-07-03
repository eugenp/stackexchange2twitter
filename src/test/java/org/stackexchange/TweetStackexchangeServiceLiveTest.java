package org.stackexchange;

import static org.stackexchange.persistence.setup.TwitterAccountToStackAccount.twitterAccountToStackSite;
import static org.stackexchange.persistence.setup.TwitterAccountToStackAccount.twitterAccountToStackSites;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.api.constants.StackSite;
import org.stackexchange.service.TweetStackexchangeService;
import org.stackexchange.spring.StackexchangeConfig;
import org.stackexchange.spring.StackexchangeContextConfig;
import org.stackexchange.spring.StackexchangePersistenceJPAConfig;
import org.stackexchange.strategies.StackExchangePageStrategy;
import org.stackexchange.util.GenericUtil;
import org.stackexchange.util.SimpleTwitterAccount;
import org.stackexchange.util.Tag;
import org.tweet.spring.TwitterConfig;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TwitterConfig.class, StackexchangeContextConfig.class, StackexchangePersistenceJPAConfig.class, StackexchangeConfig.class })
public class TweetStackexchangeServiceLiveTest {

    @Autowired
    private TweetStackexchangeService tweetStackexchangeService;

    @Autowired
    private StackExchangePageStrategy pageStrategy;

    // tests

    @Test
    public final void whenContextIsInitalized_thenNoExceptions() {
        //
    }

    @Test
    public final void whenTweeting_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySite(twitterAccountToStackSite(SimpleTwitterAccount.SpringAtSO), SimpleTwitterAccount.SpringAtSO.name(), 1);
    }

    @Test
    public final void whenTweetingByTag_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.SpringAtSO), Tag.spring.name(), SimpleTwitterAccount.SpringAtSO.name(), 1);
    }

    @Test
    public final void whenTweetingByDefaultTag_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.SpringAtSO), SimpleTwitterAccount.SpringAtSO.name(), 1);
    }

    @Test
    public final void whenTweetingByTagJava_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.JavaTopSO), Tag.java.name(), SimpleTwitterAccount.JavaTopSO.name(), 1);
    }

    @Test
    public final void whenTweetingByTagClojure_thenNoExceptions() throws JsonProcessingException, IOException {
        final int pageToStartWith = pageStrategy.decidePage(SimpleTwitterAccount.BestClojure, Tag.clojure.name());
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestClojure), Tag.clojure.name(), SimpleTwitterAccount.BestClojure.name(), pageToStartWith);
    }

    @Test
    public final void whenTweetingByTagScala_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestScala), Tag.scala.name(), SimpleTwitterAccount.BestScala.name(), 1);
    }

    @Test
    public final void whenTweetingByTagJquery_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.jQueryDaily), Tag.jquery.name(), SimpleTwitterAccount.jQueryDaily.name(), 1);
    }

    @Test
    public final void whenTweetingByTagREST_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.RESTDaily), Tag.rest.name(), SimpleTwitterAccount.RESTDaily.name(), 1);
    }

    @Test
    public final void whenTweetingByTagEclipse_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestEclipse), Tag.eclipse.name(), SimpleTwitterAccount.BestEclipse.name(), 1);
    }

    @Test
    public final void whenTweetingByTagGit_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestGit), Tag.git.name(), SimpleTwitterAccount.BestGit.name(), 1);
    }

    @Test
    public final void whenTweetingByTagMaven_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestMaven), Tag.maven.name(), SimpleTwitterAccount.BestMaven.name(), 1);
    }

    @Test
    public final void whenTweetingByTagJPA_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestJPA), Tag.jpa.name(), SimpleTwitterAccount.BestJPA.name(), 1);
    }

    @Test
    public final void whenTweetingByTagAlgorithm_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestAlgorithms), Tag.algorithm.name(), SimpleTwitterAccount.BestAlgorithms.name(), 1);
    }

    @Test
    public final void whenTweetingByTagAWS_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestAWS), Tag.aws.name(), SimpleTwitterAccount.BestAWS.name(), 1);
    }

    @Test
    public final void whenTweetingByDefaultObjectiveCDailyTag_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.ObjectiveCDaily), SimpleTwitterAccount.ObjectiveCDaily.name(), 1);
    }

    @Test
    public final void whenTweetingByRandomTag_thenNoExceptions() throws JsonProcessingException, IOException {
        final StackSite randomSite = GenericUtil.pickOneGeneric(twitterAccountToStackSites(SimpleTwitterAccount.BestBash));
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(randomSite, Tag.bash.name(), SimpleTwitterAccount.BestBash.name(), 1);
    }

    // AskUbuntu

    @Test
    public final void whenTweetingOnAskUbuntu_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySite(twitterAccountToStackSite(SimpleTwitterAccount.AskUbuntuBest), SimpleTwitterAccount.AskUbuntuBest.name(), 1);
    }

}
