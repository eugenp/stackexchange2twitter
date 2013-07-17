package org.common.classification;

public final class ClassificationSettings {

    public static final int LEARNERS_IN_THE_CLASSIFIER_POOL = 50;
    public static final int FEATURES = 5000;

    public static final String TWEET_TOKENIZER = " ,.!?\":()|";

    // @ is an important signal - do not add to the tokenizer

    private ClassificationSettings() {
        throw new AssertionError();
    }

    // util

}
