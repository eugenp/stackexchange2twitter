package org.classification.data;

import static org.classification.data.GenericClassificationDataUtil.oneVsAnotherTrainingDataShuffled;
import static org.classification.data.GenericClassificationDataUtil.testData;
import static org.classification.util.ClassificationSettings.FEATURES;
import static org.classification.util.ClassificationSettings.PROBES_FOR_CONTENT_ENCODER_VECTOR;
import static org.classification.util.SpecificClassificationUtil.COMMERCIAL;
import static org.classification.util.SpecificClassificationUtil.JOB;
import static org.classification.util.SpecificClassificationUtil.NONCOMMERCIAL;
import static org.classification.util.SpecificClassificationUtil.NONJOB;
import static org.classification.util.SpecificClassificationUtil.NONPROGRAMMING;
import static org.classification.util.SpecificClassificationUtil.PROGRAMMING;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.mahout.math.NamedVector;

public final class ClassificationData {

    public static final class Commercial {

        public static final class Training {
            public static final String NONCOMMERCIAL = "/classification/commercial/noncommercial.classif";
            public static final String COMMERCIAL = "/classification/commercial/commercial.classif";
        }

        public static final class Test {
            public static final String NONCOMMERCIAL = "/classification/test/commercial/noncommercial.classif";
            public static final String COMMERCIAL = "/classification/test/commercial/commercial.classif";
        }

    }

    public static final class Jobs {
        public static final class Training {
            public static final String NONJOBS = "/classification/jobs/nonjobs.classif";
            public static final String JOBS = "/classification/jobs/jobs.classif";

            public static final String NONJOBS_FULL = "/classification/jobs/nonjobs-full.classif";
            public static final String JOBS_FULL = "/classification/jobs/jobs-full.classif";

            public static final String NONJOBS_CORE = "/classification/jobs/nonjobs-core.classif";
            public static final String JOBS_CORE = "/classification/jobs/jobs-core.classif";
        }

        public static final class Test {
            public static final String NONJOBS = "/classification/test/jobs/nonjobs.classif";
            public static final String JOBS = "/classification/test/jobs/jobs.classif";
        }
    }

    public static final class Programming {
        public static final class Training {
            public static final String NONPROGRAMMING = "/classification/programming/nonprogramming.classif";
            public static final String PROGRAMMING = "/classification/programming/programming.classif";
        }

        public static final class Test {
            public static final String NONPROGRAMMING = "/classification/test/programming/nonprogramming.classif";
            public static final String PROGRAMMING = "/classification/test/programming/programming.classif";
        }
    }

    public static final class Other {
        public static final String SAMPLE = "/classification/sample.classif";
    }

    private ClassificationData() {
        throw new AssertionError();
    }

    // API

    public static final class JobsDataApi {

        // training data - jobs

        public static final List<NamedVector> jobsVsNonJobsTrainingDataDefault() throws IOException {
            return JobsDataApi.jobsVsNonJobsTrainingDataShuffled(PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
        }

        public static final List<NamedVector> jobsVsNonJobsTrainingDataShuffled(final int probes, final int features) throws IOException {
            final List<NamedVector> nonJobsVectors = JobsDataApi.nonJobsTrainingData(probes, features);
            final List<NamedVector> jobsNamedVectors = JobsDataApi.jobsTrainingData(probes, features);
            return oneVsAnotherTrainingDataShuffled(probes, features, nonJobsVectors, jobsNamedVectors);
        }

        public static final List<NamedVector> jobsVsNonJobsCoreTrainingDataShuffled(final int probes, final int features) throws IOException {
            final List<NamedVector> nonJobsVectors = JobsDataApi.nonJobsCoreTrainingData(probes, features);
            final List<NamedVector> jobsNamedVectors = JobsDataApi.jobsCoreTrainingData(probes, features);
            return oneVsAnotherTrainingDataShuffled(probes, features, nonJobsVectors, jobsNamedVectors);
        }

        public static final List<NamedVector> jobsVsNonJobsFullTrainingDataShuffled(final int probes, final int features) throws IOException {
            final List<NamedVector> nonJobsVectors = JobsDataApi.nonJobsFullTrainingData(probes, features);
            final List<NamedVector> jobsNamedVectors = JobsDataApi.jobsFullTrainingData(probes, features);
            return oneVsAnotherTrainingDataShuffled(probes, features, nonJobsVectors, jobsNamedVectors);
        }

        static final List<NamedVector> jobsTrainingData(final int probes, final int features) throws IOException {
            return new SimpleDataLoadingStrategy(Jobs.Training.JOBS, JOB).loadData(probes, features);
        }

        static final List<NamedVector> nonJobsTrainingData(final int probes, final int features) throws IOException {
            return new SimpleDataLoadingStrategy(Jobs.Training.NONJOBS, NONJOB).loadData(probes, features);
        }

        static final List<NamedVector> jobsCoreTrainingData(final int probes, final int features) throws IOException {
            return new SimpleDataLoadingStrategy(Jobs.Training.JOBS_CORE, JOB).loadData(probes, features);
        }

        static final List<NamedVector> nonJobsCoreTrainingData(final int probes, final int features) throws IOException {
            return new SimpleDataLoadingStrategy(Jobs.Training.NONJOBS_CORE, NONJOB).loadData(probes, features);
        }

        static final List<NamedVector> jobsFullTrainingData(final int probes, final int features) throws IOException {
            return new SimpleDataLoadingStrategy(Jobs.Training.JOBS_FULL, JOB).loadData(probes, features);
        }

        static final List<NamedVector> nonJobsFullTrainingData(final int probes, final int features) throws IOException {
            return new SimpleDataLoadingStrategy(Jobs.Training.NONJOBS_FULL, NONJOB).loadData(probes, features);
        }

        // test data - jobs

        static final List<ImmutablePair<String, String>> jobsTestData() throws IOException {
            return testData(Jobs.Test.JOBS, JOB);
        }

        static final List<ImmutablePair<String, String>> nonJobsTestData() throws IOException {
            return testData(Jobs.Test.NONJOBS, NONJOB);
        }

    }

    public static final class ProgrammingDataApi {

        // training data - programming

        public static final List<NamedVector> programmingVsNonProgrammingTrainingDataDefault() throws IOException {
            return ProgrammingDataApi.programmingVsNonProgrammingTrainingDataShuffled(PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
        }

        public static final List<NamedVector> programmingVsNonProgrammingTrainingDataShuffled(final int probes, final int features) throws IOException {
            final List<NamedVector> nonProgrammingVectors = ProgrammingDataApi.nonProgrammingTrainingData(probes, features);
            final List<NamedVector> programmingNamedVectors = ProgrammingDataApi.programmingTrainingData(probes, features);
            return oneVsAnotherTrainingDataShuffled(probes, features, nonProgrammingVectors, programmingNamedVectors);
        }

        static final List<NamedVector> programmingTrainingData(final int probes, final int features) throws IOException {
            return new SimpleDataLoadingStrategy(Programming.Training.PROGRAMMING, PROGRAMMING).loadData(probes, features);
        }

        static final List<NamedVector> nonProgrammingTrainingData(final int probes, final int features) throws IOException {
            return new SimpleDataLoadingStrategy(Programming.Training.NONPROGRAMMING, NONPROGRAMMING).loadData(probes, features);
        }

        // test data - programming

        static final List<ImmutablePair<String, String>> programmingTestData() throws IOException {
            return testData(Programming.Test.PROGRAMMING, PROGRAMMING);
        }

        static final List<ImmutablePair<String, String>> nonProgrammingTestData() throws IOException {
            return testData(Programming.Test.NONPROGRAMMING, NONPROGRAMMING);
        }

    }

    public static final class CommercialDataApi {

        // training data - commercial

        public static final List<NamedVector> commercialVsNonCommercialTrainingDataDefault() throws IOException {
            return ClassificationData.CommercialDataApi.commercialVsNonCommercialTrainingDataShuffled(PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
        }

        public static final List<NamedVector> commercialVsNonCommercialTrainingDataShuffled(final int probes, final int features) throws IOException {
            final List<NamedVector> nonCommercialVectors = ClassificationData.CommercialDataApi.nonCommercialTrainingData(probes, features);
            final List<NamedVector> commercialNamedVectors = ClassificationData.CommercialDataApi.commercialTrainingData(probes, features);
            return oneVsAnotherTrainingDataShuffled(probes, features, nonCommercialVectors, commercialNamedVectors);
        }

        // training data - commercial

        /**
         * - covers: (936)deal-toreject.txt, (38)deals-toreject.txt, (109)win-toreject.txt
         */
        static final List<NamedVector> commercialTrainingData(final int probes, final int features) throws IOException {
            return new SimpleDataLoadingStrategy(Commercial.Training.COMMERCIAL, COMMERCIAL).loadData(probes, features);
        }

        /**
         * - covers: (291)deal-toaccept.txt, (148)deals-toaccept.txt, (303)win-toaccept.txt
         */
        static final List<NamedVector> nonCommercialTrainingData(final int probes, final int features) throws IOException {
            return new SimpleDataLoadingStrategy(Commercial.Training.NONCOMMERCIAL, NONCOMMERCIAL).loadData(probes, features);
        }

        // test data - commercial

        static final List<ImmutablePair<String, String>> commercialTestData() throws IOException {
            return testData(Commercial.Test.COMMERCIAL, COMMERCIAL);
        }

        static final List<ImmutablePair<String, String>> nonCommercialTestData() throws IOException {
            return testData(Commercial.Test.NONCOMMERCIAL, NONCOMMERCIAL);
        }
    }

}
