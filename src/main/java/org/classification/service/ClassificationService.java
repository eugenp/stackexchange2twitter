package org.classification.service;

import static org.classification.util.ClassificationUtil.encode;

import java.io.IOException;
import java.util.List;

import org.apache.mahout.classifier.sgd.AdaptiveLogisticRegression;
import org.apache.mahout.classifier.sgd.CrossFoldLearner;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.Vector;
import org.classification.util.ClassificationSettings;
import org.classification.util.ClassificationTrainingDataUtil;
import org.classification.util.ClassificationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

@Service
public class ClassificationService implements InitializingBean {
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
        final Vector encodedAsVector = encode(Splitter.on(CharMatcher.anyOf(ClassificationSettings.TWEET_TOKENIZER)).split(text));

        final Vector collector = new DenseVector(2);
        commercialVsNonCommercialLerner.classifyFull(collector, encodedAsVector);
        final int cat = collector.maxValueIndex();

        return cat == 1;
    }

    // util

    final CrossFoldLearner commercialVsNonCommercialBestLearner() throws IOException {
        final List<NamedVector> learningData = ClassificationTrainingDataUtil.commercialVsNonCommercialLearningData();
        final AdaptiveLogisticRegression classifier = ClassificationUtil.trainClassifier(learningData);
        final CrossFoldLearner bestLearner = classifier.getBest().getPayload().getLearner();

        return bestLearner;

    }

    // Spring

    @Override
    public final void afterPropertiesSet() throws IOException {
        commercialVsNonCommercialLerner = commercialVsNonCommercialBestLearner();
    }

    public void setCommercialVsNonCommercialLerner(final CrossFoldLearner commercialVsNonCommercialLerner) {
        this.commercialVsNonCommercialLerner = commercialVsNonCommercialLerner;
    }

}
