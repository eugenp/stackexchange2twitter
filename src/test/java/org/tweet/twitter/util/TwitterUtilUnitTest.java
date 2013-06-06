package org.tweet.twitter.util;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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
        TwitterUtil.prepareTweet(randomAlphabetic(119), randomAlphabetic(19));
    }

    // pre-process

    @Test
    public final void givenTweet_whenPreProcessingTweet_thenNoExceptions() {
        TwitterUtil.hashWords(randomAlphabetic(50), Lists.<String> newArrayList());
    }

    @Test
    public final void givenNoWordsToHash_whenPreProcessingTweet_thenNoChanges() {
        final String originalTweet = randomAlphabetic(50);
        final String processedTweet = TwitterUtil.hashWords(originalTweet, Lists.<String> newArrayList());
        assertThat(originalTweet, equalTo(processedTweet));
    }

    @Test
    public final void givenSomeWordsToHash_whenTweetDoesNotContainTheWord_thenNoChanges() {
        final String originalTweet = "word1 word2 word3";
        final String processedTweet = TwitterUtil.hashWords(originalTweet, Lists.newArrayList("otherword"));
        assertThat(originalTweet, equalTo(processedTweet));
    }

    @Test
    public final void givenSomeWordsToHash_whenTweetDoesContainTheWord_thenTweetChanged1() {
        final String originalTweet = "word1 word2 word3";
        final String processedTweet = TwitterUtil.hashWords(originalTweet, Lists.newArrayList("word2"));
        assertThat(originalTweet, not(equalTo(processedTweet)));
    }

    @Test
    public final void givenSomeWordsToHash_whenTweetDoesContainTheWord_thenTweetChanged2() {
        final String originalTweet = "Testing with Guava";
        final String targetResultTweet = "Testing with #Guava";
        final String processedTweet = TwitterUtil.hashWords(originalTweet, Lists.newArrayList("Guava"));
        assertThat(targetResultTweet, equalTo(processedTweet));
    }

}
