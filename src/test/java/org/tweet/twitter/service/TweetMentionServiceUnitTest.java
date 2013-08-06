package org.tweet.twitter.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TweetMentionServiceUnitTest {

    private TweetMentionService instance;

    @Before
    public final void before() {
        instance = new TweetMentionService();
    }

    // tests

    // check if retweet mention

    @Test
    public final void whenTweetIsCheckedForBeingRetweet_thenNoExceptions() {
        instance.isRetweetMention(randomAlphabetic(120));
    }

    @Test
    public final void givenTweet1_whenTweetIsCheckedForBeingRetweet_thenIsRetweet() {
        assertTrue(instance.isRetweetMention("RT @DefuseSec: This is the worst crypto I've ever seen. http://www.cryptofails.com/2013/07/osticket-fail-open-fail-often.html \u2026 #php #cryptofails"));
    }

    @Test
    public final void givenTweet2_whenTweetIsCheckedForBeingRetweet_thenIsRetweet() {
        assertTrue(instance.isRetweetMention("RT@DefuseSec: This is the worst crypto I've ever seen. http://www.cryptofails.com/2013/07/osticket-fail-open-fail-often.html \u2026 #php #cryptofails"));
    }

    @Test
    public final void givenTweet3_whenTweetIsCheckedForBeingRetweet_thenIsRetweet() {
        assertTrue(instance.isRetweetMention("OMG. RT @DefuseSec: This is the worst crypto I've ever seen. http://www.cryptofails.com/2013/07/osticket-fail-open-fail-often.html \u2026 #php #cryptofails"));
    }

    @Test
    public final void givenTweet4_whenTweetIsCheckedForBeingRetweet_thenIsRetweet() {
        assertTrue(instance.isRetweetMention("RT@WPGaze Top 20 #WordPress Themes for Entertainment Blogs http://www.wpgaze.com/top-20-wordpress-themes-for-entertainment-blogs.html \u2026 #WordPress #WPGaze #Themes #Entertainment #Blogs"));
    }

    @Test
    public final void givenTweet5_whenTweetIsCheckedForBeingRetweet_thenIsRetweet() {
        assertTrue(instance.isRetweetMention("Morning all! RT @ContractHire http://t.co/dxKq3cl03U follow and you could win a new #iPad Mini! #comp #Apple #ipad"));
    }

    // extract mentions

    @Test
    public final void whenExtractingMentionsFromTweetText1_thenCorrect() {
        final List<String> mentions = instance.extractMentions("@fmueller_bln When you are talking about layer, do you mean the XML change sets of Liquibase?");
        assertThat(mentions, hasItem("@fmueller_bln"));
    }

    @Test
    public final void whenExtractingMentionsFromTweetText2_thenCorrect() {
        final List<String> mentions = instance.extractMentions("DON'T MISS: Join @russmiles for a talk on #Clojure #Lisp & #Simplicity on THURSDAY @skillsmatter: http://ow.ly/nCZ5o  #london");
        assertThat(mentions, hasItems("@russmiles", "@skillsmatter"));
    }

    @Test
    public final void whenExtractingMentionsFromTweetText3_thenCorrect() {
        final List<String> mentions = instance.extractMentions("@AmitSin94906455 I participated to this course: https://www.coursera.org/course/progfun  The http://scala-lang.org  has great docs: http://www.scala-lang.org/documentation/");
        assertThat(mentions, hasItems("@AmitSin94906455"));
    }

}
