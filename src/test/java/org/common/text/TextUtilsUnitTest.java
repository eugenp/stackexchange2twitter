package org.common.text;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.common.util.TextUtils;
import org.junit.Test;

public class TextUtilsUnitTest {

    // tests

    // pre-processing text

    @Test
    public final void whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = ".htaccess & Wordpress: Exclude folder from RewriteRule";
        final String preProcessedTweet = TextUtils.preProcessTweetText(".htaccess & Wordpress: Exclude folder from RewriteRule");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario1_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "System(\"pause\"); - Why is it wrong? - stackoverflow.com/questions/1107";
        final String preProcessedTweet = TextUtils.preProcessTweetText("System(“pause”); - Why is it wrong? - stackoverflow.com/questions/1107");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario2_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "<context:property-placeholder> properties not accessible to the child (web) context";
        final String preProcessedTweet = TextUtils.preProcessTweetText("<context:property-placeholder> properties not accessible to the child (web) context");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario3_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "#Akka 2.2.0 FINAL Released - http://tmblr.co/ZlHOLwpD-j-a";
        final String preProcessedTweet = TextUtils.preProcessTweetText("#Akka 2.2.0 FINAL Released — http://tmblr.co/ZlHOLwpD-j-a");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario4_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "What is the difference between an EAR file and a \"WebSphere Enhanced EAR\"?";
        final String preProcessedTweet = TextUtils.preProcessTweetText("What is the difference between an EAR file and a “WebSphere Enhanced EAR”?");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario5_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "Spring Batch Example - XML File To Database";
        final String preProcessedTweet = TextUtils.preProcessTweetText("Spring Batch Example – XML File To Database");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario6_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "What's the best way to validate an XML file against an XSD file?";
        final String preProcessedTweet = TextUtils.preProcessTweetText("What's the best way to validate an XML file against an XSD file?");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario7_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "Understanding \"randomness\"";
        final String preProcessedTweet = TextUtils.preProcessTweetText("Understanding “randomness”");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario8_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "What's the best way to communicate between view controllers?";
        final String preProcessedTweet = TextUtils.preProcessTweetText("What's the best way to communicate between view controllers?");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario9_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "I'm broadcasting #LIVE on #HangWith for #iPhone! Come Hang w/souljaboy! http://bit.ly/hangwsocial";
        final String preProcessedTweet = TextUtils.preProcessTweetText("I&rsquo;m broadcasting #LIVE on #HangWith for #iPhone! Come Hang w/souljaboy! http://bit.ly/hangwsocial");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    //

}
