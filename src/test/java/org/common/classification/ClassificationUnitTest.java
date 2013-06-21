package org.common.classification;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.junit.Test;

public class ClassificationUnitTest {

    // tests

    @Test
    public final void whenTextIsEncodedAsVector_thenNoExceptions() {
        ClassificationUtil.encodeFeatureVector(randomAlphabetic(12));
    }

}
