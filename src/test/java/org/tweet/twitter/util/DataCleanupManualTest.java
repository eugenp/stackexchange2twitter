package org.tweet.twitter.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.classification.data.GenericClassificationDataUtil;
import org.junit.Test;

import com.google.common.collect.Sets;

public class DataCleanupManualTest {

    @Test
    public final void whenCleaningUpDuplicatesInFile_thenFileIsCleaned() throws IOException {
        final InputStream is = GenericClassificationDataUtil.class.getResourceAsStream("/notes/test/deal-toreject.txt");
        final List<String> lines = IOUtils.readLines(new BufferedReader(new InputStreamReader(is)));

        final Set<String> uniqueLines = Sets.newHashSet(lines);
        final File file = new File("/opt/sandbox/deal-toreject_2.txt");
        file.createNewFile();
        final FileWriter fileWriter = new FileWriter(file);
        IOUtils.writeLines(uniqueLines, "\n", fileWriter);
        fileWriter.close();
    }
}
