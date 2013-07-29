package org.tweet.twitter.util;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.tweet.twitter.service.TweetService;

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

    // prepareTweet

    @Test
    public final void givenTextAndLink_whenPreparindTweet_thenNoExceptions() {
        new TweetService().constructTweetSimple(randomAlphabetic(119), randomAlphabetic(19));
    }

    // truncate

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

    // reject by regex

    @Test
    public final void givenTweetContainsBannedExpression_whenCheckingScenario1_thenRejected() {
        assertTrue(TwitterUtil.isRejectedByBannedRegexExpressions("RT @BETAwards: 3 Words: FREE. #IPAD. MINI ----> http://bet.us/14agHXR  <--- RT if you want one!"));
    }

    @Test
    public final void givenTweetContainsBannedExpression_whenCheckingScenario2_thenRejected() {
        assertTrue(TwitterUtil.isRejectedByBannedRegexExpressions("RT @BETAwards: some text - if you want one -RT"));
    }

    @Test
    public final void givenTweetContainsBannedExpression_whenCheckingScenario3_thenRejected() {
        assertTrue(TwitterUtil.isRejectedByBannedRegexExpressions("3 Words: FREE. #IPAD. MINI ----> http://bet.us/14agHXR  <--- RT if you want one!"));
    }

    @Test
    public final void givenTweetContainsBannedExpression_whenCheckingScenario4_thenRejected() {
        assertTrue(TwitterUtil.isRejectedByBannedRegexExpressions("3 Words: Free. #IPAD. MINI ----> http://bet.us/14agHXR  <--- RT if you want one!"));
    }

    // accepted

    @Test
    public final void givenTweetContainsBannedExpression_whenCheckingScenario1_thenAccepted() {
        assertFalse(TwitterUtil.isRejectedByBannedRegexExpressions("RT @BETAwards: some text - if you want one -RTell"));
    }

    @Test
    public final void givenTweetContainsBannedExpression_whenCheckingScenario2_thenAccepted() {
        assertFalse(TwitterUtil.isRejectedByBannedRegexExpressions("RTdfd @BETAwards: some text - if you want one -RT"));
    }

    @Test
    public final void givenTweetContainsBannedExpression_whenCheckingScenario3_thenAccepted() {
        assertFalse(TwitterUtil.isTweetBanned("25+ Best and Free jQuery Image Slider / Galleries - Pixaza http://t.co/OyHH4ZPm8B #jquery"));
    }

    @Test
    public final void givenTweetContainsBannedExpression_whenCheckingScenario4_thenAccepted() {
        assertFalse(TwitterUtil.isRejectedByBannedRegexExpressions("3 Words: FREE. #IPAD. MINI ----> http://bet.us/14agHXR"));
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

    // rt - original user

    @Test
    public final void whenExtractingUserOutOfRtScenario1_thenCorrectlyExtracted() {
        final String expected = "ashnazir";
        final String result = TwitterUtil.extractOriginalUserFromRt("RT @ashnazir: Tizen 2.2 SDK Released | Tizen Experts http://tizen.ly/13YbYjb  #HTML5 #Linux #Developer #WebApp #WebDev");
        assertThat(result, equalTo(expected));
    }

    @Test
    public final void whenExtractingUserOutOfRtScenario2_thenCorrectlyExtracted() {
        final String expected = "ashnazir";
        final String result = TwitterUtil.extractOriginalUserFromRt("RT @ashnazir - Tizen 2.2 SDK Released | Tizen Experts http://tizen.ly/13YbYjb  #HTML5 #Linux #Developer #WebApp #WebDev");
        assertThat(result, equalTo(expected));
    }

    @Test
    public final void whenExtractingUserOutOfRtScenario3_thenCorrectlyExtracted() {
        final String expected = "someone";
        final String result = TwitterUtil.extractOriginalUserFromRt("RT @someone - Tizen 2.2 SDK Released | Tizen Experts http://tizen.ly/13YbYjb  #HTML5 #Linux #Developer #WebApp #WebDev");
        assertThat(result, equalTo(expected));
    }

    @Test
    public final void whenExtractingUserOutOfRtScenario4_thenCorrectlyExtracted() {
        final String expected = "someone";
        final String result = TwitterUtil.extractOriginalUserFromRt("RT @someone- Tizen 2.2 SDK Released | Tizen Experts http://tizen.ly/13YbYjb  #HTML5 #Linux #Developer #WebApp #WebDev");
        assertThat(result, equalTo(expected));
    }

    @Test
    public final void whenExtractingUserOutOfRtScenario5_thenCorrectlyExtracted() {
        final String expected = "someone";
        final String result = TwitterUtil.extractOriginalUserFromRt("RT @someone -Tizen 2.2 SDK Released | Tizen Experts http://tizen.ly/13YbYjb  #HTML5 #Linux #Developer #WebApp #WebDev");
        assertThat(result, equalTo(expected));
    }

    @Test
    public final void whenExtractingUserOutOfRtScenario6_thenCorrectlyExtracted() {
        final String expected = "digitaltvnews";
        final String result = TwitterUtil.extractOriginalUserFromRt("RT @digitaltvnews: Humax unveils the world-first #HTML5 based smart set-top box http://t.co/6nuQJeHNSS #cabletv #digitaltv #korea");
        assertThat(result, equalTo(expected));
    }

    @Test
    public final void whenExtractingUserOutOfRtScenario7_thenCorrectlyExtracted() {
        final String expected = "jboner";
        final String result = TwitterUtil.extractOriginalUserFromRt("RT @jboner: Great initiative \"@GetEventStore: #akka native client in progress ;-) https://t.co/gL1PBDVXVa its not close to ready but give u");
        assertThat(result, equalTo(expected));
    }

    @Test
    public final void whenExtractingUserOutOfRtScenario8_thenCorrectlyExtracted() {
        final String expected = "maliki_borneo";
        final String result = TwitterUtil.extractOriginalUserFromRt("RT @maliki_borneo: Awesome #jQuery #HTML5 uploader by @MicheleBertoli http://t.co/cYVoDN6g3q");
        assertThat(result, equalTo(expected));
    }

    @Test
    public final void whenExtractingUserOutOfRtScenario9_thenCorrectlyExtracted() {
        final String expected = "RadicalFishGame";
        final String result = TwitterUtil.extractOriginalUserFromRt("RT @RadicalFishGame: New post: Weekly Update #25 - Getting closer to the milestone http://t.co/EWO3EqAzLo #crosscode #gamedev #html5 #javas");
        assertThat(result, equalTo(expected));
    }

    @Test
    public final void whenExtractingUserOutOfRtScenario10_thenCorrectlyExtracted() {
        final String expected = "bootply";
        final String result = TwitterUtil.extractOriginalUserFromRt("RT @bootply: 1000's of working snippets and examples for Bootstrap http://t.co/Y7z8Gvbysa #bootstrap #css #webdev #html5");
        assertThat(result, equalTo(expected));
    }

    @Test
    public final void whenExtractingUserOutOfRtScenario11_thenCorrectlyExtracted() {
        final String expected = "OpenWebDevice";
        final String result = TwitterUtil.extractOriginalUserFromRt("RT @OpenWebDevice: Top 10 #HTML5 games http://t.co/s9L3m1hh6D");
        assertThat(result, equalTo(expected));
    }

    @Test
    public final void whenExtractingUserOutOfRtScenario12_thenCorrectlyExtracted() {
        final String expected = "adverway";
        final String result = TwitterUtil.extractOriginalUserFromRt("RT @adverway: Discover some tips about the #Javascript Conference in Spain  http://t.co/4JJp6uAJZQ #spainjs #html5 #advergame");
        assertThat(result, equalTo(expected));
    }

    @Test
    public final void whenExtractingUserOutOfRtScenario13_thenCorrectlyExtracted() {
        final String expected = "akkateam";
        final String result = TwitterUtil.extractOriginalUserFromRt("RT @akkateam: #Akka 2.2 Spotlight: Publish/Subscribe in Cluster… http://t.co/ps3ZUrJnaq");
        assertThat(result, equalTo(expected));
    }

    // break up

    @Test
    public final void whenDeterminingTheLargerPartOfATweetScenario1_thenCorrectlyDetermined() {
        final String originalTweet = "RT @vmbrasseur: This regex MUST become a t-shirt. #osb13 #perl #biking / Credit to @nickpatch http://t.co/Qhrk0b7kAZ";
        final String expectedLargerPart = "RT @vmbrasseur: This regex MUST become a t-shirt. #osb13 #perl #biking / Credit to @nickpatch";

        final String extractedLargerPart = TwitterUtil.extractLargerPart(originalTweet).trim();
        assertThat(extractedLargerPart, equalTo(expectedLargerPart));
    }

    @Test
    public final void whenDeterminingTheLargerPartOfATweetScenario2_thenCorrectlyDetermined() {
        final String originalTweet = "RT @vmbrasseur: This regex MUST become a t-shirt. #osb13 #perl #biking / http://t.co/Qhrk0b7kAZ / Credit to @nickpatch";
        final String expectedLargerPart = "RT @vmbrasseur: This regex MUST become a t-shirt. #osb13 #perl #biking /";

        final String extractedLargerPart = TwitterUtil.extractLargerPart(originalTweet).trim();
        assertThat(extractedLargerPart, equalTo(expectedLargerPart));
    }

    @Test
    public final void whenDeterminingTheLargerPartOfATweetScenario3_thenCorrectlyDetermined() {
        final String originalTweet = "RT @vmbrasseur: http://t.co/Qhrk0b7kAZ - This regex MUST become a t-shirt. #osb13 #perl #biking / Credit to @nickpatch";
        final String expectedLargerPart = "- This regex MUST become a t-shirt. #osb13 #perl #biking / Credit to @nickpatch";

        final String extractedLargerPart = TwitterUtil.extractLargerPart(originalTweet).trim();
        assertThat(extractedLargerPart, equalTo(expectedLargerPart));
    }

}
