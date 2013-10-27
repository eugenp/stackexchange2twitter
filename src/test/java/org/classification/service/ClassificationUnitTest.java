package org.classification.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.classification.util.GenericClassificationUtil.encodeWithTypeInfoDefault;

import java.io.IOException;
import java.util.List;

import org.apache.mahout.classifier.sgd.AdaptiveLogisticRegression;
import org.apache.mahout.classifier.sgd.ModelSerializer;
import org.apache.mahout.math.NamedVector;
import org.classification.util.SpecificClassificationDataUtil;
import org.classification.util.SpecificClassificationUtil;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ClassificationUnitTest {
    // private static final String VECTOR_FILE_ON_DISK = "file:/tmp/spammery.seq";
    private static final String CLASSIFIER_FILE_ON_DISK = "/tmp/classif.ier";

    // tests

    @Test
    public final void whenTextIsEncodedAsVector2_thenNoExceptions() throws IOException {
        encodeWithTypeInfoDefault(randomAlphabetic(4), Lists.newArrayList(randomAlphabetic(5), randomAlphabetic(4)));
    }

    // @Test
    // public final void whenLoadingClassificationData_thenNoExceptions() throws IOException {
    // writeData(VECTOR_FILE_ON_DISK);
    // }
    //
    // @Test
    // public final void whenLoadingClassificationData_thenOperationCorrect() throws IOException {
    // assertNotNull(writeData(VECTOR_FILE_ON_DISK));
    // }
    //
    // @Test
    // public final void givenDataIsLoaded_whenWriterIsUsed_thenNoExceptions() throws IOException {
    // final List<Vector> vectors = vectors();
    //
    // final VectorWriter vectorWriter = writeData(VECTOR_FILE_ON_DISK);
    // vectorWriter.write(vectors);
    // vectorWriter.close();
    // }
    //
    // @Test
    // @Ignore("temporary")
    // public final void givenDataWasWritten_whenDataIsReadBack_thenNoExceptions() throws IOException {
    // final String filePathOnDisk = "file:/tmp/" + randomAlphabetic(5) + ".seq";
    //
    // writeData(filePathOnDisk);
    //
    // final Reader reader = readBackData(filePathOnDisk);
    // final LongWritable key = new LongWritable();
    // final VectorWritable value = new VectorWritable();
    // while (reader.next(key, value)) {
    // final NamedVector vector = (NamedVector) value.get();
    // System.out.println(vector);
    // }
    // }
    //
    // @Test
    // public final void whenVectorIsCreatedWrittenAndReadBack_theVectorRemainsTheSame() throws IOException {
    // final Vector originalVector = encodeWithTypeInfoDefault(NONCOMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split("How to travel around the world for a year http://blog.alexmaccaw.com/how-to-travel-around-the-world-for-a-year/"));
    //
    // // write
    // final VectorWriter vectorWriter = writeData(VECTOR_FILE_ON_DISK);
    // vectorWriter.write(originalVector);
    // vectorWriter.close();
    //
    // // read back
    // final Reader reader = readBackData(VECTOR_FILE_ON_DISK);
    // final VectorWritable value = new VectorWritable();
    // reader.next(new LongWritable(), value);
    //
    // // compare
    // final NamedVector retrievedVector = (NamedVector) value.get();
    // assertThat(retrievedVector, equalTo(originalVector));
    // }

    // training

    @Test
    // TODO: fix
    @Ignore("java heap space")
    public final void whenClassifierIsTrained_thenNoExceptions() throws IOException {
        final List<NamedVector> vectors = trainingData();

        SpecificClassificationUtil.trainCommercialClassifierDefault(vectors);
    }

    @Test
    @Ignore("long running")
    public final void givenClassifierWasTrained_whenPersistedToDisk_thenNoExceptions() throws IOException {
        final List<NamedVector> vectors = trainingData();
        final AdaptiveLogisticRegression classifier = SpecificClassificationUtil.trainCommercialClassifierDefault(vectors);

        ModelSerializer.writeBinary(CLASSIFIER_FILE_ON_DISK, classifier.getBest().getPayload().getLearner());
    }

    // util

    // private final List<Vector> vectors() throws IOException {
    // final List<NamedVector> namedVectors = trainingData();
    // final List<Vector> vectors = Lists.<Vector> newArrayList(namedVectors);
    // return vectors;
    // }

    private final List<NamedVector> trainingData() throws IOException {
        return SpecificClassificationDataUtil.jobsVsNonJobsTrainingDataDefault();
    }

}
