package org.classification.util;

import static org.classification.util.ClassificationSettings.FEATURES;
import static org.classification.util.ClassificationSettings.PROBES_FOR_CONTENT_ENCODER_VECTOR;
import static org.classification.util.GenericClassificationDataUtil.oneVsAnotherTrainingDataShuffled;
import static org.classification.util.GenericClassificationDataUtil.testData;
import static org.classification.util.GenericClassificationDataUtil.trainingData;
import static org.classification.util.SpecificClassificationUtil.JOB;
import static org.classification.util.SpecificClassificationUtil.NONJOB;
import static org.classification.util.SpecificClassificationUtil.NONPROGRAMMING;
import static org.classification.util.SpecificClassificationUtil.PROGRAMMING;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.mahout.math.NamedVector;

public final class SpecificClassificationDataUtil {

    public static final class Other {
        public static final String SAMPLE = "/classification/sample.classif";
    }

    public static final class TrainingFull {
        public static final String NONCOMMERCIAL = "/classification/jobs/nonjobs-full.classif";
        public static final String COMMERCIAL = "/classification/jobs/jobs-full.classif";

        public static final String NONPROGRAMMING = "/classification/programming/nonprogramming-full.classif";
        public static final String PROGRAMMING = "/classification/programming/programming-full.classif";
    }

    public static final class TrainingCore {
        public static final String NONCOMMERCIAL = "/classification/jobs/nonjobs-core.classif";
        public static final String COMMERCIAL = "/classification/jobs/jobs-core.classif";

        public static final String NONPROGRAMMING = "/classification/programming/nonprogramming-core.classif";
        public static final String PROGRAMMING = "/classification/programming/programming-core.classif";
    }

    public static final class Training {
        public static final String NONCOMMERCIAL = "/classification/jobs/nonjobs.classif";
        public static final String COMMERCIAL = "/classification/jobs/jobs.classif";

        public static final String NONPROGRAMMING = "/classification/programming/nonprogramming.classif";
        public static final String PROGRAMMING = "/classification/programming/programming.classif";
    }

    public static final class Test {
        public static final String NONCOMMERCIAL = "/classification/test/jobs/nonjobs.classif";
        public static final String COMMERCIAL = "/classification/test/jobs/jobs.classif";

        public static final String NONPROGRAMMING = "/classification/test/programming/nonprogramming.classif";
        public static final String PROGRAMMING = "/classification/test/programming/programming.classif";
    }

    private SpecificClassificationDataUtil() {
        throw new AssertionError();
    }

    // API

    // training data

    public static final List<NamedVector> programmingVsNonProgrammingTrainingDataDefault() throws IOException {
        return programmingVsNonProgrammingTrainingDataShuffled(PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
    }

    public static final List<NamedVector> jobsVsNonJobsTrainingDataDefault() throws IOException {
        return jobsVsNonJobsTrainingDataShuffled(PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
    }

    static final List<NamedVector> programmingVsNonProgrammingTrainingDataShuffled(final int probes, final int features) throws IOException {
        final List<NamedVector> nonProgrammingVectors = nonProgrammingTrainingData(probes, features);
        final List<NamedVector> programmingNamedVectors = programmingTrainingData(probes, features);
        return oneVsAnotherTrainingDataShuffled(probes, features, nonProgrammingVectors, programmingNamedVectors);
    }

    static final List<NamedVector> jobsVsNonJobsTrainingDataShuffled(final int probes, final int features) throws IOException {
        final List<NamedVector> nonJobsVectors = nonJobsTrainingData(probes, features);
        final List<NamedVector> jobsNamedVectors = jobsTrainingData(probes, features);
        return oneVsAnotherTrainingDataShuffled(probes, features, nonJobsVectors, jobsNamedVectors);
    }

    public static final List<NamedVector> jobsVsNonJobsCoreTrainingDataShuffled(final int probes, final int features) throws IOException {
        final List<NamedVector> nonJobsVectors = nonJobsCoreTrainingData(probes, features);
        final List<NamedVector> jobsNamedVectors = jobsCoreTrainingData(probes, features);
        return oneVsAnotherTrainingDataShuffled(probes, features, nonJobsVectors, jobsNamedVectors);
    }

    public static final List<NamedVector> jobsVsNonJobsFullTrainingDataShuffled(final int probes, final int features) throws IOException {
        final List<NamedVector> nonJobsVectors = nonJobsFullTrainingData(probes, features);
        final List<NamedVector> jobsNamedVectors = jobsFullTrainingData(probes, features);
        return oneVsAnotherTrainingDataShuffled(probes, features, nonJobsVectors, jobsNamedVectors);
    }

    // test data

    static final List<ImmutablePair<String, String>> programmingTestData() throws IOException {
        return testData(Test.PROGRAMMING, PROGRAMMING);
    }

    static final List<ImmutablePair<String, String>> nonProgrammingTestData() throws IOException {
        return testData(Test.NONPROGRAMMING, NONPROGRAMMING);
    }

    static final List<ImmutablePair<String, String>> jobsTestData() throws IOException {
        return testData(Test.COMMERCIAL, JOB);
    }

    static final List<ImmutablePair<String, String>> nonJobsTestData() throws IOException {
        return testData(Test.NONCOMMERCIAL, NONJOB);
    }

    // training data

    static final List<NamedVector> jobsTrainingData(final int probes, final int features) throws IOException {
        return trainingData(Training.COMMERCIAL, JOB, probes, features);
    }

    static final List<NamedVector> nonJobsTrainingData(final int probes, final int features) throws IOException {
        return trainingData(Training.NONCOMMERCIAL, NONJOB, probes, features);
    }

    static final List<NamedVector> programmingTrainingData(final int probes, final int features) throws IOException {
        return trainingData(Training.PROGRAMMING, PROGRAMMING, probes, features);
    }

    static final List<NamedVector> nonProgrammingTrainingData(final int probes, final int features) throws IOException {
        return trainingData(Training.NONPROGRAMMING, NONPROGRAMMING, probes, features);
    }

    // core training data

    static final List<NamedVector> jobsCoreTrainingData(final int probes, final int features) throws IOException {
        return trainingData(TrainingCore.COMMERCIAL, JOB, probes, features);
    }

    static final List<NamedVector> nonJobsCoreTrainingData(final int probes, final int features) throws IOException {
        return trainingData(TrainingCore.NONCOMMERCIAL, NONJOB, probes, features);
    }

    // full training data

    static final List<NamedVector> jobsFullTrainingData(final int probes, final int features) throws IOException {
        return trainingData(TrainingFull.COMMERCIAL, JOB, probes, features);
    }

    static final List<NamedVector> nonJobsFullTrainingData(final int probes, final int features) throws IOException {
        return trainingData(TrainingFull.NONCOMMERCIAL, NONJOB, probes, features);
    }

}
