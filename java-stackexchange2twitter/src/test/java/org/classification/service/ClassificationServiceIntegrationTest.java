package org.classification.service;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.classification.data.ClassificationData.Jobs;
import org.classification.spring.ClassificationConfig;
import org.common.spring.CommonServiceConfig;
import org.gplus.spring.GplusContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonServiceConfig.class, ClassificationConfig.class, GplusContextConfig.class })
public class ClassificationServiceIntegrationTest {

    @Autowired
    private ClassificationService classificationService;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoException() {
        //
    }

    @Test
    public final void whenReadingClassificationTrainingFile_thenNoException() throws IOException {
        final List<String> lines = IOUtils.readLines(new BufferedReader(new FileReader("src/main/resources" + Jobs.Training.JOBS)));
        System.out.println(lines);
    }

    @Test
    public final void whenTweetIsClassified_thenNoException() {
        final boolean isMatch = classificationService.isJobDefault("URGENT: Scala Developer | 3 Month Contract | Westminster | Immediate Requirement! #Scala #Freelance #Jobs #IT");
        assertTrue(isMatch);
    }

}
