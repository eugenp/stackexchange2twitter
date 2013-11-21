package org.tweet.meta.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public final class TweetContainsWordPredicateUnitTest {

    private TweetContainsWordPredicate instance;

    @Before
    public final void before() {
        this.instance = new TweetContainsWordPredicate("scala");
    }

    // tests

    @Test
    public final void whenDeterminingIfContainsWordForTweetText1_thenNo() {
        final String tweetTextLowerCase = "New Blog Post: Digital signage and in-store radio provide a unique shopping experience at AW LAB http://ow.ly/2BzQNv  via @ScalaInc".toLowerCase();
        final boolean containsWord = instance.containsWord(tweetTextLowerCase);
        assertThat(containsWord, is(false));
    }

    @Test
    public final void whenDeterminingIfContainsWordForTweetText2_thenNo() {
        final String tweetTextLowerCase = "Don't miss: Jonas Boner's awesome talk -The Road to #Akka #Cluster, and Beyond at #ScalaX 2013 on Dec 2nd-3rd http://ow.ly/r0Qsd  @jboner".toLowerCase();
        final boolean containsWord = instance.containsWord(tweetTextLowerCase);
        assertThat(containsWord, is(false));
    }

    @Test
    public final void whenDeterminingIfContainsWordForTweetText3_thenNo() {
        final String tweetTextLowerCase = "Built-in support for SBT projects is now available in IntelliJ IDEA 13! http://ow.ly/qVCjJ  - via @intellijidea".toLowerCase();
        final boolean containsWord = instance.containsWord(tweetTextLowerCase);
        assertThat(containsWord, is(false));
    }

    //
    @Test
    public final void whenDeterminingIfContainsWordForTweetText1_thenYes() {
        final String tweetTextLowerCase = "Demystifying #Scala with Netflix's @dmarsh http://hanselminutes.com/394/demystifying-scala-with-netflixs-dianne-marsh …".toLowerCase();
        final boolean containsWord = instance.containsWord(tweetTextLowerCase);
        assertThat(containsWord, is(true));
    }

    @Test
    public final void whenDeterminingIfContainsWordForTweetText2_thenYes() {
        final String tweetTextLowerCase = "Don't miss: David Pollak's talk 'Some musings on Scala & Clojure by a long time Scala dude' at #ClojureX 2013 Dec 6th http://ow.ly/r0Tvp".toLowerCase();
        final boolean containsWord = instance.containsWord(tweetTextLowerCase);
        assertThat(containsWord, is(true));
    }

    @Test
    public final void whenDeterminingIfContainsWordForTweetText3_thenYes() {
        final String tweetTextLowerCase = "Our very own Lukasz Szwed will present a talk at @krakowscala's upcoming @ScalaCamp in #Krakow! http://tech.gilt.com/post/67585145801/gilt-is-a-sponsor-of-krakow-polands-scalacamp-4 … #scala #typesafe".toLowerCase();
        final boolean containsWord = instance.containsWord(tweetTextLowerCase);
        assertThat(containsWord, is(true));
    }

    @Test
    public final void whenDeterminingIfContainsWordForTweetText4_thenYes() {
        final String tweetTextLowerCase = "What does \"code: => Unit\" mean in #scala? - http://bit.ly/1iuNphw".toLowerCase();
        final boolean containsWord = instance.containsWord(tweetTextLowerCase);
        assertThat(containsWord, is(true));
    }
}
