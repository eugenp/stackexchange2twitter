package org.common.service.classification;

import static org.common.classification.ClassificationUtil.COMMERCIAL;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.common.classification.ClassificationData;
import org.common.spring.CommonContextConfig;
import org.gplus.spring.GplusContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class, GplusContextConfig.class })
public class ClassificationServiceLiveTest {

    @Autowired
    private ClassificationService classificationService;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoException() {
        //
    }

    @Test
    public final void whenReadingClassificationTrainingFile_thenNoException() throws IOException {
        final List<String> lines = IOUtils.readLines(new BufferedReader(new FileReader("src/main/resources/classification/commercial.classif")));
        System.out.println(lines);
    }

    @Test
    public final void whenTweetIsClassified_thenNoException() {
        final boolean isCommercial = classificationService.isCommercial(COMMERCIAL, "URGENT: Scala Developer | 3 Month Contract | Westminster | Immediate Requirement! #Scala #Freelance #Jobs #IT");
        assertTrue(isCommercial);
    }

    @Test
    public final void givenClassifierWasTrained_whenClassifyingTestData_thenResultsAreGood() throws IOException {
        final int runs = 500;
        final List<Double> results = Lists.newArrayList();
        for (int i = 0; i < runs; i++) {
            final List<ImmutablePair<String, String>> testData = ClassificationData.commercialAndNonCommercialTweets();
            final double percentageCorrect = analyzeData(testData);
            results.add(percentageCorrect);
        }

        final DescriptiveStatistics stats = new DescriptiveStatistics();
        for (int i = 0; i < results.size(); i++) {
            stats.addValue(results.get(i));
        }
        final double mean = stats.getMean();
        System.out.println("Average Success Rate: " + mean);
    }

    @Test
    public final void givenClassifierWasTrained_whenClassifyingTestDataNew_thenResultsAreGood() throws IOException {
        final int runs = 500;
        final List<Double> results = Lists.newArrayList();
        for (int i = 0; i < runs; i++) {
            final List<ImmutablePair<String, String>> testData = ClassificationData.commercialAndNonCommercialTweets();
            final double percentageCorrect = analyzeDataNew(testData);
            results.add(percentageCorrect);
        }

        final DescriptiveStatistics stats = new DescriptiveStatistics();
        for (int i = 0; i < results.size(); i++) {
            stats.addValue(results.get(i));
        }
        final double mean = stats.getMean();
        System.out.println("Average Success Rate: " + mean);
    }

    // util

    private final double analyzeData(final List<ImmutablePair<String, String>> testData) throws IOException {
        classificationService.afterPropertiesSet();

        int correct = 0;
        int total = 0;
        for (final Pair<String, String> tweetData : testData) {
            total++;
            final boolean expected = COMMERCIAL.equals(tweetData.getLeft());
            final boolean isTweetCommercial = classificationService.isCommercial(tweetData.getLeft(), tweetData.getRight());
            if (isTweetCommercial == expected) {
                correct++;
            }
        }

        final double cd = correct;
        final double td = total;
        final double percentageCorrect = cd / td;
        return percentageCorrect;
    }

    private final double analyzeDataNew(final List<ImmutablePair<String, String>> testData) throws IOException {
        classificationService.afterPropertiesSet();

        int correct = 0;
        int total = 0;
        for (final Pair<String, String> tweetData : testData) {
            total++;
            final boolean expected = COMMERCIAL.equals(tweetData.getLeft());
            final boolean isTweetCommercial = classificationService.isCommercial(tweetData.getRight());
            if (isTweetCommercial == expected) {
                correct++;
            }
        }

        final double cd = correct;
        final double td = total;
        final double percentageCorrect = cd / td;
        return percentageCorrect;
    }

}
