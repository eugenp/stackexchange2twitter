package org.classification.util;

public final class ClassificationSettings {

    /**
     * - TODO: figure out what this is exactly
     */
    public static final int PROBES_FOR_CONTENT_ENCODER_VECTOR = 5;
    public static final int LEARNERS_IN_THE_CLASSIFIER_POOL = 200;
    public static final int FEATURES = 5000;
    /*
    # Actual Scores (training data - core vs full)
    - 5000:
    -- 2 - 0.XXX
    -- 3 - 0.847
    - 7000: 
    -- 3 - 0.903
    -- 4 - 0.909
    -- 5 - 0.910
    -- 6 - 0.913
    */

    /*
    # Vary the size of the pool
    - pool =  50 - 0.789
    - pool = 100 - 0.806
    - pool = 150 - 0.812,0.829
    - pool = 200 - 
     * - note: it's not important how well they perform, just how much is the gap between 50-200 
    */

    /*
    # Vary the size of the runs
    - runs =   250 - 0.824
    - runs =   500 - 0.823
    - runs =   750 - 0.826
    - runs =  1000 - 0.826
     * - note: it's not important how well they perform, just how much is the gap between runs is 
    */

    /**
     *  can be included as well
     * - TODO: potential: 
     * `/` with = 0.9xx; without = 0.9xx
     * `+` with = ~0.9x; without = 0.907 - data shows it should be kept out - counterexample: C++
     * `_` with = ~0.908; without = 0.900 - data shows that it should be in
     */
    public static final String TWEET_TOKENIZER = " ,.!?\":()|-&/_";

    // @ is an important signal - do not add to the tokenizer

    private ClassificationSettings() {
        throw new AssertionError();
    }

    // util

}
