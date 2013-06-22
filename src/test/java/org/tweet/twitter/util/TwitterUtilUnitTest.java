package org.tweet.twitter.util;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.stackexchange.util.Tag;

import com.google.common.collect.Lists;

public final class TwitterUtilUnitTest {

    // tests

    // isTweetTextValid

    @Test
    public final void givenValidTweetText_whenCheckingForValidity_thenIsValid() {
        assertTrue(TwitterUtil.isTweetTextValid(randomAlphabetic(12)));
    }

    @Test
    public final void givenInvalidTweetText_whenCheckingForValidity_thenIsNotValid() {
        assertFalse(TwitterUtil.isTweetTextValid(randomAlphabetic(141)));
    }

    // isTweetValid

    @Test
    public final void givenValidTweet_whenCheckingForValidity_thenIsValid() {
        assertTrue(TwitterUtil.isTweetValid(randomAlphabetic(12)));
    }

    @Test
    public final void givenInvalidTweet_whenCheckingForValidity_thenIsNotValid() {
        assertFalse(TwitterUtil.isTweetValid(randomAlphabetic(141)));
    }

    // prepareTweet

    @Test
    public final void givenTextAndLink_whenPreparindTweet_thenNoExceptions() {
        TwitterUtil.prepareTweet(randomAlphabetic(119), randomAlphabetic(19));
    }

    // pre-process

    @Test
    public final void givenTweet_whenPreProcessingTweet_thenNoExceptions() {
        TwitterUtil.hashtagWords(randomAlphabetic(50), Lists.<String> newArrayList());
    }

    @Test
    public final void givenNoWordsToHash_whenPreProcessingTweet_thenNoChanges() {
        final String originalTweet = randomAlphabetic(50);
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.<String> newArrayList());
        assertThat(originalTweet, equalTo(processedTweet));
    }

    @Test
    public final void givenSomeWordsToHash_whenTweetDoesNotContainTheWord_thenNoChanges() {
        final String originalTweet = "word1 word2 word3";
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList("otherword"));
        assertThat(originalTweet, equalTo(processedTweet));
    }

    @Test
    public final void givenSomeWordsToHash_whenTweetDoesContainTheWord_thenTweetChanged1() {
        final String originalTweet = "word1 word2 word3";
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList("word2"));
        assertThat(originalTweet, not(equalTo(processedTweet)));
    }

    @Test
    public final void givenSomeWordsToHash_whenTweetDoesContainTheWord_thenTweetChanged2() {
        final String originalTweet = "Testing with Guava";
        final String targetResultTweet = "Testing with #Guava";
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList("guava"));
        assertThat(targetResultTweet, equalTo(processedTweet));
    }

    @Test
    @Ignore("ignored for now - the max lenght functionality was disabled because it was counting to many characters in the link")
    public final void givenSomeWordsToHash_whenTweetIsToLongToHash_thenNoChange() {
        final String prefix = randomAlphabetic(122);
        final String originalTweet = prefix + "Testing with Guava";
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList("guava"));
        assertThat(originalTweet, equalTo(processedTweet));
    }

    @Test
    public final void givenRealCaseScenario1_whenHashtagsAreAdded_thenCorrect() {
        final String originalTweet = "Hibernate + Spring using multiple datasources? - http://stackoverflow.com/questions/860918/hibernate-spring-using-multiple-datasources";
        final String targetTweet = "Hibernate + #Spring using multiple datasources? - http://stackoverflow.com/questions/860918/hibernate-spring-using-multiple-datasources";
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList("spring"));
        assertThat(targetTweet, equalTo(processedTweet));
    }

    @Test
    public final void givenRealCaseScenario2_whenHashtagsAreAdded_thenCorrect() {
        final String originalTweet = "How are Anonymous (inner) classes used in Java? - http://stackoverflow.com/questions/355167/how-are-anonymous-inner-classes-used-in-java";
        final String targetTweet = "How are Anonymous (inner) classes used in #Java? - http://stackoverflow.com/questions/355167/how-are-anonymous-inner-classes-used-in-java";
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList("java"));
        assertThat(targetTweet, equalTo(processedTweet));
    }

    @Test
    public final void givenRealCaseScenario3_whenHashtagsAreAdded_thenCorrect() {
        final String originalTweet = "What's the best Web interface for Git repositories? - http://stackoverflow.com/questions/438163/whats";
        final String targetTweet = "What's the best Web interface for #Git repositories? - http://stackoverflow.com/questions/438163/whats";
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList("git"));
        assertThat(targetTweet, equalTo(processedTweet));
    }

    @Test
    public final void givenRealCaseScenario4_whenHashtagsAreAdded_thenCorrect() {
        final String originalTweet = "Programmatically showing a View from an Eclipse Plug-in - http://stackoverflow.com/questions/171824/programmatically-showing";
        final String targetTweet = "Programmatically showing a View from an #Eclipse Plug-in - http://stackoverflow.com/questions/171824/programmatically-showing";
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList("eclipse"));
        assertThat(targetTweet, equalTo(processedTweet));
    }

    @Test
    public final void givenRealCaseScenario5_whenHashtagsAreAdded_thenCorrect() {
        final String originalTweet = "Static return type of Scala macros - http://stackoverflow.com/questions/13669974/static-return-type-of-scala-macros";
        final String targetTweet = "Static return type of #Scala macros - http://stackoverflow.com/questions/13669974/static-return-type-of-scala-macros";
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList("scala"));
        assertThat(targetTweet, equalTo(processedTweet));
    }

    @Test
    public final void givenRealCaseScenario6_whenHashtagsAreAdded_thenCorrect() {
        final String originalTweet = "Tweet: 25+ Highly Useful #jQuery Plugins Bringing Life back to HTML Tables http://t.co/smTFbBO8l4";
        final String targetTweet = "Tweet: 25+ Highly Useful #jQuery Plugins Bringing Life back to HTML Tables http://t.co/smTFbBO8l4";
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList("jquery"));
        assertThat(targetTweet, equalTo(processedTweet));
    }

    @Test
    public final void givenRealCaseScenario7_whenHashtagsAreAdded_thenCorrect() {
        final String originalTweet = "Best Way to Inject Hibernate Session by Spring 3 - http://stackoverflow.com/questions/4699381/best-way-to-inject-hibernate-session-by-spring-3";
        final String targetTweet = "Best Way to Inject #Hibernate Session by #Spring 3 - http://stackoverflow.com/questions/4699381/best-way-to-inject-hibernate-session-by-spring-3";
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList("spring", "hibernate"));
        assertThat(targetTweet, equalTo(processedTweet));
    }

    @Test
    public final void givenRealCaseScenario8_whenHashtagsAreAdded_thenCorrect() {
        final String originalTweet = "Problem installing Maven plugin (m2eclipse) in Eclipse (Galileo) - http://stackoverflow.com/questions/2802";
        final String targetTweet = "Problem installing #Maven plugin (m2eclipse) in Eclipse (Galileo) - http://stackoverflow.com/questions/2802";
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList("maven"));
        assertThat(targetTweet, equalTo(processedTweet));
    }

    @Test
    public final void givenRealCaseScenario9_whenHashtagsAreAdded_thenCorrect() {
        final String originalTweet = "\"string could not resolved\" error in eclipse for C++ - http://stackoverflow.com/questions/7905025/string-could-not-resolved-error-in-eclipse-for-c";
        final String targetTweet = "\"string could not resolved\" error in #eclipse for C++ - http://stackoverflow.com/questions/7905025/string-could-not-resolved-error-in-eclipse-for-c";
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList("eclipse"));
        assertThat(targetTweet, equalTo(processedTweet));
    }

    @Test
    @Ignore("corner case")
    public final void givenRealCaseScenario10_whenHashtagsAreAdded_thenCorrect() {
        final String originalTweet = "Understanding EJB3/JPA container-level transactions and isolation level - http://stackoverflow.com/questions/4136852/understanding-ejb3-jpa-container-level-transactions-and-isolation-level";
        final String targetTweet = "Understanding #EJB3/#JPA container-level transactions and isolation level - http://stackoverflow.com/questions/4136852/understanding-ejb3-jpa-container-level-transactions-and-isolation-level";
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList("ejb3", "jpa"));
        assertThat(targetTweet, equalTo(processedTweet));
    }

    @Test
    public final void givenRealCaseScenario11_whenHashtagsAreAdded_thenCorrect() {
        final String originalTweet = "Fast vector math in #Clojure / Incanter - http://stackoverflow.com/questions/3814048/fast-vector-math-in-clojure-incanter";
        final String targetTweet = "Fast vector math in #Clojure / #Incanter - http://stackoverflow.com/questions/3814048/fast-vector-math-in-clojure-incanter";
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList(Tag.clojure.name(), "incanter"));
        assertThat(targetTweet, equalTo(processedTweet));
    }

    @Test
    public final void givenRealCaseScenario12_whenHashtagsAreAdded_thenCorrect() {
        final String originalTweet = "Hot deploying changes with Netbeans, Maven, and Glassfish - http://stackoverflow.com/questions/2290935/hot-deploying-changes-with-netbeans-maven-and-glassfish";
        final String targetTweet = "Hot deploying changes with Netbeans, #Maven, and Glassfish - http://stackoverflow.com/questions/2290935/hot-deploying-changes-with-netbeans-maven-and-glassfish";
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList("maven", "pom"));
        assertThat(targetTweet, equalTo(processedTweet));
    }

    //

    // tweetContainsBannedKeywords

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario1_thenRejected() {
        assertTrue(TwitterUtil.tweetContainsBannedKeywords("Checkout this cool job - some job"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario2_thenRejected() {
        assertTrue(TwitterUtil.tweetContainsBannedKeywords("I need a free app to do that"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario3_thenRejected() {
        assertTrue(TwitterUtil.tweetContainsBannedKeywords("I need a app to do that; it's gotta be free!"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario4_thenRejected() {
        assertTrue(TwitterUtil.tweetContainsBannedKeywords("Telecommuting job: Full Time Python/Plone Developer at Decernis #jquery #java #javascript #python #perl http://t.co/580g7g8Hum"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario5_thenRejected() {
        assertTrue(TwitterUtil.tweetContainsBannedKeywords("Tweet: #freelance #jquery #job - a teacher to teach me how to solve my jQuery AJAX CSS issue ($2 - 8/hr) - http://t.co/WaiAHsgZ8a #jobs"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario6_thenRejected() {
        assertTrue(TwitterUtil.tweetContainsBannedKeywords("Tweet: Javascript Application Engineer http://t.co/WuYi4HiGhP #jquery #html5 #jobs #hiring #careers"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario7_thenRejected() {
        assertTrue(TwitterUtil.tweetContainsBannedKeywords("Tweet: #jquery #job - ColdFusion Web Developer - http://t.co/O2DbDyFqea #jobs"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario8_thenRejected() {
        assertTrue(TwitterUtil.tweetContainsBannedKeywords("Tweet: RT @MarianSchubert: &lt;- Looking for a job. #lean #kanban #scrum #ci #cd #clojure #ios #perl #python #php #fp #tdd #unix #erlang #xp #pairpro�"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario9_thenRejected() {
        assertTrue(TwitterUtil.tweetContainsBannedKeywords("love open source and have a passion for #git? come help us make it better for everyone, we are hiring! http://t.co/7hk7fqgEN1"));
    }

    // accepted

    @Test
    public final void givenTweetDoesNotContainBannedKeywords_whenCheckingScenario1_thenAccepted() {
        assertFalse(TwitterUtil.tweetContainsBannedKeywords("25+ Best and Free jQuery Image Slider / Galleries - Pixaza http://t.co/OyHH4ZPm8B #jquery"));
    }

}
