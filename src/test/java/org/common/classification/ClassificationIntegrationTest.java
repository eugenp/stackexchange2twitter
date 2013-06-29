package org.common.classification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class ClassificationIntegrationTest {

    // tests

    @Test
    public final void whenX_thenNoException() throws IOException {
        final List<String> lines = IOUtils.readLines(new BufferedReader(new FileReader("src/main/resources/commercial.classif")));
        System.out.println(lines);
    }

}
