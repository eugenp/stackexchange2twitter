package org.common.classification;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.io.IOException;

import org.junit.Test;

import com.google.common.collect.Lists;

public class ClassificationUnitTest {

    // tests

    @Test
    public final void whenTextIsEncodedAsVector1_thenNoExceptions() {
        ClassificationUtil.encodeIncomplete(randomAlphabetic(4) + " " + randomAlphabetic(5));
    }

    @Test
    public final void whenTextIsEncodedAsVector2_thenNoExceptions() throws IOException {
        ClassificationUtil.encode(randomAlphabetic(4), Lists.newArrayList(randomAlphabetic(5), randomAlphabetic(4)));
    }

    @Test
    public final void whenLoadingClassificationData_thenNoExceptions() throws IOException {
        ClassificationUtil.loadData();
    }

}
