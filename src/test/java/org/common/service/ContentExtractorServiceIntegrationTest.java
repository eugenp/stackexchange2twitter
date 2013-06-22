package org.common.service;

import org.common.spring.CommonContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class })
public class ContentExtractorServiceIntegrationTest {

    // tests

    @Test
    public final void whenContextIsInitialized_thenNoExceptions() {
        //
    }

}
