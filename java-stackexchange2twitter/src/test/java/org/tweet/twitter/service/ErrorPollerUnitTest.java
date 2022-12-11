package org.tweet.twitter.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.junit.Before;
import org.junit.Test;
import org.tweet.twitter.util.ErrorUtil;

public final class ErrorPollerUnitTest {

    private ErrorPoller instance;

    @Before
    public final void before() {
        instance = new ErrorPoller();
    }

    // tests

    @Test
    public final void when_thenNoExceptions() {
        ErrorUtil.registerError(ErrorUtil.bannedRegExesMaybeErrors, randomAlphabetic(5) + "win" + randomAlphabetic(5), randomAlphabetic(20));
        ErrorUtil.registerError(ErrorUtil.bannedRegExesMaybeErrors, randomAlphabetic(5) + "only" + randomAlphabetic(5), randomAlphabetic(20));
        ErrorUtil.registerError(ErrorUtil.bannedRegExesMaybeErrors, randomAlphabetic(5) + "win" + randomAlphabetic(5), randomAlphabetic(20));
        ErrorUtil.registerError(ErrorUtil.bannedRegExesMaybeErrors, randomAlphabetic(5) + "deal" + randomAlphabetic(5), randomAlphabetic(20));

        instance.logBannedRegExesMaybeErrors();
    }

}
