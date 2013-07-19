package org.classification.service;

import static org.classification.util.ClassificationSettings.FEATURES;
import static org.classification.util.ClassificationSettings.PROBES_FOR_CONTENT_ENCODER_VECTOR;
import static org.classification.util.ClassificationSettings.TWEET_TOKENIZER;
import static org.classification.util.ClassificationUtil.encodeDefault;

import java.io.IOException;

import org.apache.mahout.classifier.sgd.CrossFoldLearner;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
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
        final Vector encodedAsVector = encodeDefault(Splitter.on(CharMatcher.anyOf(TWEET_TOKENIZER)).split(text));

        final Vector collector = new DenseVector(2);
        commercialVsNonCommercialLerner.classifyFull(collector, encodedAsVector);
        final int cat = collector.maxValueIndex();

        return cat == 1;
    }

    // Spring

    @Override
    public final void afterPropertiesSet() throws IOException {
        commercialVsNonCommercialLerner = ClassificationUtil.commercialVsNonCommercialBestLearner(PROBES_FOR_CONTENT_ENCODER_VECTOR, FEATURES);
    }

    public final void setCommercialVsNonCommercialLerner(final CrossFoldLearner commercialVsNonCommercialLerner) {
        this.commercialVsNonCommercialLerner = commercialVsNonCommercialLerner;
    }

}
