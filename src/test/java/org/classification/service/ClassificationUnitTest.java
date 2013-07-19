package org.classification.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.classification.util.ClassificationUtil.NONCOMMERCIAL;
import static org.classification.util.ClassificationUtil.encodeWithTypeInfoDefault;
import static org.classification.util.ClassificationUtil.readBackData;
import static org.classification.util.ClassificationUtil.writeData;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.mahout.classifier.sgd.AdaptiveLogisticRegression;
import org.apache.mahout.classifier.sgd.ModelSerializer;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.utils.vectors.io.VectorWriter;
import org.classification.util.ClassificationTrainingDataUtil;
import org.classification.util.ClassificationUtil;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class ClassificationUnitTest {
    private static final String VECTOR_FILE_ON_DISK = "file:/tmp/spammery.seq";
    private static final String CLASSIFIER_FILE_ON_DISK = "/tmp/classif.ier";

    // tests

    @Test
    public final void whenTextIsEncodedAsVector2_thenNoExceptions() throws IOException {
        encodeWithTypeInfoDefault(randomAlphabetic(4), Lists.newArrayList(randomAlphabetic(5), randomAlphabetic(4)));
    }

    @Test
    public final void whenLoadingClassificationData_thenNoExceptions() throws IOException {
        writeData(VECTOR_FILE_ON_DISK);
    }

    @Test
    public final void whenLoadingClassificationData_thenOperationCorrect() throws IOException {
        assertNotNull(writeData(VECTOR_FILE_ON_DISK));
    }

    @Test
    public final void givenDataIsLoaded_whenWriterIsUsed_thenNoExceptions() throws IOException {
        final List<Vector> vectors = vectors();

        final VectorWriter vectorWriter = writeData(VECTOR_FILE_ON_DISK);
        vectorWriter.write(vectors);
        vectorWriter.close();
    }

    @Test
    @Ignore("temporary")
    public final void givenDataWasWritten_whenDataIsReadBack_thenNoExceptions() throws IOException {
        final String filePathOnDisk = "file:/tmp/" + randomAlphabetic(5) + ".seq";

        writeData(filePathOnDisk);

        final Reader reader = readBackData(filePathOnDisk);
        final LongWritable key = new LongWritable();
        final VectorWritable value = new VectorWritable();
        while (reader.next(key, value)) {
            final NamedVector vector = (NamedVector) value.get();
            System.out.println(vector);
        }
    }

    @Test
    public final void whenVectorIsCreatedWrittenAndReadBack_theVectorRemainsTheSame() throws IOException {
        final Vector originalVector = encodeWithTypeInfoDefault(NONCOMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split("How to travel around the world for a year http://blog.alexmaccaw.com/how-to-travel-around-the-world-for-a-year/"));

        // write
        final VectorWriter vectorWriter = writeData(VECTOR_FILE_ON_DISK);
        vectorWriter.write(originalVector);
        vectorWriter.close();

        // read back
        final Reader reader = readBackData(VECTOR_FILE_ON_DISK);
        final VectorWritable value = new VectorWritable();
        reader.next(new LongWritable(), value);

        // compare
        final NamedVector retrievedVector = (NamedVector) value.get();
        assertThat(retrievedVector, equalTo(originalVector));
    }

    // training

    @Test
    public final void whenClassifierIsTrained_thenNoExceptions() throws IOException {
        final List<NamedVector> vectors = learningData();

        ClassificationUtil.trainClassifierDefault(vectors);
    }

    @Test
    public final void givenClassifierWasTrained_whenPersistedToDisk_thenNoExceptions() throws IOException {
        final List<NamedVector> vectors = learningData();
        final AdaptiveLogisticRegression classifier = ClassificationUtil.trainClassifierDefault(vectors);

        ModelSerializer.writeBinary(CLASSIFIER_FILE_ON_DISK, classifier.getBest().getPayload().getLearner());
    }

    // util

    private final List<Vector> vectors() throws IOException {
        final List<NamedVector> namedVectors = learningData();
        final List<Vector> vectors = Lists.<Vector> newArrayList(namedVectors);
        return vectors;
    }

    private final List<NamedVector> learningData() throws IOException {
        return ClassificationTrainingDataUtil.commercialVsNonCommercialLearningDataDefault();
    }

}
