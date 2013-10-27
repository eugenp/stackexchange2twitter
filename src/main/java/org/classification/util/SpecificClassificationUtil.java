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

    public static final String JOB = "job";
    public static final String NONJOB = "nonjob";

    private SpecificClassificationUtil() {
        throw new AssertionError();
    }

    // classifier

    public static CrossFoldLearner trainNewLearnerJobWithCoreTrainingData(final int probes, final int features) throws IOException {
        final List<NamedVector> coreTrainingData = SpecificClassificationDataUtil.jobsVsNonJobsCoreTrainingDataShuffled(probes, features);
        return trainNewLearnerCommercial(coreTrainingData, probes, features);
    }

    public static CrossFoldLearner trainNewLearnerCommercialWithFullTrainingData(final int probes, final int features) throws IOException {
        final List<NamedVector> trainingData = SpecificClassificationDataUtil.jobsVsNonJobsTrainingDataShuffled(probes, features);
        final AdaptiveLogisticRegression classifier = SpecificClassificationUtil.trainCommercialClassifier(trainingData, features, LEARNERS_IN_THE_CLASSIFIER_POOL);
        final CrossFoldLearner bestLearner = classifier.getBest().getPayload().getLearner();
        return bestLearner;
    }

    public static CrossFoldLearner trainNewLearnerProgramming(final int probes, final int features) throws IOException {
        final List<NamedVector> trainingData = SpecificClassificationDataUtil.programmingVsNonProgrammingTrainingDataShuffled(probes, features);
        return trainNewLearnerCommercial(trainingData, probes, features);
    }

    public static CrossFoldLearner trainNewLearnerCommercial(final List<NamedVector> trainingData, final int probes, final int features) throws IOException {
        final AdaptiveLogisticRegression classifier = SpecificClassificationUtil.trainCommercialClassifier(trainingData, features, LEARNERS_IN_THE_CLASSIFIER_POOL);
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
        return GenericClassificationUtil.trainGenericClassifier(JOB, vectors, features, learners);
    }

    private static AdaptiveLogisticRegression trainProgrammingClassifier(final Iterable<NamedVector> vectors, final int features, final int learners) throws IOException {
        return GenericClassificationUtil.trainGenericClassifier(PROGRAMMING, vectors, features, learners);
    }

}
