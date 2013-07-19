package org.classification.service;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.classification.spring.ClassificationConfig;
import org.common.spring.CommonContextConfig;
import org.gplus.spring.GplusContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class, ClassificationConfig.class, GplusContextConfig.class })
public class ClassificationServiceLiveTest {

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private ClassificationAccuracyTestService classificationAccuracyService;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoException() {
        //
    }

    @Test
    public final void whenReadingClassificationTrainingFile_thenNoException() throws IOException {
        final List<String> lines = IOUtils.readLines(new BufferedReader(new FileReader("src/main/resources/classification/commercial.classif")));
        System.out.println(lines);
    }

    @Test
    public final void whenTweetIsClassified_thenNoException() {
        final boolean isCommercial = classificationService.isCommercial("URGENT: Scala Developer | 3 Month Contract | Westminster | Immediate Requirement! #Scala #Freelance #Jobs #IT");
        assertTrue(isCommercial);
    }

    // 5000 features: 0.923
    // 10000 features: 0.9xx
    /**
     * - note: the data to be classified has EMPTY type information included in the encoded vector <br/>
     * - so the results are production-like, but not excellent
     */
    @Test
    // @Ignore("long running - ignored by default")
    public final void givenClassifierWasTrained_whenClassifyingTestDataWithoutTypeInfo_thenResultsAreGood() throws IOException {
        final int runs = 1000;
        final double mean = classificationAccuracyService.calculateClassifierAccuracy(runs);
        System.out.println("Average Success Rate: " + mean);
    }

}
