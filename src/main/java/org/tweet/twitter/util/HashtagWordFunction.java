package org.tweet.twitter.util;

import java.util.List;

import com.google.common.base.Function;

public class HashtagWordFunction implements Function<String, String> {
    private final List<String> wordsToHash;

    public HashtagWordFunction(final List<String> wordsToHash) {
        this.wordsToHash = wordsToHash;
    }

    // API

    @Override
    public final String apply(final String input) {
        return wordToHashtag(input);
    }

    private final String wordToHashtag(final String originalCandidate) {
        String candidate = originalCandidate;
        if (candidate.endsWith("?") || candidate.endsWith("!")) {
            candidate = originalCandidate.substring(0, originalCandidate.length() - 1);
        }
        for (final String wordToHash : wordsToHash) {
            if (wordToHash.equals(candidate.toLowerCase())) {
                return "#" + originalCandidate;
            }
        }
        return originalCandidate;
    }

}
