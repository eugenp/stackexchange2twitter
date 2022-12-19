package org.classification.util;

public final class TweetSettings {

    /**
     *  can be included as well
     * - TODO: potential:
     * `/` with = 0.9xx; without = 0.9xx
     * `+` with = ~0.9x; without = 0.907 - data shows it should be kept out - counterexample: C++
     * `_` with = ~0.908; without = 0.900 - data shows that it should be in
     */
    public static final String TWEET_TOKENIZER = " ,.!?\":()|-&/_";

    // @ is an important signal - do not add to the tokenizer

    private TweetSettings() {
        throw new AssertionError();
    }

    // util

}
