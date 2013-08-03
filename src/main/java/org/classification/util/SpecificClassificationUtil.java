package org.classification.util;

import static org.classification.util.ClassificationSettings.FEATURES;
import static org.classification.util.ClassificationSettings.LEARNERS_IN_THE_CLASSIFIER_POOL;

import java.io.IOException;
import java.util.List;

import org.apache.mahout.classifier.sgd.AdaptiveLogisticRegression;
import org.apache.mahout.classifier.sgd.CrossFoldLearner;
import org.apache.mahout.math.NamedVector;

public final class SpecificClassificationUtil {

    public static final String PROGRAMMING = "programming";
    public static final String NONPROGRAMMING = "nonprogramming";

    public static final String COMMERCIAL = "commercial";
    public static final String NONCOMMERCIAL = "noncommercial";

    private SpecificClassificationUtil() {
        throw new AssertionError();
    }

    // classifier

    public static CrossFoldLearner trainNewLearnerCommercialWithCoreTrainingData(final int probes, final int features) throws IOException {
        final List<NamedVector> learningData = SpecificClassificationDataUtil.commercialVsNonCommercialCoreTrainingData(probes, features);
        final AdaptiveLogisticRegression classifier = SpecificClassificationUtil.trainCommercialClassifier(learningData, features, LEARNERS_IN_THE_CLASSIFIER_POOL);
        final CrossFoldLearner bestLearner = classifier.getBest().getPayload().getLearner();

        return bestLearner;
    }

    public static CrossFoldLearner trainNewLearnerCommercialWithFullTrainingData(final int probes, final int features) throws IOException {
        final List<NamedVector> learningData = SpecificClassificationDataUtil.commercialVsNonCommercialTrainingData(probes, features);
        final AdaptiveLogisticRegression classifier = SpecificClassificationUtil.trainCommercialClassifier(learningData, features, LEARNERS_IN_THE_CLASSIFIER_POOL);
        final CrossFoldLearner bestLearner = classifier.getBest().getPayload().getLearner();

        return bestLearner;
    }

    public static CrossFoldLearner trainNewLearnerProgramming(final int probes, final int features) throws IOException {
        final List<NamedVector> trainingData = SpecificClassificationDataUtil.programmingVsNonProgrammingTrainingData(probes, features);
        final AdaptiveLogisticRegression classifier = SpecificClassificationUtil.trainProgrammingClassifier(trainingData, features, LEARNERS_IN_THE_CLASSIFIER_POOL);
        final CrossFoldLearner bestLearner = classifier.getBest().getPayload().getLearner();

        return bestLearner;
    }

    public static AdaptiveLogisticRegression trainCommercialClassifierDefault(final Iterable<NamedVector> vectors) throws IOException {
        return trainCommercialClassifier(vectors, FEATURES, LEARNERS_IN_THE_CLASSIFIER_POOL);
    }

    public static AdaptiveLogisticRegression trainProgrammingClassifierDefault(final Iterable<NamedVector> vectors) throws IOException {
        return trainProgrammingClassifier(vectors, FEATURES, LEARNERS_IN_THE_CLASSIFIER_POOL);
    }

    private static AdaptiveLogisticRegression trainCommercialClassifier(final Iterable<NamedVector> vectors, final int features, final int learners) throws IOException {
        return GenericClassificationUtil.trainGenericClassifier(COMMERCIAL, vectors, features, learners);
    }

    private static AdaptiveLogisticRegression trainProgrammingClassifier(final Iterable<NamedVector> vectors, final int features, final int learners) throws IOException {
        return GenericClassificationUtil.trainGenericClassifier(PROGRAMMING, vectors, features, learners);
    }

}
