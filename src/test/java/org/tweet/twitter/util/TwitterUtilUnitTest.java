package org.tweet.twitter.util;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.stackexchange.util.TwitterTag;
import org.tweet.twitter.service.TweetService;

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

    @Test
    public final void givenTweetTextWithMoreThanOneLink_whenCheckingForValidity_thenIsNotValid() {
        assertFalse(TwitterUtil.isTweetTextValid("Unit Testing JavaScript/JQuery in an http://t.co/410T1muptv MVC Project using QUnit: http://t.co/iuLIhnpGA1 #javascript #jquery"));
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
        new TweetService().constructTweetSimple(randomAlphabetic(119), randomAlphabetic(19));
    }

    // truncate

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
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList(TwitterTag.clojure.name(), "incanter"));
        assertThat(targetTweet, equalTo(processedTweet));
    }

    @Test
    public final void givenRealCaseScenario12_whenHashtagsAreAdded_thenCorrect() {
        final String originalTweet = "Hot deploying changes with Netbeans, Maven, and Glassfish - http://stackoverflow.com/questions/2290935/hot-deploying-changes-with-netbeans-maven-and-glassfish";
        final String targetTweet = "Hot deploying changes with Netbeans, #Maven, and Glassfish - http://stackoverflow.com/questions/2290935/hot-deploying-changes-with-netbeans-maven-and-glassfish";
        final String processedTweet = TwitterUtil.hashtagWords(originalTweet, Lists.newArrayList("maven", "pom"));
        assertThat(targetTweet, equalTo(processedTweet));
    }

    // tweetContainsBannedKeywords

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario1_thenRejected() {
        assertTrue(TwitterUtil.isTweetBanned("Checkout this cool job - some job"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario2_thenRejected() {
        assertTrue(TwitterUtil.isTweetBanned("I need a free app to do that"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario3_thenRejected() {
        assertTrue(TwitterUtil.isTweetBanned("I need a app to do that; it's gotta be free!"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario4_thenRejected() {
        assertTrue(TwitterUtil.isTweetBanned("Telecommuting job: Full Time Python/Plone Developer at Decernis #jquery #java #javascript #python #perl http://t.co/580g7g8Hum"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario5_thenRejected() {
        assertTrue(TwitterUtil.isTweetBanned("Tweet: #freelance #jquery #job - a teacher to teach me how to solve my jQuery AJAX CSS issue ($2 - 8/hr) - http://t.co/WaiAHsgZ8a #jobs"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario6_thenRejected() {
        assertTrue(TwitterUtil.isTweetBanned("Tweet: Javascript Application Engineer http://t.co/WuYi4HiGhP #jquery #html5 #jobs #hiring #careers"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario7_thenRejected() {
        assertTrue(TwitterUtil.isTweetBanned("Tweet: #jquery #job - ColdFusion Web Developer - http://t.co/O2DbDyFqea #jobs"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario8_thenRejected() {
        assertTrue(TwitterUtil.isTweetBanned("Tweet: RT @MarianSchubert: &lt;- Looking for a job. #lean #kanban #scrum #ci #cd #clojure #ios #perl #python #php #fp #tdd #unix #erlang #xp #pairpro�"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario9_thenRejected() {
        assertTrue(TwitterUtil.isTweetBanned("love open source and have a passion for #git? come help us make it better for everyone, we are hiring! http://t.co/7hk7fqgEN1"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario10_thenRejected() {
        assertTrue(TwitterUtil.isTweetBanned("Hire Experienced #WordPress developer at Affordable price, Inquire Now! http://www.valuecoders.com/hire-developers/hire-wordpress-developers"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario11_thenRejected() {
        assertTrue(TwitterUtil.isTweetBanned("Buy 42% off Secrets of the #JavaScript Ninja on Amazon http://amzn.to/12kkaUn  @jeresig"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario12_thenRejected() {
        assertTrue(TwitterUtil.isTweetBanned("Get 42% off Secrets of the #JavaScript Ninja on Amazon http://amzn.to/12kkaUn  @jeresig"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario13_thenRejected() {
        assertTrue(TwitterUtil.isTweetBanned("Follow us on Linkedin - http://linkd.in/V4Fxa5  #Android #iOS #PS3 #Xbox360 #Apps #GameDev #IDRTG #Video #Game #Developer"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario14_thenRejected() {
        assertTrue(TwitterUtil.isTweetBanned("I'm broadcasting #LIVE on #HangWith for #iPhone! Come Hang w/souljaboy! http://bit.ly/hangwsocial"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario15_thenRejected() {
        assertTrue(TwitterUtil.isTweetBanned("Are you a #python developer and want to collaborate? Help us testing #libturpial http://turpial.org.ve/2013/06/libturpial-1-0-frozen/ \u2026"));
    }

    @Test
    public final void givenTweetContainsBannedKeywords_whenCheckingScenario16_thenRejected() {
        assertTrue(TwitterUtil.isTweetBanned("Photo: #Akka is on strike! Stores closed pic.twitter.com/16iZBYKZBF #AngerStrike #StopPrawerPlan #\u0628\u0631\u0627\u0641\u0631_\u0644\u0646_\u064A\u0645\u0631"));
    }

    // accepted

    @Test
    public final void givenTweetDoesNotContainBannedKeywords_whenCheckingScenario1_thenAccepted() {
        assertFalse(TwitterUtil.isTweetBanned("25+ Best and Free jQuery Image Slider / Galleries - Pixaza http://t.co/OyHH4ZPm8B #jquery"));
    }

    // rt

    @Test
    public final void whenExtractingTweetOutOfRtScenario1_thenCorrectlyExtracted() {
        final String expected = "Tizen 2.2 SDK Released | Tizen Experts http://tizen.ly/13YbYjb  #HTML5 #Linux #Developer #WebApp #WebDev";
        final String resultTweet = TwitterUtil.extractTweetFromRt("RT @ashnazir: Tizen 2.2 SDK Released | Tizen Experts http://tizen.ly/13YbYjb  #HTML5 #Linux #Developer #WebApp #WebDev");
        assertThat(resultTweet, equalTo(expected));
    }

    @Test
    public final void whenExtractingTweetOutOfRtScenario2_thenCorrectlyExtracted() {
        final String expected = "Tizen 2.2 SDK Released | Tizen Experts http://tizen.ly/13YbYjb  #HTML5 #Linux #Developer #WebApp #WebDev";
        final String resultTweet = TwitterUtil.extractTweetFromRt("RT @ashnazir - Tizen 2.2 SDK Released | Tizen Experts http://tizen.ly/13YbYjb  #HTML5 #Linux #Developer #WebApp #WebDev");
        assertThat(resultTweet, equalTo(expected));
    }

    @Test
    public final void whenExtractingTweetOutOfRtScenario3_thenCorrectlyExtracted() {
        final String expected = "Tizen 2.2 SDK Released | Tizen Experts http://tizen.ly/13YbYjb  #HTML5 #Linux #Developer #WebApp #WebDev";
        final String resultTweet = TwitterUtil.extractTweetFromRt("RT @someone - Tizen 2.2 SDK Released | Tizen Experts http://tizen.ly/13YbYjb  #HTML5 #Linux #Developer #WebApp #WebDev");
        assertThat(resultTweet, equalTo(expected));
    }

    @Test
    public final void whenExtractingTweetOutOfRtScenario4_thenCorrectlyExtracted() {
        final String expected = "Tizen 2.2 SDK Released | Tizen Experts http://tizen.ly/13YbYjb  #HTML5 #Linux #Developer #WebApp #WebDev";
        final String resultTweet = TwitterUtil.extractTweetFromRt("RT @someone- Tizen 2.2 SDK Released | Tizen Experts http://tizen.ly/13YbYjb  #HTML5 #Linux #Developer #WebApp #WebDev");
        assertThat(resultTweet, equalTo(expected));
    }

    @Test
    public final void whenExtractingTweetOutOfRtScenario5_thenCorrectlyExtracted() {
        final String expected = "Tizen 2.2 SDK Released | Tizen Experts http://tizen.ly/13YbYjb  #HTML5 #Linux #Developer #WebApp #WebDev";
        final String resultTweet = TwitterUtil.extractTweetFromRt("RT @someone -Tizen 2.2 SDK Released | Tizen Experts http://tizen.ly/13YbYjb  #HTML5 #Linux #Developer #WebApp #WebDev");
        assertThat(resultTweet, equalTo(expected));
    }

    @Test
    public final void whenExtractingTweetOutOfRtScenario6_thenCorrectlyExtracted() {
        final String expected = "Humax unveils the world-first #HTML5 based smart set-top box http://t.co/6nuQJeHNSS #cabletv #digitaltv #korea";
        final String resultTweet = TwitterUtil.extractTweetFromRt("RT @digitaltvnews: Humax unveils the world-first #HTML5 based smart set-top box http://t.co/6nuQJeHNSS #cabletv #digitaltv #korea");
        assertThat(resultTweet, equalTo(expected));
    }

    @Test
    public final void whenExtractingTweetOutOfRtScenario7_thenCorrectlyExtracted() {
        final String expected = "Awesome #jQuery #HTML5 uploader by @MicheleBertoli http://t.co/cYVoDN6g3q";
        final String resultTweet = TwitterUtil.extractTweetFromRt("RT @maliki_borneo: Awesome #jQuery #HTML5 uploader by @MicheleBertoli http://t.co/cYVoDN6g3q");
        assertThat(resultTweet, equalTo(expected));
    }

    @Test
    public final void whenExtractingTweetOutOfRtScenario8_thenCorrectlyExtracted() {
        final String expected = "Awesome #jQuery #HTML5 uploader by @MicheleBertoli http://t.co/cYVoDN6g3q";
        final String resultTweet = TwitterUtil.extractTweetFromRt("RT @maliki_borneo: Awesome #jQuery #HTML5 uploader by @MicheleBertoli http://t.co/cYVoDN6g3q");
        assertThat(resultTweet, equalTo(expected));
    }

    @Test
    public final void whenExtractingTweetOutOfRtScenario9_thenCorrectlyExtracted() {
        final String expected = "New post: Weekly Update #25 - Getting closer to the milestone http://t.co/EWO3EqAzLo #crosscode #gamedev #html5 #javas";
        final String resultTweet = TwitterUtil.extractTweetFromRt("RT @RadicalFishGame: New post: Weekly Update #25 - Getting closer to the milestone http://t.co/EWO3EqAzLo #crosscode #gamedev #html5 #javas");
        assertThat(resultTweet, equalTo(expected));
    }

    @Test
    public final void whenExtractingTweetOutOfRtScenario10_thenCorrectlyExtracted() {
        final String expected = "1000's of working snippets and examples for Bootstrap http://t.co/Y7z8Gvbysa #bootstrap #css #webdev #html5";
        final String resultTweet = TwitterUtil.extractTweetFromRt("RT @bootply: 1000's of working snippets and examples for Bootstrap http://t.co/Y7z8Gvbysa #bootstrap #css #webdev #html5");
        assertThat(resultTweet, equalTo(expected));
    }

    @Test
    public final void whenExtractingTweetOutOfRtScenario11_thenCorrectlyExtracted() {
        final String expected = "Top 10 #HTML5 games http://t.co/s9L3m1hh6D";
        final String resultTweet = TwitterUtil.extractTweetFromRt("RT @OpenWebDevice: Top 10 #HTML5 games http://t.co/s9L3m1hh6D");
        assertThat(resultTweet, equalTo(expected));
    }

    @Test
    public final void whenExtractingTweetOutOfRtScenario12_thenCorrectlyExtracted() {
        final String expected = "Discover some tips about the #Javascript Conference in Spain  http://t.co/4JJp6uAJZQ #spainjs #html5 #advergame";
        final String resultTweet = TwitterUtil.extractTweetFromRt("RT @adverway: Discover some tips about the #Javascript Conference in Spain  http://t.co/4JJp6uAJZQ #spainjs #html5 #advergame");
        assertThat(resultTweet, equalTo(expected));
    }

    @Test
    public final void whenExtractingTweetOutOfRtScenario13_thenCorrectlyExtracted() {
        final String expected = "#Akka 2.2 Spotlight: Publish/Subscribe in Cluster… http://t.co/ps3ZUrJnaq";
        final String resultTweet = TwitterUtil.extractTweetFromRt("RT @akkateam: #Akka 2.2 Spotlight: Publish/Subscribe in Cluster… http://t.co/ps3ZUrJnaq");
        assertThat(resultTweet, equalTo(expected));
    }

    @Test
    public final void whenExtractingTweetOutOfRtScenario14_thenCorrectlyExtracted() {
        final String expected = "Great initiative \"@GetEventStore: #akka native client in progress ;-) https://t.co/gL1PBDVXVa its not close to ready but give u";
        final String resultTweet = TwitterUtil.extractTweetFromRt("RT @jboner: Great initiative \"@GetEventStore: #akka native client in progress ;-) https://t.co/gL1PBDVXVa its not close to ready but give u");
        assertThat(resultTweet, equalTo(expected));
    }

}
