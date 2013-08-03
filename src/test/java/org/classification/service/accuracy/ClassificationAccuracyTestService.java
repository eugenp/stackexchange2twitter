package org.classification.service.accuracy;

import static org.classification.util.ClassificationSettings.FEATURES;
import static org.classification.util.ClassificationSettings.PROBES_FOR_CONTENT_ENCODER_VECTOR;
import static org.classification.util.SpecificClassificationUtil.COMMERCIAL;
import static org.classification.util.SpecificClassificationUtil.PROGRAMMING;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.mahout.classifier.sgd.CrossFoldLearner;
import org.classification.service.ClassificationService;
import org.classification.util.ClassificationTestData;
import org.classification.util.SpecificClassificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class ClassificationAccuracyTestService {

    @Autowired
    private ClassificationService classificationService;

    public ClassificationAccuracyTestService() {
        super();
    }

    // API

    public final double calculateCommercialClassifierAccuracyDefault(final int runs) throws IOException {
        return calculateCommercialClassifierAccuracy(runs, PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
    }

    public final double calculateProgrammingClassifierAccuracyDefault(final int runs) throws IOException {
        return calculateProgrammingClassifierAccuracy(runs, PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
    }

    final double calculateCommercialClassifierAccuracy(final int runs, final int probes, final int features) throws IOException {
        final long start = System.nanoTime() / (1000 * 1000 * 1000);
        final List<Double> results = Lists.newArrayList();
        for (int i = 0; i < runs; i++) {
            final List<ImmutablePair<String, String>> testData = ClassificationTestData.commercialAndNonCommercialTestData();
            final double percentageCorrect = analyzeCommercialData(testData, probes, features);
            results.add(percentageCorrect);
            if (i % 100 == 0) {
                System.out.println("Processing 100 ... - " + ((i / 100) + 1));
            }
        }

        final DescriptiveStatistics stats = new DescriptiveStatistics();
        for (final Double stat : results) {
            stats.addValue(stat);
        }
        final double mean = stats.getMean();
        final long end = System.nanoTime() / (1000 * 1000 * 1000);
        System.out.println("Processing time: " + (end - start) + " sec");
        return mean;
    }

    final double calculateProgrammingClassifierAccuracy(final int runs, final int probes, final int features) throws IOException {
        final long start = System.nanoTime() / (1000 * 1000 * 1000);
        final List<Double> results = Lists.newArrayList();
        for (int i = 0; i < runs; i++) {
            final List<ImmutablePair<String, String>> testData = ClassificationTestData.programmingAndNonProgrammingTestData();
            final double percentageCorrect = analyzeProgrammingData(testData, probes, features);
            results.add(percentageCorrect);
            if (i % 100 == 0) {
                System.out.println("Processing 100 ... - " + ((i / 100) + 1));
            }
        }

        final DescriptiveStatistics stats = new DescriptiveStatistics();
        for (final Double stat : results) {
            stats.addValue(stat);
        }
        final double mean = stats.getMean();
        final long end = System.nanoTime() / (1000 * 1000 * 1000);
        System.out.println("Processing time: " + (end - start) + " sec");
        return mean;
    }

    // util

    private final double analyzeCommercialData(final List<ImmutablePair<String, String>> testData, final int probes, final int features) throws IOException {
        final CrossFoldLearner bestLearner = SpecificClassificationUtil.commercialVsNonCommercialBestLearner(probes, features);
        classificationService.setCommercialVsNonCommercialLerner(bestLearner);

        int correct = 0;
        int total = 0;
        for (final Pair<String, String> tweetData : testData) {
            total++;
            final boolean expected = COMMERCIAL.equals(tweetData.getLeft());
            final boolean isTweetCommercial = classificationService.isCommercial(tweetData.getRight(), probes, features);
            if (isTweetCommercial == expected) {
                correct++;
            }
        }

        final double cd = correct;
        final double td = total;
        final double percentageCorrect = cd / td;
        return percentageCorrect;
    }

    private final double analyzeProgrammingData(final List<ImmutablePair<String, String>> testData, final int probes, final int features) throws IOException {
        final CrossFoldLearner bestLearner = SpecificClassificationUtil.programmingVsNonProgrammingBestLearner(probes, features);
        classificationService.setProgrammingVsNonProgrammingLerner(bestLearner);

        int correct = 0;
        int total = 0;
        for (final Pair<String, String> tweetData : testData) {
            total++;
            final boolean expected = PROGRAMMING.equals(tweetData.getLeft());
            final boolean isTweetCommercial = classificationService.isProgramming(tweetData.getRight(), probes, features);
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
