package org.classification.util;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.google.common.collect.Lists;

public final class ClassificationData {

    private ClassificationData() {
        throw new AssertionError();
    }

    // API

    public static List<ImmutablePair<String, String>> commercialAndNonCommercialTweets() throws IOException {
        final List<ImmutablePair<String, String>> noncommercialTweets = ClassificationDataUtil.noncommercialTestData();
        final List<ImmutablePair<String, String>> commercialTweets = ClassificationDataUtil.commercialTestData();
        final List<ImmutablePair<String, String>> allTweets = Lists.newArrayList();
        allTweets.addAll(noncommercialTweets);
        allTweets.addAll(commercialTweets);
        Collections.shuffle(allTweets);
        return allTweets;
    }

    // util

}
