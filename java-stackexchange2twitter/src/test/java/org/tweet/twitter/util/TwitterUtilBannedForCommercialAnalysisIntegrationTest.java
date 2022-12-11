package org.tweet.twitter.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

public final class TwitterUtilBannedForCommercialAnalysisIntegrationTest {

    // tests - should not be banned

    @Test
    public final void givenTweetIsOK_whenCheckingIfIsBannedForCommercialAnalysis1_thenNo() {
        final String tweet = "I heard 2 vets talking about Star Trek. I asked how they became Trekkies. Answer: \"Only final natural after serving final on the USS Enterprise.\" #win";
        assertFalse(TwitterUtil.isTweetBannedForCommercialAnalysis(tweet));
    }

    // tests - should be banned

    @Test
    @Ignore
    public final void givenTweetIsNotOK_whenCheckingIfIsBannedForCommercialAnalysis1_thenYes() {
        final String tweet = "";
        assertTrue(TwitterUtil.isTweetBannedForCommercialAnalysis(tweet));
    }

}
