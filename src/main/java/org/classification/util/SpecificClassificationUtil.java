package org.classification.util;

import static org.classification.util.ClassificationSettings.FEATURES;
import static org.classification.util.ClassificationSettings.LEARNERS_IN_THE_CLASSIFIER_POOL;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
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

    // encoding

    // data read/write to disk

    // public static VectorWriter writeData(final String pathOnDisk) throws IOException {
    // final URI path = URI.create(pathOnDisk);
    // final Configuration hconf = new Configuration();
    // final FileSystem fs = FileSystem.get(path, hconf);
    //
    // final SequenceFile.Writer writer = SequenceFile.createWriter(fs, hconf, new Path(path), LongWritable.class, VectorWritable.class);
    // final VectorWriter vw = new SequenceFileVectorWriter(writer);
    // return vw;
    // }

    public static SequenceFile.Reader readBackData(final String pathOnDisk) throws IOException {
        final URI path = URI.create(pathOnDisk);
        final Configuration hconf = new Configuration();
        final FileSystem fs = FileSystem.get(path, hconf);

        final SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path(path), hconf);
        return reader;
    }

    // classifier

    public static CrossFoldLearner commercialVsNonCommercialBestLearner(final int probes, final int features) throws IOException {
        final List<NamedVector> learningData = ClassificationDataUtil.commercialVsNonCommercialTrainingData(probes, features);
        final AdaptiveLogisticRegression classifier = SpecificClassificationUtil.trainCommercialClassifier(learningData, features, LEARNERS_IN_THE_CLASSIFIER_POOL);
        final CrossFoldLearner bestLearner = classifier.getBest().getPayload().getLearner();

        return bestLearner;
    }

    public static CrossFoldLearner programmingVsNonProgrammingBestLearner(final int probes, final int features) throws IOException {
        final List<NamedVector> trainingData = ClassificationDataUtil.programmingVsNonProgrammingTrainingData(probes, features);
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
