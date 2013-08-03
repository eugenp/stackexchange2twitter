package org.classification.util;

import static org.classification.util.ClassificationSettings.FEATURES;
import static org.classification.util.ClassificationSettings.PROBES_FOR_CONTENT_ENCODER_VECTOR;
import static org.classification.util.GenericClassificationDataUtil.oneVsAnotherLearningData;
import static org.classification.util.GenericClassificationDataUtil.testData;
import static org.classification.util.GenericClassificationDataUtil.trainingData;
import static org.classification.util.SpecificClassificationUtil.COMMERCIAL;
import static org.classification.util.SpecificClassificationUtil.NONCOMMERCIAL;
import static org.classification.util.SpecificClassificationUtil.NONPROGRAMMING;
import static org.classification.util.SpecificClassificationUtil.PROGRAMMING;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.mahout.math.NamedVector;

public final class SpecificClassificationDataUtil {

    private static final String CLASSIFICATION_NONPROGRAMMING = "/classification/nonprogramming.classif";
    private static final String CLASSIFICATION_PROGRAMMING = "/classification/programming.classif";
    private static final String CLASSIFICATION_NONCOMMERCIAL = "/classification/noncommercial.classif";
    private static final String CLASSIFICATION_COMMERCIAL = "/classification/commercial.classif";

    private static final String CLASSIFICATION_TEST_NONCOMMERCIAL = "/classification/test/noncommercial.classif";
    private static final String CLASSIFICATION_TEST_COMMERCIAL = "/classification/test/commercial.classif";
    private static final String CLASSIFICATION_TEST_NONPROGRAMMING = "/classification/test/nonprogramming.classif";
    private static final String CLASSIFICATION_TEST_PROGRAMMING = "/classification/test/programming.classif";

    private SpecificClassificationDataUtil() {
        throw new AssertionError();
    }

    // API

    // training data

    public static final List<NamedVector> programmingVsNonProgrammingTrainingDataDefault() throws IOException {
        return programmingVsNonProgrammingTrainingData(PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
    }

    public static final List<NamedVector> commercialVsNonCommercialTrainingDataDefault() throws IOException {
        return commercialVsNonCommercialTrainingData(PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
    }

    static final List<NamedVector> programmingVsNonProgrammingTrainingData(final int probes, final int features) throws IOException {
        final List<NamedVector> nonProgrammingVectors = nonProgrammingTrainingData(probes, features);
        final List<NamedVector> programmingNamedVectors = programmingTrainingData(probes, features);
        return oneVsAnotherLearningData(probes, features, nonProgrammingVectors, programmingNamedVectors);
    }

    static final List<NamedVector> commercialVsNonCommercialTrainingData(final int probes, final int features) throws IOException {
        final List<NamedVector> nonCommercialvectors = nonCommercialTrainingData(probes, features);
        final List<NamedVector> commercialNamedVectors = commercialTrainingData(probes, features);
        return oneVsAnotherLearningData(probes, features, nonCommercialvectors, commercialNamedVectors);
    }

    // test data

    static final List<ImmutablePair<String, String>> programmingTestData() throws IOException {
        return testData(CLASSIFICATION_TEST_PROGRAMMING, PROGRAMMING);
    }

    static final List<ImmutablePair<String, String>> nonprogrammingTestData() throws IOException {
        return testData(CLASSIFICATION_TEST_NONPROGRAMMING, NONPROGRAMMING);
    }

    static final List<ImmutablePair<String, String>> commercialTestData() throws IOException {
        return testData(CLASSIFICATION_TEST_COMMERCIAL, COMMERCIAL);
    }

    static final List<ImmutablePair<String, String>> noncommercialTestData() throws IOException {
        return testData(CLASSIFICATION_TEST_NONCOMMERCIAL, NONCOMMERCIAL);
    }

    // util

    static final List<NamedVector> commercialTrainingData(final int probes, final int features) throws IOException {
        return trainingData(CLASSIFICATION_COMMERCIAL, COMMERCIAL, probes, features);
    }

    static final List<NamedVector> nonCommercialTrainingData(final int probes, final int features) throws IOException {
        return trainingData(CLASSIFICATION_NONCOMMERCIAL, NONCOMMERCIAL, probes, features);
    }

    private static final List<NamedVector> programmingTrainingData(final int probes, final int features) throws IOException {
        return trainingData(CLASSIFICATION_PROGRAMMING, PROGRAMMING, probes, features);
    }

    private static final List<NamedVector> nonProgrammingTrainingData(final int probes, final int features) throws IOException {
        return trainingData(CLASSIFICATION_NONPROGRAMMING, NONPROGRAMMING, probes, features);
    }

}
