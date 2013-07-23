package org.common.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;

import org.common.spring.CommonContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.util.SpringProfileUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class })
@ActiveProfiles(SpringProfileUtil.LIVE)
public class ContentExtractorServiceLiveTest {

    @Autowired
    private ContentExtractorService contentExtractorService;

    // tests

    @Test
    public final void whenContextIsInitialized_thenNoExceptions() {
        //
    }

    @Test
    public final void whenContentIsExtractedFromUrl_thenNoExceptions() throws MalformedURLException, IOException {
        assertThat(contentExtractorService.extractTitle("http://www.google.com"), equalTo("Google"));
    }

    // util

}
