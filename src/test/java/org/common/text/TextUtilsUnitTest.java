package org.common.text;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

public class TextUtilsUnitTest {

    // tests

    // extract urls

    @Test
    public final void whenExtractingUrlFromContentScenario1_thenNoExceptions() {
        final String content = "For functional programming fans: <span class=\"proflinkWrapper\"><span class=\"proflinkPrefix\">+</span><a href=\"https://plus.google.com/112870314339261400768\" class=\"proflink\" oid=\"112870314339261400768\">Adam Bard</a></span>�takes a look at Clojure/core.reducers, and how the parallelism expressed in the framework impacts performance. In particular, the new &#39;fold&#39; method (a parallel &#39;reduce&#39; and &#39;combine&#39;) can have major benefits.<br /><br />More on reducers at <a href=\"http://clojure.com/blog/2012/05/15/anatomy-of-reducer.html\" class=\"ot-anchor\" rel=\"nofollow\">clojure.com/blog/2012/05/15/anatomy-of-reducer.html</a>.";
        final List<String> extractedUrls = TextUtils.extractUrls(content);
        assertThat(extractedUrls, hasSize(2));
    }

    @Test
    public final void whenExtractingUrlFromContentScenario2_thenNoExceptions() {
        final String content = "<b>Clojure Koans</b><br /><br />$ git clone git://<a href=\"http://github.com/functional-koans/clojure-koans.git\" class=\"ot-anchor\">github.com/functional-koans/clojure-koans.git</a> <br />$ cd clojure-koans<br />$ curl <a href=\"https://raw.github.com/technomancy/leiningen/stable/bin/lein\" class=\"ot-anchor\">https://raw.github.com/technomancy/leiningen/stable/bin/lein</a> &gt; script/lein<br />$ chmod +x script/lein<br />$ script/lein deps<br />$ script/run<br /><br />Enlightenment awaits.";
        final List<String> extractedUrls = TextUtils.extractUrls(content);
        assertThat(extractedUrls, hasSize(3));
    }

    // determine main url

    @Test
    public final void givenUrls_whenDeterminingMainUrlScenario1_thenCorrectlyDetermined() {
        final String content = "For functional programming fans: <span class=\"proflinkWrapper\"><span class=\"proflinkPrefix\">+</span><a href=\"https://plus.google.com/112870314339261400768\" class=\"proflink\" oid=\"112870314339261400768\">Adam Bard</a></span>�takes a look at Clojure/core.reducers, and how the parallelism expressed in the framework impacts performance. In particular, the new &#39;fold&#39; method (a parallel &#39;reduce&#39; and &#39;combine&#39;) can have major benefits.<br /><br />More on reducers at <a href=\"http://clojure.com/blog/2012/05/15/anatomy-of-reducer.html\" class=\"ot-anchor\" rel=\"nofollow\">clojure.com/blog/2012/05/15/anatomy-of-reducer.html</a>.";
        final List<String> extractedUrls = TextUtils.extractUrls(content);
        final String mainUrl = TextUtils.determineMainUrl(extractedUrls);
        assertThat(mainUrl, equalTo("http://clojure.com/blog/2012/05/15/anatomy-of-reducer.html"));
    }

    @Test
    public final void givenUrls_whenDeterminingMainUrlScenario2_thenCorrectlyDetermined() {
        final String content = "<b>Clojure Koans</b><br /><br />$ git clone git://<a href=\"http://github.com/functional-koans/clojure-koans.git\" class=\"ot-anchor\">github.com/functional-koans/clojure-koans.git</a> <br />$ cd clojure-koans<br />$ curl <a href=\"https://raw.github.com/technomancy/leiningen/stable/bin/lein\" class=\"ot-anchor\">https://raw.github.com/technomancy/leiningen/stable/bin/lein</a> &gt; script/lein<br />$ chmod +x script/lein<br />$ script/lein deps<br />$ script/run<br /><br />Enlightenment awaits.";
        final List<String> extractedUrls = TextUtils.extractUrls(content);
        final String mainUrl = TextUtils.determineMainUrl(extractedUrls);
        assertThat(mainUrl, equalTo("https://raw.github.com/technomancy/leiningen/stable/bin/lein"));
    }

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

    //

}
