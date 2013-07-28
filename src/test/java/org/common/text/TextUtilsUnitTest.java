package org.common.text;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.common.util.TextUtil;
import org.junit.Test;
import org.tweet.twitter.util.TwitterUtil;

public class TextUtilsUnitTest {

    // tests

    // pre-processing text

    @Test
    public final void whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = ".htaccess & Wordpress: Exclude folder from RewriteRule";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters(".htaccess & Wordpress: Exclude folder from RewriteRule");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario1_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "System(\"pause\"); - Why is it wrong? - stackoverflow.com/questions/1107";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("System(“pause”); - Why is it wrong? - stackoverflow.com/questions/1107");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario2_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "<context:property-placeholder> properties not accessible to the child (web) context";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("<context:property-placeholder> properties not accessible to the child (web) context");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario3_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "#Akka 2.2.0 FINAL Released - http://tmblr.co/ZlHOLwpD-j-a";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("#Akka 2.2.0 FINAL Released — http://tmblr.co/ZlHOLwpD-j-a");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario4_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "What is the difference between an EAR file and a \"WebSphere Enhanced EAR\"?";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("What is the difference between an EAR file and a “WebSphere Enhanced EAR”?");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario5_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "Spring Batch Example - XML File To Database";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("Spring Batch Example – XML File To Database");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario6_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "What's the best way to validate an XML file against an XSD file?";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("What's the best way to validate an XML file against an XSD file?");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario7_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "Understanding \"randomness\"";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("Understanding “randomness”");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario8_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "What's the best way to communicate between view controllers?";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("What's the best way to communicate between view controllers?");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario9_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "I'm broadcasting #LIVE on #HangWith for #iPhone! Come Hang w/souljaboy! http://bit.ly/hangwsocial";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("I&rsquo;m broadcasting #LIVE on #HangWith for #iPhone! Come Hang w/souljaboy! http://bit.ly/hangwsocial");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario10_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "#Google Search API for C# - http://stackoverflow.com/questions/2580396/google-searchapi-for-c";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("#Google Search&nbsp;API for C# - http://stackoverflow.com/questions/2580396/google-searchapi-for-c");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario11_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "#Akka 2.2 Spotlight: Publish/Subscribe in Cluster... http://tmblr.co/ZlHOLwq2afmO";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("#Akka 2.2 Spotlight: Publish/Subscribe in Cluster… http://tmblr.co/ZlHOLwq2afmO");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario12_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "What are Unity's keyboard and mouse shortcuts?";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("What are Unity&#39;s keyboard and mouse shortcuts?");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    // trim

    @Test
    public final void whenTrimmingLargeTweetScenario1_thenCorrectResult() {
        final String largeTweet = "RT @__GiMa__: #NEW [#iOS] #Math #Bracket|# #Education|$0.99-> #FREE #NOW| http://t.co/dVzJKsKFUY #LIMITED #TIME|#iPhone http://t.co/5ZPOw9a...";
        final String trimmedTweet = TextUtil.trimTweet(largeTweet);
        assertTrue(TwitterUtil.isTweetTextWithLinkValid(trimmedTweet));
    }

    @Test
    public final void whenTrimmingLargeTweetScenario2_thenCorrectResult() {
        final String largeTweet = "RT @__GiMa__: #NEW [#iOS] #iGet #- #Download ...|# #Utilities|$0.99-> #FREE #NOW| http://t.co/AYA08aMFKa #LIMITED #TIME|#iPhone http://t.co...";
        final String trimmedTweet = TextUtil.trimTweet(largeTweet);
        assertTrue(TwitterUtil.isTweetTextWithLinkValid(trimmedTweet));
    }

    @Test
    public final void whenTrimmingLargeTweetScenario3_thenCorrectResult() {
        final String largeTweet = "RT @__GiMa__: #NEW [#iOS] #Cool #Barcelona #!|# #Travel|$3.99-> #FREE #NOW| http://t.co/gEBl8pFb11 #LIMITED #TIME|#iPhone http://t.co/EtDBk...";
        final String trimmedTweet = TextUtil.trimTweet(largeTweet);
        assertTrue(TwitterUtil.isTweetTextWithLinkValid(trimmedTweet));
    }

    @Test
    public final void whenTrimmingLargeTweetScenario4_thenCorrectResult() {
        final String largeTweet = "RT @linuxfoundation: A fellow #Linux SysAdmin shares tips and tricks in his blog: http://t.co/mlKBdh413q And a thank u from LF: http://t.co...";
        final String trimmedTweet = TextUtil.trimTweet(largeTweet);
        assertTrue(TwitterUtil.isTweetTextWithLinkValid(trimmedTweet));
    }

    @Test
    public final void whenTrimmingLargeTweetScenario5_thenCorrectResult() {
        final String largeTweet = "RT @FightingCEO: #SQL Formatting standards - Capitalization, Indentation, Comments, Parenthesis http://t.co/gEo3kl91XF via @sharethis #SQL #...";
        final String trimmedTweet = TextUtil.trimTweet(largeTweet);
        assertTrue(TwitterUtil.isTweetTextWithLinkValid(trimmedTweet));
    }

    @Test
    public final void whenTrimmingLargeTweetScenario6_thenCorrectResult() {
        final String largeTweet = "The latest release of #Zend Studio - 10.1.0 - makes it easier than ever to work with #PHP libraries and #Zend Server http://t.co/3k...";
        final String trimmedTweet = TextUtil.trimTweet(largeTweet);
        assertTrue(TwitterUtil.isTweetTextWithLinkValid(trimmedTweet));
    }

    @Test
    public final void whenTrimmingLargeTweetScenario7_thenCorrectResult() {
        final String largeTweet = "#NEW [#iOS] #Ace #Block ...|# #Games|$0.99-> #FREE #NOW| http://bit.ly/17KgsqD  #LIMITED #TIME|#iPhone|#DEAL|#RT|#App|657";
        final String trimmedTweet = TextUtil.trimTweet(largeTweet);
        assertTrue(TwitterUtil.isTweetTextWithLinkValid(trimmedTweet));
    }

}
