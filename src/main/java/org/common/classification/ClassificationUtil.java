package org.common.classification;

import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

public final class ClassificationUtil {

    private ClassificationUtil() {
        throw new AssertionError();
    }

    // util

    public static Vector encodeFeatureVector(final String text) {
        ClassificationIntegrationTest.encoder.addText(text.toLowerCase());
        final Vector v = new RandomAccessSparseVector(ClassificationIntegrationTest.FEATURES);
        ClassificationIntegrationTest.bias.addToVector((byte[]) null, 1, v);
        ClassificationIntegrationTest.encoder.flush(1, v);
        return v;
    }

}
