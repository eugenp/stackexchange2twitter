package org.classification.util;

import static org.classification.util.ClassificationSettings.TWEET_TOKENIZER;
import static org.classification.util.GenericClassificationUtil.encodeWithTypeInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.mahout.math.NamedVector;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public final class GenericClassificationDataUtil {

    private GenericClassificationDataUtil() {
        throw new AssertionError();
    }

    // API

    // util

    public static final List<NamedVector> oneVsAnotherLearningDataShuffled(final int probes, final int features, final List<NamedVector> vectorsFirstSet, final List<NamedVector> vectorsSecondSet) throws IOException {
        final List<NamedVector> allNamedVectors = oneVsAnotherLearningData(probes, features, vectorsFirstSet, vectorsSecondSet);
        Collections.shuffle(allNamedVectors);
        return allNamedVectors;
    }

    public static final List<NamedVector> oneVsAnotherLearningData(final int probes, final int features, final List<NamedVector> vectorsFirstSet, final List<NamedVector> vectorsSecondSet) throws IOException {
        final List<NamedVector> allNamedVectors = Lists.<NamedVector> newArrayList();
        allNamedVectors.addAll(vectorsFirstSet);
        allNamedVectors.addAll(vectorsSecondSet);
        return allNamedVectors;
    }

    public static final List<NamedVector> trainingData(final String path, final String type, final int probes, final int features) throws IOException {
        final InputStream is = GenericClassificationDataUtil.class.getResourceAsStream(path);
        final List<String> tweets = IOUtils.readLines(new BufferedReader(new InputStreamReader(is)));

        final List<NamedVector> vectors = Lists.<NamedVector> newArrayList();
        for (final String tweet : tweets) {
            vectors.add(encodeWithTypeInfo(type, Splitter.on(CharMatcher.anyOf(TWEET_TOKENIZER)).split(tweet), probes, features));
        }

        return vectors;
    }

    public static final List<ImmutablePair<String, String>> testData(final String path, final String type) throws IOException {
        final InputStream is = GenericClassificationDataUtil.class.getResourceAsStream(path);
        final List<String> tweets = IOUtils.readLines(new BufferedReader(new InputStreamReader(is)));

        final List<ImmutablePair<String, String>> data = Lists.newArrayList();
        for (final String tweet : tweets) {
            data.add(new ImmutablePair<String, String>(type, tweet));
        }

        return data;
    }

}
