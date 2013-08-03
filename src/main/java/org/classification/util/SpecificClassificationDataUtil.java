package org.classification.util;

import static org.classification.util.ClassificationSettings.FEATURES;
import static org.classification.util.ClassificationSettings.PROBES_FOR_CONTENT_ENCODER_VECTOR;
import static org.classification.util.GenericClassificationDataUtil.oneVsAnotherTrainingDataShuffled;
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

    static final class TrainingFull {
        private static final String CLASSIFICATION_NONCOMMERCIAL = "/classification/commercial/noncommercial-full.classif";
        private static final String CLASSIFICATION_COMMERCIAL = "/classification/commercial/commercial-full.classif";

        private static final String CLASSIFICATION_NONPROGRAMMING = "/classification/programming/nonprogramming-full.classif";
        private static final String CLASSIFICATION_PROGRAMMING = "/classification/programming/programming-full.classif";
    }

    static final class TrainingCore {
        private static final String CLASSIFICATION_NONCOMMERCIAL = "/classification/commercial/noncommercial-core.classif";
        private static final String CLASSIFICATION_COMMERCIAL = "/classification/commercial/commercial-core.classif";

        private static final String CLASSIFICATION_NONPROGRAMMING = "/classification/programming/nonprogramming-core.classif";
        private static final String CLASSIFICATION_PROGRAMMING = "/classification/programming/programming-core.classif";
    }

    static final class Training {
        private static final String CLASSIFICATION_NONCOMMERCIAL = "/classification/commercial/noncommercial.classif";
        private static final String CLASSIFICATION_COMMERCIAL = "/classification/commercial/commercial.classif";

        private static final String CLASSIFICATION_NONPROGRAMMING = "/classification/programming/nonprogramming.classif";
        private static final String CLASSIFICATION_PROGRAMMING = "/classification/programming/programming.classif";
    }

    static final class Test {
        private static final String CLASSIFICATION_NONCOMMERCIAL = "/classification/test/commercial/noncommercial.classif";
        private static final String CLASSIFICATION_COMMERCIAL = "/classification/test/commercial/commercial.classif";

        private static final String CLASSIFICATION_NONPROGRAMMING = "/classification/test/programming/nonprogramming.classif";
        private static final String CLASSIFICATION_PROGRAMMING = "/classification/test/programming/programming.classif";
    }

    private SpecificClassificationDataUtil() {
        throw new AssertionError();
    }

    // API

    // training data

    public static final List<NamedVector> programmingVsNonProgrammingTrainingDataDefault() throws IOException {
        return programmingVsNonProgrammingTrainingDataShuffled(PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
    }

    public static final List<NamedVector> commercialVsNonCommercialTrainingDataDefault() throws IOException {
        return commercialVsNonCommercialTrainingDataShuffled(PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
    }

    static final List<NamedVector> programmingVsNonProgrammingTrainingDataShuffled(final int probes, final int features) throws IOException {
        final List<NamedVector> nonProgrammingVectors = nonProgrammingTrainingData(probes, features);
        final List<NamedVector> programmingNamedVectors = programmingTrainingData(probes, features);
        return oneVsAnotherTrainingDataShuffled(probes, features, nonProgrammingVectors, programmingNamedVectors);
    }

    static final List<NamedVector> commercialVsNonCommercialTrainingDataShuffled(final int probes, final int features) throws IOException {
        final List<NamedVector> nonCommercialvectors = nonCommercialTrainingData(probes, features);
        final List<NamedVector> commercialNamedVectors = commercialTrainingData(probes, features);
        return oneVsAnotherTrainingDataShuffled(probes, features, nonCommercialvectors, commercialNamedVectors);
    }

    public static final List<NamedVector> commercialVsNonCommercialCoreTrainingDataShuffled(final int probes, final int features) throws IOException {
        final List<NamedVector> nonCommercialvectors = nonCommercialCoreTrainingData(probes, features);
        final List<NamedVector> commercialNamedVectors = commercialCoreTrainingData(probes, features);
        return oneVsAnotherTrainingDataShuffled(probes, features, nonCommercialvectors, commercialNamedVectors);
    }

    static final List<NamedVector> commercialVsNonCommercialFullTrainingDataShuffled(final int probes, final int features) throws IOException {
        final List<NamedVector> nonCommercialvectors = nonCommercialFullTrainingData(probes, features);
        final List<NamedVector> commercialNamedVectors = commercialFullTrainingData(probes, features);
        return oneVsAnotherTrainingDataShuffled(probes, features, nonCommercialvectors, commercialNamedVectors);
    }

    // test data

    static final List<ImmutablePair<String, String>> programmingTestData() throws IOException {
        return testData(Test.CLASSIFICATION_PROGRAMMING, PROGRAMMING);
    }

    static final List<ImmutablePair<String, String>> nonprogrammingTestData() throws IOException {
        return testData(Test.CLASSIFICATION_NONPROGRAMMING, NONPROGRAMMING);
    }

    static final List<ImmutablePair<String, String>> commercialTestData() throws IOException {
        return testData(Test.CLASSIFICATION_COMMERCIAL, COMMERCIAL);
    }

    static final List<ImmutablePair<String, String>> noncommercialTestData() throws IOException {
        return testData(Test.CLASSIFICATION_NONCOMMERCIAL, NONCOMMERCIAL);
    }

    // training data

    static final List<NamedVector> commercialTrainingData(final int probes, final int features) throws IOException {
        return trainingData(Training.CLASSIFICATION_COMMERCIAL, COMMERCIAL, probes, features);
    }

    static final List<NamedVector> nonCommercialTrainingData(final int probes, final int features) throws IOException {
        return trainingData(Training.CLASSIFICATION_NONCOMMERCIAL, NONCOMMERCIAL, probes, features);
    }

    static final List<NamedVector> programmingTrainingData(final int probes, final int features) throws IOException {
        return trainingData(Training.CLASSIFICATION_PROGRAMMING, PROGRAMMING, probes, features);
    }

    static final List<NamedVector> nonProgrammingTrainingData(final int probes, final int features) throws IOException {
        return trainingData(Training.CLASSIFICATION_NONPROGRAMMING, NONPROGRAMMING, probes, features);
    }

    // core training data

    static final List<NamedVector> commercialCoreTrainingData(final int probes, final int features) throws IOException {
        return trainingData(TrainingCore.CLASSIFICATION_COMMERCIAL, COMMERCIAL, probes, features);
    }

    static final List<NamedVector> nonCommercialCoreTrainingData(final int probes, final int features) throws IOException {
        return trainingData(TrainingCore.CLASSIFICATION_NONCOMMERCIAL, NONCOMMERCIAL, probes, features);
    }

    // full training data

    static final List<NamedVector> commercialFullTrainingData(final int probes, final int features) throws IOException {
        return trainingData(TrainingFull.CLASSIFICATION_COMMERCIAL, COMMERCIAL, probes, features);
    }

    static final List<NamedVector> nonCommercialFullTrainingData(final int probes, final int features) throws IOException {
        return trainingData(TrainingFull.CLASSIFICATION_NONCOMMERCIAL, NONCOMMERCIAL, probes, features);
    }

}
