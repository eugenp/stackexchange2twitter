package org.tweet.twitter.service;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

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
