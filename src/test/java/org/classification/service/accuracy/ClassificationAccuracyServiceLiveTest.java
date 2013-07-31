package org.classification.service.accuracy;

import java.io.IOException;
import java.util.List;

import org.classification.service.ClassificationService;
import org.classification.spring.ClassificationConfig;
import org.common.spring.CommonContextConfig;
import org.gplus.spring.GplusContextConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.util.SpringProfileUtil;

import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class, ClassificationConfig.class, GplusContextConfig.class })
@ActiveProfiles(SpringProfileUtil.LIVE)
public class ClassificationAccuracyServiceLiveTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private ClassificationAccuracyTestService classificationAccuracyService;

    // tests

    /**
     * - note: the data to be classified has EMPTY type information included in the encoded vector <br/>
     * - so the results are production-like, but not excellent
     */
    @Test
    // @Ignore("long running - ignored by default")
    public final void givenCommercialClassifierWasTrained_whenClassifyingTestDataWithoutTypeInfo_thenResultsAreGood() throws IOException {
        // final List<Integer> probeCounts = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        // final List<Integer> featuresCount = Lists.newArrayList(1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 15000);
        final List<Integer> probeCounts = Lists.newArrayList(2, 3, 4, 5);
        final List<Integer> featuresCount = Lists.newArrayList(5000, 7000);

        final int runs = 1000;
        for (final Integer features : featuresCount) {
            for (final Integer probes : probeCounts) {
                final double mean = classificationAccuracyService.calculateCommercialClassifierAccuracy(runs, probes, features);
                logger.warn("For features= " + features + " and probes= " + probes + " result is= " + mean);
                System.out.println("For features= " + features + " and probes= " + probes + " result is= " + mean);
            }
        }
    }

    /**
     * - note: the data to be classified has EMPTY type information included in the encoded vector <br/>
     * - so the results are production-like, but not excellent
     */
    @Test
    @Ignore("long running - ignored by default")
    public final void givenProgrammingClassifierWasTrained_whenClassifyingTestDataWithoutTypeInfo_thenResultsAreGood() throws IOException {
        // final List<Integer> probeCounts = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        // final List<Integer> featuresCount = Lists.newArrayList(1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 15000);
        final List<Integer> probeCounts = Lists.newArrayList(3, 4, 5, 6);
        final List<Integer> featuresCount = Lists.newArrayList(5000, 7000);

        final int runs = 1000;
        for (final Integer features : featuresCount) {
            for (final Integer probes : probeCounts) {
                final double mean = classificationAccuracyService.calculateProgrammingClassifierAccuracy(runs, probes, features);
                logger.warn("For features= " + features + " and probes= " + probes + " result is= " + mean);
                System.out.println("For features= " + features + " and probes= " + probes + " result is= " + mean);
            }
        }
    }

}
