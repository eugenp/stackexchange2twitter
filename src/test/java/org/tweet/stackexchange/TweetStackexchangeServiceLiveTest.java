package org.tweet.stackexchange;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.api.constants.Site;
import org.tweet.spring.ContextConfig;
import org.tweet.spring.PersistenceJPAConfig;
import org.tweet.spring.StackexchangeConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.stackexchange.util.SimpleTwitterAccount;
import org.tweet.stackexchange.util.StackexchangeUtil;
import org.tweet.stackexchange.util.Tag;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TwitterConfig.class, ContextConfig.class, PersistenceJPAConfig.class, StackexchangeConfig.class })
public class TweetStackexchangeServiceLiveTest {

    @Autowired
    private TweetStackexchangeService tweetStackexchangeService;

    // tests

    @Test
    public final void whenTweeting_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySite(Site.stackoverflow, SimpleTwitterAccount.SpringAtSO.name(), 1);
    }

    @Test
    public final void whenTweetingByTag_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.spring.name(), SimpleTwitterAccount.SpringAtSO.name(), 1);
    }

    @Test
    public final void whenTweetingByTagJava_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.java.name(), SimpleTwitterAccount.JavaTopSO.name(), 1);
    }

    @Test
    public final void whenTweetingByTagClojure_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.clojure.name(), SimpleTwitterAccount.BestClojure.name(), 1);
    }

    @Test
    public final void whenTweetingByTagScala_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.scala.name(), SimpleTwitterAccount.BestScala.name(), 1);
    }

    @Test
    public final void whenTweetingByTagJquery_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.jquery.name(), SimpleTwitterAccount.jQueryDaily.name(), 1);
    }

    @Test
    public final void whenTweetingByTagREST_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.rest.name(), SimpleTwitterAccount.RESTDaily.name(), 1);
    }

    @Test
    public final void whenTweetingByTagEclipse_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.eclipse.name(), SimpleTwitterAccount.BestEclipse.name(), 1);
    }

    @Test
    public final void whenTweetingByTagGit_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.git.name(), SimpleTwitterAccount.BestGit.name(), 1);
    }

    @Test
    public final void whenTweetingByTagMaven_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.maven.name(), SimpleTwitterAccount.BestMaven.name(), 1);
    }

    @Test
    public final void whenTweetingByTagJPA_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.jpa.name(), SimpleTwitterAccount.BestJPA.name(), 1);
    }

    @Test
    public final void whenTweetingByTagAlgorithm_thenNoExceptions() throws JsonProcessingException, IOException {
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(Site.stackoverflow, Tag.algorithm.name(), SimpleTwitterAccount.BestAlgorithms.name(), 1);
    }

    @Test
    public final void whenTweetingByRandomTag_thenNoExceptions() throws JsonProcessingException, IOException {
        final Site randomSite = StackexchangeUtil.pickOne(Site.stackoverflow, Site.askubuntu, Site.superuser);
        tweetStackexchangeService.tweetTopQuestionBySiteAndTag(randomSite, Tag.bash.name(), SimpleTwitterAccount.BestBash.name(), 1);
    }

    // list tweets

    @Test
    public final void whenListingTweets_thenNoExceptions() throws JsonProcessingException, IOException {
        final List<String> tweets = tweetStackexchangeService.listTweets(SimpleTwitterAccount.JavaTopSO.name());
        System.out.println(tweets);
    }

    // util

}
