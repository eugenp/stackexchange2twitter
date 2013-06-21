package org.common.classification;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.utils.vectors.io.SequenceFileVectorWriter;
import org.apache.mahout.utils.vectors.io.VectorWriter;
import org.apache.mahout.vectorizer.encoders.AdaptiveWordValueEncoder;
import org.apache.mahout.vectorizer.encoders.ConstantValueEncoder;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;
import org.apache.mahout.vectorizer.encoders.TextValueEncoder;

public final class ClassificationUtil {

    static final TextValueEncoder encoder = new TextValueEncoder("body");
    private static final FeatureVectorEncoder bias = new ConstantValueEncoder("Intercept");
    public static final int FEATURES = 1000;
    public static final String[] LEAK_LABELS = { "none", "month-year", "day-month-year" };

    private ClassificationUtil() {
        throw new AssertionError();
    }

    // util

    /**
     * The following are encoded: the type of content and the actual bag of words
     * The label argument to the NamedVector is either “commercial” or “non-commercial”
     */
    public static Vector encode(final String type, final Iterable<String> words) throws IOException {
        final FeatureVectorEncoder content_encoder = new AdaptiveWordValueEncoder("content");
        content_encoder.setProbes(2);

        final FeatureVectorEncoder type_encoder = new StaticWordValueEncoder("type");
        type_encoder.setProbes(2);

        final Vector vect = new RandomAccessSparseVector(ClassificationUtil.FEATURES);
        type_encoder.addToVector(type, vect);

        for (final String word : words) {
            content_encoder.addToVector(word, vect);
        }
        return new NamedVector(vect, "label");
    }

    public static Vector encodeIncomplete(final String text) {
        ClassificationUtil.encoder.addText(text.toLowerCase());
        final Vector vect = new RandomAccessSparseVector(ClassificationUtil.FEATURES);
        ClassificationUtil.bias.addToVector((byte[]) null, 1, vect);
        ClassificationUtil.encoder.flush(1, vect);
        return vect;
    }

    public static VectorWriter loadData(final String pathOnDisk) throws IOException {
        final URI path = URI.create(pathOnDisk);
        final Configuration hconf = new Configuration();
        final FileSystem fs = FileSystem.get(path, hconf);

        final SequenceFile.Writer writer = SequenceFile.createWriter(fs, hconf, new Path(path), LongWritable.class, VectorWritable.class);
        final VectorWriter vw = new SequenceFileVectorWriter(writer);
        return vw;
    }

    public static void readBackData(final String pathOnDisk) throws IOException {
        final URI path = URI.create(pathOnDisk);
        final Configuration hconf = new Configuration();
        final FileSystem fs = FileSystem.get(path, hconf);

        final SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path(path), hconf);

        final LongWritable key = new LongWritable();
        final VectorWritable value = new VectorWritable();
        while (reader.next(key, value)) {
            final NamedVector vector = (NamedVector) value.get();
            // do stuff with v
        }
    }

}
