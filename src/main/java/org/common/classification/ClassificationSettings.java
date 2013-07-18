package org.common.classification;

public final class ClassificationSettings {

    /**
     * -- 5000 features
     * 2 - 0.942
     * 3 - 0.944
     * 4 - 0.940
     * 5 - 0.920
     * 6 - 0.909
     * 10 -  
     * -- 1000 features
     * 3 - 0.931
     * 5 - 0.920
     * 10 - 
     * - TODO: figure out what this is exactly
     */
    public static final int PROBES_FOR_CONTENT_ENCODER_VECTOR = 10;
    public static final int LEARNERS_IN_THE_CLASSIFIER_POOL = 100;
    public static final int FEATURES = 10000;

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
