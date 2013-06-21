package org.common.classification;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.common.classification.ClassificationUtil.COMMERCIAL;
import static org.common.classification.ClassificationUtil.NONCOMMERCIAL;
import static org.common.classification.ClassificationUtil.encode;
import static org.common.classification.ClassificationUtil.loadData;
import static org.common.classification.ClassificationUtil.readBackData;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.mahout.classifier.sgd.AdaptiveLogisticRegression;
import org.apache.mahout.classifier.sgd.CrossFoldLearner;
import org.apache.mahout.classifier.sgd.ModelSerializer;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.utils.vectors.io.VectorWriter;
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
        encode(randomAlphabetic(4), Lists.newArrayList(randomAlphabetic(5), randomAlphabetic(4)));
    }

    @Test
    public final void whenLoadingClassificationData_thenNoExceptions() throws IOException {
        loadData(VECTOR_FILE_ON_DISK);
    }

    @Test
    public final void whenLoadingClassificationData_thenOperationCorrect() throws IOException {
        assertNotNull(loadData(VECTOR_FILE_ON_DISK));
    }

    @Test
    public final void givenDataIsLoaded_whenWriterIsUsed_thenNoExceptions() throws IOException {
        final List<Vector> vectors = vectors();

        final VectorWriter vectorWriter = loadData(VECTOR_FILE_ON_DISK);
        vectorWriter.write(vectors);
        vectorWriter.close();
    }

    @Test
    public final void givenDataWasWritten_whenDataIsReadBack_thenNoExceptions() throws IOException {
        final Reader reader = readBackData(VECTOR_FILE_ON_DISK);

        final LongWritable key = new LongWritable();
        final VectorWritable value = new VectorWritable();
        while (reader.next(key, value)) {
            final NamedVector vector = (NamedVector) value.get();
            System.out.println(vector);
        }
    }

    @Test
    public final void whenVectorIsCreatedWrittenAndReadBack_theVectorRemainsTheSame() throws IOException {
        final Vector originalVector = encode(NONCOMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split("How to travel around the world for a year http://blog.alexmaccaw.com/how-to-travel-around-the-world-for-a-year/"));

        // write
        final VectorWriter vectorWriter = loadData(VECTOR_FILE_ON_DISK);
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
        final List<NamedVector> vectors = namedVectors();

        ClassificationUtil.trainClassifier(vectors);
    }

    @Test
    public final void givenClassifierWasTrained_whenPersistedToDisk_thenNoExceptions() throws IOException {
        final List<NamedVector> vectors = namedVectors();
        final AdaptiveLogisticRegression classifier = ClassificationUtil.trainClassifier(vectors);

        ModelSerializer.writeBinary(CLASSIFIER_FILE_ON_DISK, classifier.getBest().getPayload().getLearner());
    }

    // usage

    @Test
    public final void givenClassifierWasTrained_whenUsingThePersistedToDisk_thenNoExceptions() throws IOException {
        final List<NamedVector> vectors = namedVectors();
        final AdaptiveLogisticRegression classifier = ClassificationUtil.trainClassifier(vectors);
        final CrossFoldLearner bestLearner = classifier.getBest().getPayload().getLearner();

        int correct = 0;
        int total = 0;
        for (final NamedVector vect : vectors) {
            total++;
            final int expected = COMMERCIAL.equals(vect.getName()) ? 1 : 0;

            final Vector collector = new DenseVector(2);
            bestLearner.classifyFull(collector, vect);

            final int cat = collector.maxValueIndex();
            if (cat == expected) {
                correct++;
            }
        }

        final double cd = correct;
        final double td = total;
        System.out.println(cd / td);
    }

    // utils

    private final List<Vector> vectors() throws IOException {
        final List<NamedVector> namedVectors = namedVectors();
        final List<Vector> vectors = Lists.<Vector> newArrayList(namedVectors);
        return vectors;
    }

    private final List<NamedVector> namedVectors() throws IOException {
        final NamedVector noncommercial1 = encode(NONCOMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split("How to travel around the world for a year"));
        final NamedVector noncommercial2 = encode(NONCOMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split("What is nux and what's it used for?"));
        final NamedVector noncommercial3 = encode(NONCOMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split("What is the fastest way to learn #JPA? "));
        final NamedVector noncommercial4 = encode(NONCOMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split("How can I execute a script each time I log in and out from OS X?"));
        final NamedVector commercial1 = encode(COMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split("We're looking to #hire a Front End Developer/Creative Designer to join our team in Leeds. Get in touch for more information."));
        final NamedVector commercial2 = encode(COMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split("New job Nurse Practitioners & Physician Assistants - New Jersey  #hire #jobs"));
        final NamedVector commercial3 = encode(COMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split("Looking to hire excellent senior Java developers preferrably with MongoDb skills to our Espoo, Finland office #agile #hire #Java #MongoDb"));
        final NamedVector commercial4 = encode(COMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split("I'm looking for #scala dev #contract in #Melbourne or #Sydney. #job"));
        final NamedVector commercial5 = encode(COMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split("Scala Developer with World-Class Tech Co in Cambridge #job #scala #cambridge"));

        final List<NamedVector> vectors = Lists.newArrayList(noncommercial1, commercial1, noncommercial2, commercial2, noncommercial3, commercial3, noncommercial4, commercial4, commercial5);
        return vectors;
    }

}
