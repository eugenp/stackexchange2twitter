package org.common.service.classification;

import static org.common.classification.ClassificationUtil.COMMERCIAL;
import static org.common.classification.ClassificationUtil.NONCOMMERCIAL;
import static org.common.classification.ClassificationUtil.encode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.mahout.classifier.sgd.AdaptiveLogisticRegression;
import org.apache.mahout.classifier.sgd.CrossFoldLearner;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.Vector;
import org.common.classification.ClassificationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

@Service
public class ClassificationService implements InitializingBean {
    public static final String TWEET_TOKENIZER = " ,.!?\":";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private CrossFoldLearner commercialVsNonCommercialLerner;

    public ClassificationService() {
        super();
    }

    // API

    public boolean isCommercial(final String text) {
        try {
            return isCommercialInternal(text);
        } catch (final Exception ex) {
            logger.error("", ex);
            return false;
        }
    }

    boolean isCommercialInternal(final String text) {
        final NamedVector encodedAsVector = encode("", Splitter.on(CharMatcher.anyOf(TWEET_TOKENIZER)).split(text));

        final Vector collector = new DenseVector(2);
        commercialVsNonCommercialLerner.classifyFull(collector, encodedAsVector);
        final int cat = collector.maxValueIndex();

        return cat == 1;
    }

    public boolean isCommercialNew(final String text) {
        try {
            return isCommercialInternalNew(text);
        } catch (final Exception ex) {
            logger.error("", ex);
            return false;
        }
    }

    boolean isCommercialInternalNew(final String text) {
        final Vector encodedAsVector = encode(Splitter.on(CharMatcher.anyOf(TWEET_TOKENIZER)).split(text));

        final Vector collector = new DenseVector(2);
        commercialVsNonCommercialLerner.classifyFull(collector, encodedAsVector);
        final int cat = collector.maxValueIndex();

        return cat == 1;
    }

    // util

    boolean isCommercial(final String type, final String text) {
        final NamedVector encodedAsVector = encode(type, Splitter.on(CharMatcher.anyOf(TWEET_TOKENIZER)).split(text));

        final Vector collector = new DenseVector(2);
        commercialVsNonCommercialLerner.classifyFull(collector, encodedAsVector);
        final int cat = collector.maxValueIndex();

        return cat == 1;
    }

    final CrossFoldLearner commercialVsNonCommercialBestLearner() throws IOException {
        final List<NamedVector> learningData = commercialVsNonCommercialLearningData();
        final AdaptiveLogisticRegression classifier = ClassificationUtil.trainClassifier(learningData);
        final CrossFoldLearner bestLearner = classifier.getBest().getPayload().getLearner();

        return bestLearner;

    }

    final List<NamedVector> commercialVsNonCommercialLearningData() throws IOException {
        final List<String> noncommercialTweets = IOUtils.readLines(new BufferedReader(new FileReader("src/main/resources/classification/noncommercial.classif")));
        final List<String> commercialTweets = IOUtils.readLines(new BufferedReader(new FileReader("src/main/resources/classification/commercial.classif")));

        final List<NamedVector> noncommercialNamedVectors = Lists.<NamedVector> newArrayList();
        final List<NamedVector> commercialNamedVectors = Lists.<NamedVector> newArrayList();
        for (final String noncommercialTweet : noncommercialTweets) {
            noncommercialNamedVectors.add(encode(NONCOMMERCIAL, Splitter.on(CharMatcher.anyOf(TWEET_TOKENIZER)).split(noncommercialTweet)));
        }
        for (final String commercialTweet : commercialTweets) {
            noncommercialNamedVectors.add(encode(COMMERCIAL, Splitter.on(CharMatcher.anyOf(TWEET_TOKENIZER)).split(commercialTweet)));
        }

        final List<NamedVector> allNamedVectors = Lists.<NamedVector> newArrayList();
        allNamedVectors.addAll(commercialNamedVectors);
        allNamedVectors.addAll(noncommercialNamedVectors);
        Collections.shuffle(allNamedVectors);
        return allNamedVectors;
    }

    // Spring

    @Override
    public final void afterPropertiesSet() throws IOException {
        commercialVsNonCommercialLerner = commercialVsNonCommercialBestLearner();
    }

}
