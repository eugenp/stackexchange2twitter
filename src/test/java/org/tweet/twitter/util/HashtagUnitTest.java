package org.tweet.twitter.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.google.common.collect.Lists;

public final class HashtagUnitTest {

    // tests

    // isTweetTextValid

    @Test
    public final void givenWordShouldBeHashtag_whenTransformingToHashtag_thenTransformed() {
        final String twitterTag = new HashtagWordFunction(Lists.newArrayList("java")).apply("Java?");
        assertThat(twitterTag, equalTo("#Java?"));
    }

}
