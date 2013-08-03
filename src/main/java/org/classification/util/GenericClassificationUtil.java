package org.classification.util;

import static org.classification.util.ClassificationSettings.FEATURES;
import static org.classification.util.ClassificationSettings.PROBES_FOR_CONTENT_ENCODER_VECTOR;

import java.io.IOException;

import org.apache.mahout.classifier.sgd.AdaptiveLogisticRegression;
import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.AdaptiveWordValueEncoder;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;

import com.google.api.client.util.Preconditions;

public final class GenericClassificationUtil {

    private static final int NUMBER_OF_CATEGORIES = 2;

    private GenericClassificationUtil() {
        throw new AssertionError();
    }

    // encoding

    public static NamedVector encodeWithTypeInfoDefault(final String type, final Iterable<String> words) {
        return encodeWithTypeInfo(type, words, PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
    }

    /**
     * The following are encoded: the type of content and the actual bag of words
     * The label argument to the NamedVector is either commercial or non-commercial
     */
    static NamedVector encodeWithTypeInfo(final String type, final Iterable<String> words, final int probesForContent, final int features) {
        final FeatureVectorEncoder content_encoder = new AdaptiveWordValueEncoder("content");
        content_encoder.setProbes(probesForContent);

        final FeatureVectorEncoder type_encoder = new StaticWordValueEncoder("type");
        type_encoder.setProbes(1);

        final Vector vect = new RandomAccessSparseVector(features);
        type_encoder.addToVector(type, vect);

        for (final String word : words) {
            content_encoder.addToVector(word, vect);
        }
        return new NamedVector(vect, type);
    }

    public static Vector encodeDefault(final Iterable<String> words) {
        return encode(words, PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
    }

    public static Vector encode(final Iterable<String> words, final int probesForEncodingContent, final int features) {
        final FeatureVectorEncoder content_encoder = new AdaptiveWordValueEncoder("content");
        content_encoder.setProbes(probesForEncodingContent);

        final Vector vect = new RandomAccessSparseVector(features);
        for (final String word : words) {
            content_encoder.addToVector(word, vect);
        }
        return vect;
    }

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

    // public static SequenceFile.Reader readBackData(final String pathOnDisk) throws IOException {
    // final URI path = URI.create(pathOnDisk);
    // final Configuration hconf = new Configuration();
    // final FileSystem fs = FileSystem.get(path, hconf);
    //
    // final SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path(path), hconf);
    // return reader;
    // }

    // classifier

    public static AdaptiveLogisticRegression trainGenericClassifier(final String nameOfType, final Iterable<NamedVector> vectors, final int features, final int learners) throws IOException {
        final AdaptiveLogisticRegression metaLearner = new AdaptiveLogisticRegression(NUMBER_OF_CATEGORIES, features, new L1());
        Preconditions.checkState(learners > 25);
        metaLearner.setPoolSize(learners);
        for (final NamedVector vect : vectors) {
            metaLearner.train(nameOfType.equals(vect.getName()) ? 1 : 0, vect); // example: nameOfType = COMMERCIAL
        }
        metaLearner.close();
        return metaLearner;
    }

}
