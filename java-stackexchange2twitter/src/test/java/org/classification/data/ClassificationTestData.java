package org.classification.data;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.classification.data.ClassificationData.CommercialDataApi;
import org.classification.data.ClassificationData.JobsDataApi;
import org.classification.data.ClassificationData.ProgrammingDataApi;

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

    public static List<ImmutablePair<String, String>> commercialAndNonCommercialTestDataShuffled() throws IOException {
        final List<ImmutablePair<String, String>> allTestData = commercialAndNonCommercialTestData();
        Collections.shuffle(allTestData);
        return allTestData;
    }

    public static List<ImmutablePair<String, String>> commercialAndNonCommercialTestData() throws IOException {
        final List<ImmutablePair<String, String>> nonCommercialTweets = CommercialDataApi.nonCommercialTestData();
        final List<ImmutablePair<String, String>> commercialTweets = CommercialDataApi.commercialTestData();
        final List<ImmutablePair<String, String>> allTestData = Lists.newArrayList();
        allTestData.addAll(nonCommercialTweets);
        allTestData.addAll(commercialTweets);
        return allTestData;
    }

    // util

}
