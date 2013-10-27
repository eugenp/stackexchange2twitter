package org.classification.util;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.classification.util.SpecificClassificationDataUtil.JobsDataApi;
import org.classification.util.SpecificClassificationDataUtil.ProgrammingDataApi;

import com.google.common.collect.Lists;

public final class ClassificationTestData {

    private ClassificationTestData() {
        throw new AssertionError();
    }

    // API

    public static List<ImmutablePair<String, String>> jobsAndNonJobsTestDataShuffled() throws IOException {
        final List<ImmutablePair<String, String>> allTestData = jobsAndNonJobsTestData();
        Collections.shuffle(allTestData);
        return allTestData;
    }

    public static List<ImmutablePair<String, String>> jobsAndNonJobsTestData() throws IOException {
        final List<ImmutablePair<String, String>> nonJobsTweets = JobsDataApi.nonJobsTestData();
        final List<ImmutablePair<String, String>> jobsTweets = JobsDataApi.jobsTestData();
        final List<ImmutablePair<String, String>> allTestData = Lists.newArrayList();
        allTestData.addAll(nonJobsTweets);
        allTestData.addAll(jobsTweets);
        return allTestData;
    }

    public static List<ImmutablePair<String, String>> programmingAndNonProgrammingTestDataShuffled() throws IOException {
        final List<ImmutablePair<String, String>> allTestData = programmingAndNonProgrammingTestData();
        Collections.shuffle(allTestData);
        return allTestData;
    }

    public static List<ImmutablePair<String, String>> programmingAndNonProgrammingTestData() throws IOException {
        final List<ImmutablePair<String, String>> nonprogrammingTweets = ProgrammingDataApi.nonProgrammingTestData();
        final List<ImmutablePair<String, String>> programmingTweets = ProgrammingDataApi.programmingTestData();
        final List<ImmutablePair<String, String>> allTestData = Lists.newArrayList();
        allTestData.addAll(nonprogrammingTweets);
        allTestData.addAll(programmingTweets);
        return allTestData;
    }

    // util

}
