package org.classification.util;

import java.io.IOException;

import org.junit.Test;

public final class SpecificClassificationDataUtilIntegrationTest {

    @Test
    public final void whenTrainingDataIsCreated_thenNoExceptions() throws IOException {
        SpecificClassificationDataUtil.commercialCoreTrainingData(ClassificationSettings.PROBES_FOR_CONTENT_ENCODER_VECTOR, ClassificationSettings.FEATURES);
    }

}
