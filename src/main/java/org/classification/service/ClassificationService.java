package org.classification.service;

import static org.classification.util.ClassificationSettings.FEATURES;
import static org.classification.util.ClassificationSettings.PROBES_FOR_CONTENT_ENCODER_VECTOR;
import static org.classification.util.GenericClassificationUtil.encode;

import java.io.IOException;

import org.apache.mahout.classifier.sgd.CrossFoldLearner;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.classification.util.GenericClassificationDataUtil;
import org.classification.util.SpecificClassificationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class ClassificationService implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private CrossFoldLearner jobVsNonJobLerner;

    private CrossFoldLearner programmingVsNonProgrammingLerner;

    public ClassificationService() {
        super();
    }

    // API

    public boolean isJobDefault(final String text) {
        try {
            return isJobInternalDefault(text);
        } catch (final Exception ex) {
            logger.error("", ex);
            return false;
        }
    }

    public boolean isJob(final String text, final int probes, final int features) {
        try {
            return isJobInternal(text, probes, features);
        } catch (final Exception ex) {
            logger.error("", ex);
            return false;
        }
    }

    public boolean isProgramming(final String text, final int probes, final int features) {
        try {
            return isProgrammingInternal(text, probes, features);
        } catch (final Exception ex) {
            logger.error("", ex);
            return false;
        }
    }

    boolean isProgrammingInternal(final String text, final int probes, final int features) {
        final Iterable<String> textWords = GenericClassificationDataUtil.tokenizeTweet(text);
        final Vector encodedAsVector = encode(textWords, probes, features);

        final Vector collector = new DenseVector(2);
        programmingVsNonProgrammingLerner.classifyFull(collector, encodedAsVector);
        final int cat = collector.maxValueIndex();

        return cat == 1;
    }

    boolean isJobInternal(final String text, final int probes, final int features) {
        final Iterable<String> textWords = GenericClassificationDataUtil.tokenizeTweet(text);
        final Vector encodedAsVector = encode(textWords, probes, features);

        final Vector collector = new DenseVector(2);
        jobVsNonJobLerner.classifyFull(collector, encodedAsVector);
        final int cat = collector.maxValueIndex();

        return cat == 1;
    }

    boolean isJobInternalDefault(final String text) {
        return isJobInternal(text, PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
    }

    // Spring

    @Override
    public final void afterPropertiesSet() throws IOException {
        jobVsNonJobLerner = SpecificClassificationUtil.trainNewLearnerJobWithCoreTrainingData(PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
        programmingVsNonProgrammingLerner = SpecificClassificationUtil.trainNewLearnerProgramming(PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
    }

    public final void setCommercialVsNonCommercialLerner(final CrossFoldLearner commercialVsNonCommercialLerner) {
        this.jobVsNonJobLerner = commercialVsNonCommercialLerner;
    }

    public final void setProgrammingVsNonProgrammingLerner(final CrossFoldLearner programmingVsNonProgrammingLerner) {
        this.programmingVsNonProgrammingLerner = programmingVsNonProgrammingLerner;
    }

}
