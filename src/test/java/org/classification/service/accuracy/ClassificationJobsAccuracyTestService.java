package org.classification.service.accuracy;

import static org.classification.util.ClassificationSettings.FEATURES;
import static org.classification.util.ClassificationSettings.PROBES_FOR_CONTENT_ENCODER_VECTOR;
import static org.classification.util.Classifiers.JOB;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.mahout.classifier.sgd.CrossFoldLearner;
import org.apache.mahout.math.NamedVector;
import org.classification.data.ClassificationData.JobsDataApi;
import org.classification.data.ClassificationTestData;
import org.classification.service.ClassificationService;
import org.classification.util.ClassificationSettings;
import org.classification.util.Classifiers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class ClassificationJobsAccuracyTestService {

    @Autowired
    private ClassificationService classificationService;

    public ClassificationJobsAccuracyTestService() {
        super();
    }

    // API

    public final double calculateJobsClassifierAccuracyWithFullTrainingDataDefault(final int runs) throws IOException {
        return calculateJobsClassifierAccuracyWithFullTrainingData(runs, PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
    }

    public final double calculateJobsClassifierAccuracyWithCoreTrainingDataDefault(final int runs) throws IOException {
        return calculateJobsClassifierAccuracyWithCoreTrainingData(runs, PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
    }

    final double calculateJobsClassifierAccuracyWithCoreTrainingData(final int runs, final int probes, final int features) throws IOException {
        final List<NamedVector> trainingData = JobsDataApi.jobsVsNonJobsCoreTrainingDataShuffled(probes, features);
        final List<ImmutablePair<String, String>> testData = ClassificationTestData.jobsAndNonJobsTestData();

        return calculateJobsClassifierAccuracy(trainingData, testData, runs, probes, features);
    }

    final double calculateJobsClassifierAccuracyWithFullTrainingData(final int runs, final int probes, final int features) throws IOException {
        final List<NamedVector> trainingData = JobsDataApi.jobsVsNonJobsFullTrainingDataShuffled(probes, features);
        final List<ImmutablePair<String, String>> testData = ClassificationTestData.jobsAndNonJobsTestData();

        return calculateJobsClassifierAccuracy(trainingData, testData, runs, probes, features);
    }

    final double calculateJobsClassifierAccuracy(final List<NamedVector> trainingData, final List<ImmutablePair<String, String>> testData, final int runs, final int probes, final int features) throws IOException {
        System.out.println("Current tokenizer: " + ClassificationSettings.TWEET_TOKENIZER);
        final long start = System.nanoTime() / (1000 * 1000 * 1000);
        final List<Double> results = Lists.newArrayList();
        for (int i = 0; i < runs; i++) {
            Collections.shuffle(testData);
            Collections.shuffle(trainingData);
            final CrossFoldLearner bestLearnerWithCoreTraining = Classifiers.Jobs.trainNewLearnerJobs(trainingData, probes, features);
            final double percentageCorrect = analyzeJobsData(bestLearnerWithCoreTraining, testData, probes, features);
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

    private final double analyzeJobsData(final CrossFoldLearner bestLearner, final List<ImmutablePair<String, String>> testData, final int probes, final int features) throws IOException {
        classificationService.setJobsVsNonJobsLerner(bestLearner);

        int correct = 0;
        int total = 0;
        for (final Pair<String, String> tweetData : testData) {
            total++;
            final boolean expected = JOB.equals(tweetData.getLeft());
            final boolean isTweetMatch = classificationService.isJob(tweetData.getRight(), probes, features);
            if (isTweetMatch == expected) {
                correct++;
            }
        }

        final double cd = correct;
        final double td = total;
        final double percentageCorrect = cd / td;
        return percentageCorrect;
    }

}
