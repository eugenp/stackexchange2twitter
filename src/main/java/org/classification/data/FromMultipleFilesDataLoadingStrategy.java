package org.classification.data;

import static org.classification.util.GenericClassificationUtil.encodeWithTypeInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.mahout.math.NamedVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class FromMultipleFilesDataLoadingStrategy {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, String> paths;
    private final String type;
    private final boolean loadAccept;

    public FromMultipleFilesDataLoadingStrategy(final Map<String, String> paths, final String type, final boolean loadAccept) {
        super();

        this.paths = paths;
        this.type = type;
        this.loadAccept = loadAccept;
    }

    // API

    public final List<NamedVector> loadTrainData(final int probes, final int features) {
        try {
            return loadTrainDataInternal(probes, features);
        } catch (final IOException ioEx) {
            logger.error("Data could not be loaded", ioEx);
            return Lists.newArrayList();
        }
    }

    public final List<ImmutablePair<String, String>> loadTestData() {
        try {
            return loadTestDataInternal();
        } catch (final IOException ioEx) {
            logger.error("Data could not be loaded", ioEx);
            return Lists.newArrayList();
        }
    }

    // util

    final List<NamedVector> loadTrainDataInternal(final int probes, final int features) throws IOException {
        final List<NamedVector> allTestData = Lists.newArrayList();

        for (final Map.Entry<String, String> acceptAndRejectPaths : this.paths.entrySet()) {
            final List<String> acceptTweets = loadAcceptTweets(acceptAndRejectPaths);
            final List<String> rejectTweets = loadRejectTweets(acceptAndRejectPaths);

            final int minSize = Math.min(acceptTweets.size(), rejectTweets.size());
            final List<NamedVector> trainData = loadTrainData(acceptTweets, minSize, probes, features);
            allTestData.addAll(trainData);
        }

        return allTestData;
    }

    final List<ImmutablePair<String, String>> loadTestDataInternal() throws IOException {
        final List<ImmutablePair<String, String>> allTestData = Lists.newArrayList();

        for (final Map.Entry<String, String> acceptAndRejectPaths : this.paths.entrySet()) {
            final List<String> acceptTweets = loadAcceptTweets(acceptAndRejectPaths);
            final List<String> rejectTweets = loadRejectTweets(acceptAndRejectPaths);

            final int minSize = Math.min(acceptTweets.size(), rejectTweets.size());
            final List<ImmutablePair<String, String>> testData = convertTestData((loadAccept ? acceptTweets : rejectTweets), minSize);
            allTestData.addAll(testData);
        }

        return allTestData;
    }

    private final List<String> loadAcceptTweets(final Map.Entry<String, String> acceptAndRejectPaths) throws IOException {
        final String acceptPath = acceptAndRejectPaths.getKey();
        final InputStream acceptIs = GenericClassificationDataUtil.class.getResourceAsStream(acceptPath);
        final List<String> acceptTweets = IOUtils.readLines(new BufferedReader(new InputStreamReader(acceptIs)));
        return acceptTweets;
    }

    private final List<String> loadRejectTweets(final Map.Entry<String, String> acceptAndRejectPaths) throws IOException {
        final String rejectPath = acceptAndRejectPaths.getValue();
        final InputStream rejectIs = GenericClassificationDataUtil.class.getResourceAsStream(rejectPath);
        final List<String> rejectTweets = IOUtils.readLines(new BufferedReader(new InputStreamReader(rejectIs)));
        return rejectTweets;
    }

    private final List<NamedVector> loadTrainData(final List<String> tweets, final int minSize, final int probes, final int features) {
        final List<String> tweetsToLoad = Lists.newArrayList();
        tweetsToLoad.addAll(tweets.subList(0, minSize));

        final List<NamedVector> vectors = Lists.<NamedVector> newArrayList();
        for (final String tweet : tweetsToLoad) {
            final Iterable<String> wordsOfTweet = GenericClassificationDataUtil.tokenizeTweet(tweet);
            vectors.add(encodeWithTypeInfo(type, wordsOfTweet, probes, features));
        }

        return vectors;
    }

    private final List<ImmutablePair<String, String>> convertTestData(final List<String> tweets, final int minSize) {
        final List<String> tweetsToLoad = Lists.newArrayList();
        tweetsToLoad.addAll(tweets.subList(minSize, tweets.size()));

        final List<ImmutablePair<String, String>> testData = Lists.newArrayList();
        for (final String tweet : tweetsToLoad) {
            testData.add(new ImmutablePair<String, String>(type, tweet));
        }

        return testData;
    }

}
