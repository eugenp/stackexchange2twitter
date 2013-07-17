package org.common.classification;

import static org.common.classification.ClassificationSettings.LEARNERS_IN_THE_CLASSIFIER_POOL;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.classifier.sgd.AdaptiveLogisticRegression;
import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.utils.vectors.io.SequenceFileVectorWriter;
import org.apache.mahout.utils.vectors.io.VectorWriter;
import org.apache.mahout.vectorizer.encoders.AdaptiveWordValueEncoder;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;

public final class ClassificationUtil {

    public static final String COMMERCIAL = "commercial";
    public static final String NONCOMMERCIAL = "noncommercial";
    private static final int NUMBER_OF_CATEGORIES = 2;

    private ClassificationUtil() {
        throw new AssertionError();
    }

    // util

    /**
     * The following are encoded: the type of content and the actual bag of words
     * The label argument to the NamedVector is either commercial or non-commercial
     */
    public static NamedVector encode(final String type, final Iterable<String> words) {
        final FeatureVectorEncoder content_encoder = new AdaptiveWordValueEncoder("content");
        content_encoder.setProbes(1);

        final FeatureVectorEncoder type_encoder = new StaticWordValueEncoder("type");
        type_encoder.setProbes(1);

        final Vector vect = new RandomAccessSparseVector(ClassificationSettings.FEATURES);
        type_encoder.addToVector(type, vect);

        for (final String word : words) {
            content_encoder.addToVector(word, vect);
        }
        return new NamedVector(vect, type);
    }

    public static Vector encode(final Iterable<String> words) {
        final FeatureVectorEncoder content_encoder = new AdaptiveWordValueEncoder("content");
        content_encoder.setProbes(2);

        final Vector vect = new RandomAccessSparseVector(ClassificationSettings.FEATURES);
        for (final String word : words) {
            content_encoder.addToVector(word, vect);
        }
        return vect;
    }

    public static VectorWriter writeData(final String pathOnDisk) throws IOException {
        final URI path = URI.create(pathOnDisk);
        final Configuration hconf = new Configuration();
        final FileSystem fs = FileSystem.get(path, hconf);

        final SequenceFile.Writer writer = SequenceFile.createWriter(fs, hconf, new Path(path), LongWritable.class, VectorWritable.class);
        final VectorWriter vw = new SequenceFileVectorWriter(writer);
        return vw;
    }

    public static SequenceFile.Reader readBackData(final String pathOnDisk) throws IOException {
        final URI path = URI.create(pathOnDisk);
        final Configuration hconf = new Configuration();
        final FileSystem fs = FileSystem.get(path, hconf);

        final SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path(path), hconf);
        return reader;
    }

    public static AdaptiveLogisticRegression trainClassifier(final Iterable<NamedVector> vectors) throws IOException {
        final AdaptiveLogisticRegression metaLearner = new AdaptiveLogisticRegression(NUMBER_OF_CATEGORIES, ClassificationSettings.FEATURES, new L1());
        metaLearner.setPoolSize(LEARNERS_IN_THE_CLASSIFIER_POOL);
        for (final NamedVector vect : vectors) {
            metaLearner.train(COMMERCIAL.equals(vect.getName()) ? 1 : 0, vect);
        }
        metaLearner.close();
        return metaLearner;
    }

    static void trainClassifier(final SequenceFile.Reader reader) throws IOException {
        final AdaptiveLogisticRegression metaLearner = new AdaptiveLogisticRegression(NUMBER_OF_CATEGORIES, ClassificationSettings.FEATURES, new L1());
        metaLearner.setPoolSize(LEARNERS_IN_THE_CLASSIFIER_POOL);
        final VectorWritable value = new VectorWritable();
        while (reader.next(new LongWritable(), value)) {
            final NamedVector v = (NamedVector) value.get();
            metaLearner.train(COMMERCIAL.equals(v.getName()) ? 1 : 0, v);
        }
        metaLearner.close();
    }

}
