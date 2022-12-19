package org.tweet.meta.service;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.classification.spring.ClassificationConfig;
import org.common.spring.CommonPersistenceJPAConfig;
import org.common.spring.CommonServiceConfig;
import org.common.spring.MyApplicationContextInitializerProv;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.meta.spring.TwitterMetaConfig;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.util.SpringProfileUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {// @formatter:off
        KeyValPersistenceJPAConfig.class, 
        
        CommonPersistenceJPAConfig.class, 
        CommonServiceConfig.class, 
        
        ClassificationConfig.class,
        
        TwitterConfig.class, 
        
        TwitterMetaPersistenceJPAConfig.class, 
        TwitterMetaConfig.class 
}) // @formatter:on
@ActiveProfiles({ SpringProfileUtil.PERSISTENCE })
public class TweetMetaLocalServiceManualTest {

    static {
        System.setProperty(MyApplicationContextInitializerProv.PERSISTENCE_TARGET_KEY, "prod");
    }

    @Autowired
    private TweetMetaLocalService service;

    @Autowired
    private IRetweetJpaDAO retweetApi;

    // tests

    @Test
    public final void whenContextIsInitialized_thenNoExceptions() {
        //
    }

    // has already been tweeted

    @Test
    public final void whenCheckingIfSomethingHasAlreadyBeenRetrweetedScenario1_thenCorrectAnswer() {
        final String text = "RT @vmbrasseur: This regex MUST become a t-shirt. #osb13 #perl #biking / Credit to @nickpatch http://pic.twitter.com/OrOCsWL2BC";
        final Retweet existing = service.findLocalCandidateAdvanced(text, TwitterAccountEnum.PerlDaily.name());
        assertNotNull(existing);
    }

    @Test
    public final void whenCheckingIfSomethingHasAlreadyBeenRetrweetedScenario2_thenCorrectAnswer() {
        final String text = "Blogged: #Scala #Redis client goes non blocking : uses #Akka IO .. http://debasishg.blogspot.in/2013/07/scala-redis-client-goes-non-blocking.html �";
        final Retweet existing = service.findLocalCandidateAdvanced(text, TwitterAccountEnum.ScalaFact.name());
        assertNotNull(existing);
    }

    @Test
    public final void whenCheckingIfSomethingHasAlreadyBeenRetrweetedScenario3_thenCorrectAnswer() {
        final String text = "Recipes for #Akka Dependency Injection - http://tmblr.co/ZlHOLwq7PxvL  by @typesafe";
        final Retweet existing = service.findLocalCandidateAdvanced(text, TwitterAccountEnum.ScalaFact.name());
        assertNotNull(existing);
    }

    @Test
    public final void whenCheckingIfSomethingHasAlreadyBeenRetrweetedScenario4_thenCorrectAnswer() {
        final String text = "#OOCSS + #Sass = The best way to #CSS - http://buff.ly/198JbJv  #webdev #html";
        final Retweet existing = service.findLocalCandidateAdvanced(text, TwitterAccountEnum.HTMLdaily.name());
        assertNotNull(existing);
    }

    @Test
    public final void whenCheckingIfSomethingHasAlreadyBeenRetrweetedScenario5_thenCorrectAnswer() {
        final String text = "Morning all! RT @ContractHire http://t.co/dxKq3cl03U follow and you could win a new #iPad Mini! #comp #Apple #ipad";
        final Retweet existing = service.findLocalCandidateAdvanced(text, TwitterAccountEnum.InTheAppleWorld.name());
        assertNotNull(existing);
    }

    @Test
    public final void whenCheckingIfSomethingHasAlreadyBeenRetrweetedScenario6_thenCorrectAnswer() {
        final String text = "Five Tips to Improve Your #AWS Security by @Cloud_Optimize ▸ http://t.co/BAMMqlxNUc #Cloud #CloudComputing";
        final Retweet existing = service.findLocalCandidateAdvanced(text, TwitterAccountEnum.BestOfCloud.name());
        assertNotNull(existing);
    }

    @Test
    public final void whenCheckingIfSomethingHasAlreadyBeenRetrweetedScenario7_thenCorrectAnswer() {
        final String text = "You Can Now Embed Vine Videos on Your #WordPress Site http://t.co/wKygWrfDFN";
        final Retweet existing = service.findLocalCandidateAdvanced(text, TwitterAccountEnum.LandOfWordpress.name());
        assertNotNull(existing);
    }

    @Test
    public final void whenCheckingIfSomethingHasAlreadyBeenRetrweetedScenario8_thenCorrectAnswer() {
        final String text = "Blogged: #Scala #Redis client goes non blocking : uses #Akka IO .. http://t.co/Q3pB4KPeqb";
        final Retweet existing = service.findLocalCandidateAdvanced(text, TwitterAccountEnum.ScalaFact.name());
        assertNotNull(existing);
    }

    @Test
    public final void whenCheckingIfSomethingHasAlreadyBeenRetrweetedScenario9_thenCorrectAnswer() {
        final String text = "Want to #Google the '#halal' way? Search engine blocks un-Islamic content http://t.co/ymBl4Ded0S via @AlArabiya_Eng";
        final Retweet existing = service.findLocalCandidateAdvanced(text, TwitterAccountEnum.GoogleDigest.name());
        assertNotNull(existing);
    }

    @Test
    public final void whenCheckingIfSomethingHasAlreadyBeenRetrweetedScenario10_thenCorrectAnswer() {
        final String text = "FRESH: Skillscast (film,code,slides) for @rstoya05's Intro to #WebSocket with #Spring4 is at http://ow.ly/qKhmG #springx #java #spring";
        final List<Retweet> localCandidates = service.findLocalCandidatesRelaxed(text, TwitterAccountEnum.SpringTip.name());
        assertThat(localCandidates, not(emptyIterable()));
    }

    @Test
    public final void whenCheckingIfSomethingHasAlreadyBeenRetrweetedScenario11_thenCorrectAnswer() {
        final String text = "FRESH: Skillscast (film,code,slides) for @springjuergen's keynote talk on #Spring4 & #Java8 is available at http://ow.ly/qKgkH #springx";
        final List<Retweet> localCandidates = service.findLocalCandidatesRelaxed(text, TwitterAccountEnum.SpringTip.name());
        assertThat(localCandidates, not(emptyIterable()));
    }

    // other

    @Test
    public final void whenRetrievingCorrespondingTweets1_thenCorrect() {
        final String tweet = "Announcing causatum 0.1.0, a #clojure library for generating event streams based on stochastic state machines. http://t.co.";
        final List<Retweet> correspondingLocalRetweets = service.findLocalCandidatesStrict(tweet, TwitterAccountEnum.ClojureFact.name());
        assertThat(correspondingLocalRetweets, hasSize(1));
    }

    @Test
    public final void whenRetrievingCorrespondingTweets2_thenCorrect() {
        final String tweet = "Domo makes the JMP Securities \"Hot 100\" list again http://t.co/Fk4682UML8 #cloud #Saas";
        final List<Retweet> correspondingLocalRetweets = service.findLocalCandidatesStrict(tweet, TwitterAccountEnum.BestOfCloud.name());
        assertThat(correspondingLocalRetweets, hasSize(1));
    }

}
