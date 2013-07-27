package org.tweet.twitter.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TweetServiceUnitTest {

    private TweetService instance;

    @Before
    public final void before() {
        instance = new TweetService();
    }

    // tests

    // check if retweet

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

    // check validity

    @Test
    public final void givenTweetScenario1_whenCheckingValidity_thenValid() {
        assertTrue(instance.isTweetFullValid("RT @jtrnix: StarCluster 0.94 has been released! Tons of bug fixes/improvements and several new features: http://t.co/UY1eqvgnSe #EC2 #HPC #..."));
    }

    @Test
    public final void givenTweetScenario2_whenCheckingValidity_thenValid() {
        assertTrue(instance.isTweetFullValid("nrepl.el has new maintainers (me, @technomancy, @hugoduncan and @sanityinc) and a new home https://t.co/cHa0XbIEOR Please,RT #clojure #emacs"));
    }

    @Test
    public final void givenTweetScenario3_whenCheckingValidity_thenValid() {
        assertTrue(instance.isTweetFullValid("RT @zend: The latest release of Zend Studio - 10.1.0 - makes it easier than ever to work with #PHP libraries and Zend Server http://t.co/3k..."));
    }

    @Test
    public final void givenTweetScenario4_whenCheckingValidity_thenValid() {
        assertTrue(instance.isTweetFullValid("RT @sofisticlara: Awesome! Regular Expressions Guide for SEO & Analytics (http://t.co/9ZURwNKDiZ) via @SEOTakeaways @seohimanshu #Regex #SE..."));
    }

    // is it worth retweeting

    @Test
    public final void givenTextShouldNotBeTweeted_whenCheckingOnScenario1_thenRejected() {
        assertFalse(instance.isTweetWorthRetweetingByText("PETZ NAOTO FAL ELLY #junkmania #ldh #exile #3jsb #yeezy #hba #beentrill #chanel #snapback #python\u2026 http://instagram.com/p/bWL_vVwziM/"));
    }

    @Test
    public final void givenTextShouldNotBeTweeted_whenCheckingOnScenario2_thenRejected() {
        assertFalse(instance.isTweetWorthRetweetingByText("Beach day..\uD83D\uDE0D #greece #ios #beautiful @runwaydreamz http://instagram.com/p/bEvmJsI1dn/"));
    }

    @Test
    public final void givenTextShouldNotBeTweeted_whenCheckingOnScenario3_thenRejected() {
        assertFalse(instance.isTweetWorthRetweetingByText("#PHP \"@MarceloLopez84: The picture of Ronaldo which i took at Manchester Airport http://twitter.com/MarceloLopez84/status/357459318175588352/photo/1pic.twitter.com/Tx70a6MNqS \""));
    }

    @Test
    public final void givenTextShouldNotBeTweeted_whenCheckingOnScenario4_thenRejected() {
        assertFalse(instance.isTweetWorthRetweetingByText("#PHP \"@MarceloLopez84: Heres proof!! pic.twitter.com/HDHf2t1W4A\""));
    }

    @Test
    public final void givenTextShouldNotBeTweeted_whenCheckingOnScenario5_thenRejected() {
        assertFalse(instance.isTweetWorthRetweetingByText("#dark #cloud http://instagram.com/p/b6EFkGPgok/"));
    }

    @Test
    public final void givenTextShouldNotBeTweeted_whenCheckingOnScenario6_thenRejected() {
        assertFalse(instance.isTweetWorthRetweetingByText("#PHP EXCLUSIVE: Gareth Bale wants to join Manchester United http://www.dailystar.co.uk/sport/football/327439/EXCLUSIVE-Gareth-Bale-wants-to-join-Manchester-United \u2026 (Daily Star)"));
    }

    @Test
    public final void givenTextShouldNotBeTweeted_whenCheckingOnScenario7_thenRejected() {
        assertFalse(instance.isTweetWorthRetweetingByText("RT if you want to use minion case like this #iPhone #iOS #DespicableMe2 pic.twitter.com/IviFckCgba"));
    }

    @Test
    public final void givenTextShouldNotBeTweeted_whenCheckingOnScenario8_thenRejected() {
        assertFalse(instance.isTweetWorthRetweetingByText("Spelersbus #GAE pic.twitter.com/2USMMHDbyJ"));
    }

}
