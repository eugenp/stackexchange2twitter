package org.classification.util;

public final class ClassificationSettings {

    /**
     * - TODO: figure out what this is exactly
     */
    public static final int PROBES_FOR_CONTENT_ENCODER_VECTOR = 3;
    public static final int LEARNERS_IN_THE_CLASSIFIER_POOL = 100;
    public static final int FEATURES = 7000;
    // 5000,3 - 0.886
    // 7000,3 - 0.890

    public static final String TWEET_TOKENIZER = " ,.!?\":()|-";

    // @ is an important signal - do not add to the tokenizer

    private ClassificationSettings() {
        throw new AssertionError();
    }

    // util

}
// features = 10000, pool = 200 => sec 1122 (0.561)
// features = 5000, pool = 150 => sec 559
// features = 5000, pool = 30 => sec 100
