package org.classification.service;

import static org.classification.util.ClassificationSettings.FEATURES;
import static org.classification.util.ClassificationSettings.PROBES_FOR_CONTENT_ENCODER_VECTOR;
import static org.classification.util.ClassificationUtil.COMMERCIAL;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.mahout.classifier.sgd.CrossFoldLearner;
import org.classification.util.ClassificationData;
import org.classification.util.ClassificationUtil;
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

    public final double calculateClassifierAccuracyDefault(final int runs) throws IOException {
        return calculateClassifierAccuracy(runs, PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
    }

    public final double calculateClassifierAccuracy(final int runs, final int probes, final int features) throws IOException {
        final long start = System.nanoTime() / (1000 * 1000 * 1000);
        final List<Double> results = Lists.newArrayList();
        for (int i = 0; i < runs; i++) {
            final List<ImmutablePair<String, String>> testData = ClassificationData.commercialAndNonCommercialTweets();
            final double percentageCorrect = analyzeData(testData, probes, features);
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

    private final double analyzeData(final List<ImmutablePair<String, String>> testData, final int probes, final int features) throws IOException {
        final CrossFoldLearner bestLearner = ClassificationUtil.commercialVsNonCommercialBestLearner(probes, features);
        classificationService.setCommercialVsNonCommercialLerner(bestLearner);

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
