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

    public static List<ImmutablePair<String, String>> commercialAndNonCommercialTestData() throws IOException {
        final List<ImmutablePair<String, String>> noncommercialTweets = ClassificationDataUtil.noncommercialTestData();
        final List<ImmutablePair<String, String>> commercialTweets = ClassificationDataUtil.commercialTestData();
        final List<ImmutablePair<String, String>> allTestData = Lists.newArrayList();
        allTestData.addAll(noncommercialTweets);
        allTestData.addAll(commercialTweets);
        Collections.shuffle(allTestData);
        return allTestData;
    }

    public static List<ImmutablePair<String, String>> programmingAndNonProgrammingTestData() throws IOException {
        final List<ImmutablePair<String, String>> nonprogrammingTweets = ClassificationDataUtil.nonprogrammingTestData();
        final List<ImmutablePair<String, String>> programmingTweets = ClassificationDataUtil.programmingTestData();
        final List<ImmutablePair<String, String>> allTestData = Lists.newArrayList();
        allTestData.addAll(nonprogrammingTweets);
        allTestData.addAll(programmingTweets);
        Collections.shuffle(allTestData);
        return allTestData;
    }

    // util

}
