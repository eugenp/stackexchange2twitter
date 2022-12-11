package org.tweet.twitter.util;

import java.util.List;

import com.google.common.base.Function;

public class HashtagWordFunction implements Function<String, String> {
    private final List<String> wordsToHash;
    private int transformationsDone = 0;

    public HashtagWordFunction(final List<String> wordsToHash) {
        this.wordsToHash = wordsToHash;
    }

    // API

    @Override
    public final String apply(final String input) {
        return wordToHashtag(input);
    }

    public int getTransformationsDone() {
        return transformationsDone;
    }

    // util

    private final String wordToHashtag(final String originalCandidate) {
        String candidate = originalCandidate;
        if (candidate.endsWith("?") || candidate.endsWith("!") || candidate.endsWith(",")) {
            candidate = originalCandidate.substring(0, originalCandidate.length() - 1);
        }
        for (final String wordToHash : wordsToHash) {
            if (wordToHash.equals(candidate.toLowerCase())) {
                transformationsDone++;
                return "#" + originalCandidate;
            }
        }
        return originalCandidate;
    }

}
