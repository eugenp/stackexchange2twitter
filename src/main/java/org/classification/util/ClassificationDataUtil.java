package org.classification.util;

import static org.classification.util.ClassificationSettings.FEATURES;
import static org.classification.util.ClassificationSettings.PROBES_FOR_CONTENT_ENCODER_VECTOR;
import static org.classification.util.ClassificationSettings.TWEET_TOKENIZER;
import static org.classification.util.ClassificationUtil.COMMERCIAL;
import static org.classification.util.ClassificationUtil.NONCOMMERCIAL;
import static org.classification.util.ClassificationUtil.encodeWithTypeInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.mahout.math.NamedVector;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public final class ClassificationDataUtil {

    private ClassificationDataUtil() {
        throw new AssertionError();
    }

    // util

    // training

    public static final List<NamedVector> commercialVsNonCommercialLearningDataDefault() throws IOException {
        return commercialVsNonCommercialLearningData(PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
    }

    public static final List<NamedVector> commercialVsNonCommercialLearningData(final int probes, final int features) throws IOException {
        final List<NamedVector> nonCommercialvectors = nonCommercialTrainingData(probes, features);
        final List<NamedVector> commercialNamedVectors = commercialTrainingData(probes, features);

        final List<NamedVector> allNamedVectors = Lists.<NamedVector> newArrayList();
        allNamedVectors.addAll(commercialNamedVectors);
        allNamedVectors.addAll(nonCommercialvectors);
        Collections.shuffle(allNamedVectors);
        return allNamedVectors;
    }

    static final List<NamedVector> commercialTrainingData(final int probes, final int features) throws IOException {
        return trainingData("/classification/commercial.classif", COMMERCIAL, probes, features);
    }

    static final List<NamedVector> nonCommercialTrainingData(final int probes, final int features) throws IOException {
        return trainingData("/classification/noncommercial.classif", NONCOMMERCIAL, probes, features);
    }

    static final List<NamedVector> trainingData(final String path, final String type, final int probes, final int features) throws IOException {
        final String root = "src/main/resources";
        final List<String> tweets = IOUtils.readLines(new BufferedReader(new FileReader(root + path)));

        final List<NamedVector> vectors = Lists.<NamedVector> newArrayList();
        for (final String tweet : tweets) {
            vectors.add(encodeWithTypeInfo(type, Splitter.on(CharMatcher.anyOf(TWEET_TOKENIZER)).split(tweet), probes, features));
        }

        return vectors;
    }

    // test data

    public static final List<ImmutablePair<String, String>> commercialTestData() throws IOException {
        return testData("/classification/test/commercial.classif", COMMERCIAL);
    }

    public static final List<ImmutablePair<String, String>> noncommercialTestData() throws IOException {
        return testData("/classification/test/noncommercial.classif", NONCOMMERCIAL);
    }

    static final List<ImmutablePair<String, String>> testData(final String path, final String type) throws IOException {
        final String root = "src/main/resources";
        final List<String> tweets = IOUtils.readLines(new BufferedReader(new FileReader(root + path)));

        final List<ImmutablePair<String, String>> data = Lists.newArrayList();
        for (final String tweet : tweets) {
            data.add(new ImmutablePair<String, String>(type, tweet));
        }

        return data;
    }

}
