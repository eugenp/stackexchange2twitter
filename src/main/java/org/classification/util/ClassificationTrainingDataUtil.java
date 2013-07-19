package org.classification.util;

import static org.classification.util.ClassificationUtil.COMMERCIAL;
import static org.classification.util.ClassificationUtil.NONCOMMERCIAL;
import static org.classification.util.ClassificationUtil.encodeWithTypeInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.mahout.math.NamedVector;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public final class ClassificationTrainingDataUtil {

    private ClassificationTrainingDataUtil() {
        throw new AssertionError();
    }

    // util

    public static final List<NamedVector> commercialVsNonCommercialLearningData() throws IOException {
        return commercialVsNonCommercialLearningData(ClassificationSettings.PROBES_FOR_CONTENT_ENCODER_VECTOR);
    }

    public static final List<NamedVector> commercialVsNonCommercialLearningData(final int probes) throws IOException {
        final List<String> noncommercialTweets = IOUtils.readLines(new BufferedReader(new FileReader("src/main/resources/classification/noncommercial.classif")));
        final List<String> commercialTweets = IOUtils.readLines(new BufferedReader(new FileReader("src/main/resources/classification/commercial.classif")));

        final List<NamedVector> noncommercialNamedVectors = Lists.<NamedVector> newArrayList();
        final List<NamedVector> commercialNamedVectors = Lists.<NamedVector> newArrayList();
        for (final String noncommercialTweet : noncommercialTweets) {
            noncommercialNamedVectors.add(encodeWithTypeInfo(NONCOMMERCIAL, Splitter.on(CharMatcher.anyOf(ClassificationSettings.TWEET_TOKENIZER)).split(noncommercialTweet), probes));
        }
        for (final String commercialTweet : commercialTweets) {
            noncommercialNamedVectors.add(encodeWithTypeInfo(COMMERCIAL, Splitter.on(CharMatcher.anyOf(ClassificationSettings.TWEET_TOKENIZER)).split(commercialTweet), probes));
        }

        final List<NamedVector> allNamedVectors = Lists.<NamedVector> newArrayList();
        allNamedVectors.addAll(commercialNamedVectors);
        allNamedVectors.addAll(noncommercialNamedVectors);
        Collections.shuffle(allNamedVectors);
        return allNamedVectors;
    }

}
