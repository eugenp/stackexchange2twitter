package org.classification.util;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.google.common.collect.Lists;

public final class ClassificationTestData {

    private ClassificationTestData() {
        throw new AssertionError();
    }

    // API

    public static List<ImmutablePair<String, String>> commercialAndNonCommercialTestDataShuffled() throws IOException {
        final List<ImmutablePair<String, String>> allTestData = commercialAndNonCommercialTestData();
        Collections.shuffle(allTestData);
        return allTestData;
    }

    public static List<ImmutablePair<String, String>> commercialAndNonCommercialTestData() throws IOException {
        final List<ImmutablePair<String, String>> noncommercialTweets = SpecificClassificationDataUtil.noncommercialTestData();
        final List<ImmutablePair<String, String>> commercialTweets = SpecificClassificationDataUtil.commercialTestData();
        final List<ImmutablePair<String, String>> allTestData = Lists.newArrayList();
        allTestData.addAll(noncommercialTweets);
        allTestData.addAll(commercialTweets);
        return allTestData;
    }

    public static List<ImmutablePair<String, String>> programmingAndNonProgrammingTestDataShuffled() throws IOException {
        final List<ImmutablePair<String, String>> allTestData = programmingAndNonProgrammingTestData();
        Collections.shuffle(allTestData);
        return allTestData;
    }

    public static List<ImmutablePair<String, String>> programmingAndNonProgrammingTestData() throws IOException {
        final List<ImmutablePair<String, String>> nonprogrammingTweets = SpecificClassificationDataUtil.nonprogrammingTestData();
        final List<ImmutablePair<String, String>> programmingTweets = SpecificClassificationDataUtil.programmingTestData();
        final List<ImmutablePair<String, String>> allTestData = Lists.newArrayList();
        allTestData.addAll(nonprogrammingTweets);
        allTestData.addAll(programmingTweets);
        return allTestData;
    }

    // util

}
