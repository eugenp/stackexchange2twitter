package org.stackexchange.gather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.classification.util.ClassificationDataUtil;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ClassificationDataCleanupManualTest {

    // tests

    @Test
    public final void whenCleaningClassificationData_thenNoExceptions() throws IOException {
        final String path = "/classification/noncommercial.classif";
        final InputStream is = ClassificationDataUtil.class.getResourceAsStream(path);
        final List<String> tweetsForClassification = IOUtils.readLines(new BufferedReader(new InputStreamReader(is)));

        final List<String> tweetsClean = Lists.transform(tweetsForClassification, new CleanupStringFunction());
        final File file = new File("/opt/sandbox/classification_clean.classif");
        final FileWriter fw = new FileWriter(file); // it creates the file writer and the actual file
        IOUtils.writeLines(tweetsClean, "\n", fw);
    }

}
