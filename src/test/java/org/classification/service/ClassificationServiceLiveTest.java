package org.classification.service;

import java.io.IOException;

import org.classification.service.accuracy.ClassificationAccuracyTestService;
import org.classification.spring.ClassificationConfig;
import org.common.spring.CommonContextConfig;
import org.gplus.spring.GplusContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.util.SpringProfileUtil;

/**
 * - note: this test is not actually live, just long running
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class, ClassificationConfig.class, GplusContextConfig.class })
@ActiveProfiles(SpringProfileUtil.LIVE)
public class ClassificationServiceLiveTest {

    @Autowired
    private ClassificationAccuracyTestService classificationAccuracyService;

    // tests

    // 5000 features:
    // 10000 features:
    /**
     * - note: the data to be classified has EMPTY type information included in the encoded vector <br/>
     * - so the results are production-like, but not excellent
     */
    @Test
    // @Ignore("long running - ignored by default")
    public final void givenClassifierWasTrained_whenClassifyingTestDataWithoutTypeInfo_thenResultsAreGood() throws IOException {
        final int runs = 300;
        final double mean = classificationAccuracyService.calculateCommercialClassifierAccuracyDefault(runs);
        System.out.println("Average Success Rate: " + mean);
    }

}
