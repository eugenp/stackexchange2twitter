package org.common.service.external.bitly;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.common.spring.CommonServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {// @formatter:off
    CommonServiceConfig.class 
}) // @formatter:on
public class BitlyServiceLiveTest {

    @Autowired
    private BitlyService instance;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoException() {
        assertNotNull(instance);
    }

    @Test
    public final void whenUrlIsShortened_thenCorrect() {
        final String shortenedUrl = instance.shortenUrl("http://www.baeldung.com");
        assertNotNull(shortenedUrl);
        assertThat(shortenedUrl, startsWith("http://bit.ly/"));
    }

}
