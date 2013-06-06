package org.tweet.twitter.util;

import java.util.List;

import com.google.common.base.Function;

public class HashWordFunction implements Function<String, String> {
    private final List<String> wordsToHash;

    public HashWordFunction(final List<String> wordsToHash) {
        this.wordsToHash = wordsToHash;
    }

    // API

    @Override
    public final String apply(final String input) {
        if (wordsToHash.contains(input.toLowerCase())) {
            return "#" + input;
        }
        return input;
    }

}
