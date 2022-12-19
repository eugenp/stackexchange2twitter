package org.classification.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.classification.data.ClassificationData.CommercialDataApi;
import org.classification.util.ClassificationSettings;
import org.junit.Test;
import org.stackexchange.gather.CleanupStringFunction;

import com.google.common.collect.Lists;

public class ClassificationDataCleanupManualTest {

    // tests

    @Test
    public final void whenLoadingTrainClassificationData_thenNoExceptions() throws IOException {
        CommercialDataApi.nonCommercialTrainingData(ClassificationSettings.PROBES_FOR_CONTENT_ENCODER_VECTOR, ClassificationSettings.FEATURES);
    }

    @Test
    public final void whenLoadingTestClassificationData_thenNoExceptions() throws IOException {
        CommercialDataApi.nonCommercialTestData();
    }

    @Test
    public final void whenCleaningClassificationData_thenNoExceptions() throws IOException {
        final String path = ClassificationData.Other.SAMPLE;
        final InputStream is = ClassificationData.class.getResourceAsStream(path);
        final List<String> tweetsForClassification = IOUtils.readLines(new BufferedReader(new InputStreamReader(is)));

        final List<String> tweetsClean = Lists.transform(tweetsForClassification, new CleanupStringFunction());
        final File file = new File("/opt/sandbox/classification_clean.classif");
        final FileWriter fw = new FileWriter(file); // it creates the file writer and the actual file
        IOUtils.writeLines(tweetsClean, "\n", fw);
    }

}
