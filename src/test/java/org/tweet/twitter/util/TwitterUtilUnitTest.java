package org.tweet.twitter.util;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.classification.util.ClassificationSettings;
import org.junit.Test;
import org.tweet.twitter.service.TweetService;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public final class TwitterUtilUnitTest {

    // tests

    // isTweetTextValid

    @Test
    public final void givenValidTweetText_whenCheckingForValidity_thenIsValid() {
        assertTrue(TwitterUtil.isTweetTextWithoutLinkValid(randomAlphabetic(12)));
    }

    @Test
    public final void givenInvalidTweetText_whenCheckingForValidity_thenIsNotValid() {
        assertFalse(TwitterUtil.isTweetTextWithoutLinkValid(randomAlphabetic(141)));
    }

    @Test
    public final void givenTweetTextWithMoreThanOneLink_whenCheckingForValidity_thenIsNotValid() {
        assertFalse(TwitterUtil.isTweetTextWithoutLinkValid("Unit Testing JavaScript/JQuery in an http://t.co/410T1muptv MVC Project using QUnit: http://t.co/iuLIhnpGA1 #javascript #jquery"));
    }

    // isTweetValid

    @Test
    public final void givenValidTweet_whenCheckingForValidity_thenIsValid() {
        assertTrue(TwitterUtil.isTweetTextWithLinkValid(randomAlphabetic(12)));
    }

    @Test
    public final void givenValidTweetLarge_whenCheckingForValidity_thenIsValid() {
        assertTrue(TwitterUtil.isTweetTextWithLinkValid(randomAlphabetic(142)));
    }

    @Test
    public final void givenInvalidTweet_whenCheckingForValidity_thenIsNotValid() {
        assertFalse(TwitterUtil.isTweetTextWithLinkValid(randomAlphabetic(143)));
    }

    // isRejectedByContainsKeywordMaybe

    @Test
    public final void givenValidTweet1_whenCheckingIfTweetIsRejectedByKeywordMaybe_thenNo() {
        final String originalTweet = "@snipeyhead killed it at Ignite Food Camp “Failing Well: Managing Risk in High-Performance Applications”  https://t.co/9UjRq6hw1X";
        final List<String> tweetTokens = Lists.newArrayList(Splitter.on(CharMatcher.anyOf(ClassificationSettings.TWEET_TOKENIZER + "#")).split(originalTweet));

        assertFalse(TwitterUtil.isRejectedByContainsKeywordMaybeForAnalysis(tweetTokens, originalTweet));
    }

    @Test
    public final void givenValidTweet2_whenCheckingIfTweetIsRejectedByKeywordMaybe_thenNo() {
        final String originalTweet = "Win-Win Mobility webcast today at 2PM EDT from IDG, IBM, Lenovo and Intel http://t.co/tk1g6Uz0cI … #IBMMobile";
        final List<String> tweetTokens = Lists.newArrayList(Splitter.on(CharMatcher.anyOf(ClassificationSettings.TWEET_TOKENIZER + "#")).split(originalTweet));

        assertFalse(TwitterUtil.isRejectedByContainsKeywordMaybeForAnalysis(tweetTokens, originalTweet));
    }

    // prepareTweet

    @Test
    public final void givenTextAndLink_whenPreparindTweet_thenNoExceptions() {
        new TweetService().constructTweetSimple(randomAlphabetic(119), randomAlphabetic(19));
    }

    // truncate

    // isTweetBannedForTweeting - TwitterUtilBannedUnitTest

    // by regex - reject - TwitterUtilBannedForAnalysisByRegexUnitTest

    // by regex - accept

    @Test
    public final void givenTweetDoesNotContainBannedExpression_whenCheckingScenario1_thenAccepted() { // the second is not a RT
        assertFalse(TwitterUtil.isRejectedByBannedRegexExpressionsForAnalysis("RT @BETAwards: some text - if you want one -RTell"));
    }

    @Test
    public final void givenTweetDoesNotContainBannedExpression_whenCheckingScenario2_thenAccepted() { // the first is not a RT
        assertFalse(TwitterUtil.isRejectedByBannedRegexExpressionsForAnalysis("RTdfd @BETAwards: some text - if you want one -RT"));
    }

    @Test
    public final void givenTweetContainsBannedExpression_whenCheckingScenario4_thenAccepted() { // free without the RT
        assertFalse(TwitterUtil.isRejectedByBannedRegexExpressionsForAnalysis("3 Words: FREE. #IPAD. MINI ----> http://bet.us/14agHXR"));
    }

    //

    @Test
    public final void givenTweetContainsBannedExpression_whenCheckingScenario3_thenAccepted() {
        assertFalse(TwitterUtil.isTweetBannedForTweeting("25+ Best and Free jQuery Image Slider / Galleries - Pixaza http://t.co/OyHH4ZPm8B #jquery"));
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
        final String expected = "Great initiative \"@GetEventStore: #akka native client in progress ;-) https://t.co/gL1PBDVXVa its not close to ready but give u";
        final String resultTweet = TwitterUtil.extractTweetFromRt("RT @jboner: Great initiative \"@GetEventStore: #akka native client in progress ;-) https://t.co/gL1PBDVXVa its not close to ready but give u");
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
        final String expected = "#Akka 2.2 Spotlight: Publish/Subscribe in Cluster… http://t.co/ps3ZUrJnaq";
        final String resultTweet = TwitterUtil.extractTweetFromRt("#Akka 2.2 Spotlight: Publish/Subscribe in Cluster… http://t.co/ps3ZUrJnaq");
        assertThat(resultTweet, equalTo(expected));
    }

    @Test
    public final void whenExtractingTweetOutOfRtScenario15_thenCorrectlyExtracted() {
        final String expected = "Tizen 2.2 SDK Released | Tizen Experts http://tizen.ly/13YbYjb  #HTML5 #Linux ";
        final String resultTweet = TwitterUtil.extractTweetFromRt("Tizen 2.2 SDK Released | Tizen Experts http://tizen.ly/13YbYjb  #HTML5 #Linux ");
        assertThat(resultTweet, equalTo(expected));
    }

    @Test
    public final void whenExtractingTweetOutOfRtScenario16_thenCorrectlyExtracted() {
        final String expected = "#Akka 2.2 Spotlight: Publish/Subscribe in Cluster… http://t.co/ps3ZUrJnaq";
        final String resultTweet = TwitterUtil.extractTweetFromRt("#Akka 2.2 Spotlight: Publish/Subscribe in Cluster… http://t.co/ps3ZUrJnaq - RT @akkateam");
        assertThat(resultTweet, equalTo(expected));
    }

}
