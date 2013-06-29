package org.common.classification;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.common.classification.ClassificationUtil.COMMERCIAL;
import static org.common.classification.ClassificationUtil.NONCOMMERCIAL;
import static org.common.classification.ClassificationUtil.encode;
import static org.common.classification.ClassificationUtil.readBackData;
import static org.common.classification.ClassificationUtil.writeData;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
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
        encode(randomAlphabetic(4), Lists.newArrayList(randomAlphabetic(5), randomAlphabetic(4)));
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
        final Vector originalVector = encode(NONCOMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split("How to travel around the world for a year http://blog.alexmaccaw.com/how-to-travel-around-the-world-for-a-year/"));

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

        ClassificationUtil.trainClassifier(vectors);
    }

    @Test
    public final void givenClassifierWasTrained_whenPersistedToDisk_thenNoExceptions() throws IOException {
        final List<NamedVector> vectors = learningData();
        final AdaptiveLogisticRegression classifier = ClassificationUtil.trainClassifier(vectors);

        ModelSerializer.writeBinary(CLASSIFIER_FILE_ON_DISK, classifier.getBest().getPayload().getLearner());
    }

    // usage

    @Test
    public final void givenClassifierWasTrained_whenUsingThePersistedToDisk_thenNoExceptions() throws IOException {
        final List<NamedVector> learningData = learningData();
        final List<NamedVector> testData = testData();
        final AdaptiveLogisticRegression classifier = ClassificationUtil.trainClassifier(learningData);
        final CrossFoldLearner bestLearner = classifier.getBest().getPayload().getLearner();

        int correct = 0;
        int total = 0;
        for (final NamedVector vect : testData) {
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
        final List<NamedVector> namedVectors = learningData();
        final List<Vector> vectors = Lists.<Vector> newArrayList(namedVectors);
        return vectors;
    }

    private final List<NamedVector> learningData() throws IOException {
        final List<String> noncommercialTweets = IOUtils.readLines(new BufferedReader(new FileReader("src/main/resources/noncommercial.classif")));
        final List<String> commercialTweets = IOUtils.readLines(new BufferedReader(new FileReader("src/main/resources/commercial.classif")));

        final List<NamedVector> noncommercialNamedVectors = Lists.<NamedVector> newArrayList();
        final List<NamedVector> commercialNamedVectors = Lists.<NamedVector> newArrayList();
        for (final String noncommercialTweet : noncommercialTweets) {
            noncommercialNamedVectors.add(encode(NONCOMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split(noncommercialTweet)));
        }
        for (final String commercialTweet : commercialTweets) {
            noncommercialNamedVectors.add(encode(COMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split(commercialTweet)));
        }

        final List<NamedVector> allNamedVectors = Lists.<NamedVector> newArrayList();
        allNamedVectors.addAll(commercialNamedVectors);
        allNamedVectors.addAll(noncommercialNamedVectors);
        Collections.shuffle(allNamedVectors);
        return allNamedVectors;
    }

    private final List<NamedVector> testData() throws IOException {
        final List<String> noncommercialTweets = Lists.newArrayList(// @formatter:off
                "A set of great #scala and #akka examples http://bit.ly/KlJkro  #java #mapred #programming #dev", 
                "New report shows that 11% of #Java devs also use #Scala on some projects http://0t.ee/devprodrep2012c", 
                "What features of #java have been dropped in #scala? http://www.javacodegeeks.com/2011/08/what-features-of-java-have-been-dropped.html …", 
                "Why should a #Java #developer learn #Scala? To write better Java, says @jessitron: http://bit.ly/13ReUfU  via @JAXenter", 
                "#Mixin in #Java with Aspects – for a #Scala traits sample http://buff.ly/10nAOks", 
                "The Play Framework at LinkedIn #java #linkedin #scala http://es.slideshare.net/brikis98/the-play-framework-at-linkedin …", 
                "Why Scala is terser than Java? http://sortega.github.io/development/2013/06/22/terseness/ … #Scala #Java", 
                "So @springrod 'says' there is more junk in #scala libs than in #java ones, I want to see evidence for that(45:10): http://www.parleys.com", 
                "What are the popular code conventions based on some @github hosted code ? http://sideeffect.kr/popularconvention/ … (via @heyitsnoah)", 
                "Good Presentation. The #PlayFramework at #LinkedIn: Productivity and Performance at Scale: http://youtu.be/8z3h4Uv9YbE  #Scala #Java" 
        ); // @formatter:on
        final List<String> commercialTweets = Lists.newArrayList(// @formatter:off
                "We're looking to #hire a Front End Developer/Creative Designer to join our team in Leeds. Get in touch for more information.", 
                "New job Nurse Practitioners & Physician Assistants - New Jersey  #hire #jobs", 
                "Looking to hire excellent senior Java developers preferrably with MongoDb skills to our Espoo, Finland office #agile #hire #Java #MongoDb",
                "I'm looking for #scala dev #contract in #Melbourne or #Sydney. #job", 
                "Scala Developer with World-Class Tech Co in Cambridge #job #scala #cambridge", 
                "Hiring a C# or Java Software Developer / Programmer - London in London, United Kingdom http://bull.hn/l/119NU/3  #job #.net #Java", 
                "We have lots of #java engineer #job opportunities available! http://bit.ly/10b6Oap", 
                "Know anyone for this job? JAVA/OLYMPIC consultant, german fluent in Luxembourg City, Luxembourg http://bull.hn/l/YWLX/5  #job #java", 
                "Looking for a Senior Java Developer in Hoboken, NJ http://bull.hn/l/12MHI/  #job #java", 
                "Know anyone for this job? Sr. Java Software Engineer in Atlanta, GA http://bull.hn/l/12D6I/5  #job #java", 
                "Are you a good fit for this job? Java Backend Developer in Amsterdam, Netherlands http://bull.hn/l/XVTE/6  #job #java #amsterdam" 
        ); // @formatter:on

        final List<NamedVector> noncommercialNamedVectors = Lists.<NamedVector> newArrayList();
        final List<NamedVector> commercialNamedVectors = Lists.<NamedVector> newArrayList();
        for (final String noncommercialTweet : noncommercialTweets) {
            noncommercialNamedVectors.add(encode(NONCOMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split(noncommercialTweet)));
        }
        for (final String commercialTweet : commercialTweets) {
            noncommercialNamedVectors.add(encode(COMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split(commercialTweet)));
        }

        final List<NamedVector> allNamedVectors = Lists.<NamedVector> newArrayList();
        allNamedVectors.addAll(commercialNamedVectors);
        allNamedVectors.addAll(noncommercialNamedVectors);
        Collections.shuffle(allNamedVectors);
        return allNamedVectors;
    }
}
