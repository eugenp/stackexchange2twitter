package org.classification.data;

import static org.classification.data.GenericClassificationDataUtil.trainingData;

import java.io.IOException;
import java.util.List;

import org.apache.mahout.math.NamedVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class SimpleDataLoadingStrategy {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String singlePath;
    private String type;

    public SimpleDataLoadingStrategy(final String singlePath, final String type) {
        super();

        this.singlePath = singlePath;
        this.type = type;
    }

    // API

    public final List<NamedVector> loadData(final int probes, final int features) {
        try {
            return trainingData(singlePath, type, probes, features);
        } catch (final IOException ioEx) {
            logger.error("Data could not be loaded", ioEx);
            return Lists.newArrayList();
        }
    }

}
