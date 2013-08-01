package org.common.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.common.spring.CommonServiceConfig;
import org.gplus.spring.GplusContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonServiceConfig.class, GplusContextConfig.class })
public class LinkServiceIntegrationTest {

    @Autowired
    private LinkService httpService;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoException() {
        //
    }

    // is homepage url

    @Test
    public final void givenUrlUnshortened_whenVerifyingIfUrlIsHomepageScenario1_thenResultIsCorrect() throws ClientProtocolException, IOException {
        assertTrue(httpService.isHomepageUrl("http://www.google.com"));
    }

    @Test
    public final void givenUrlUnshortened_whenVerifyingIfUrlIsHomepageScenario2_thenResultIsCorrect() throws ClientProtocolException, IOException {
        assertTrue(httpService.isHomepageUrl("http://www.blog.something.org"));
    }

    @Test
    public final void givenUrlUnshortened_whenVerifyingIfUrlIsHomepageScenario3_thenResultIsCorrect() throws ClientProtocolException, IOException {
        assertTrue(httpService.isHomepageUrl("http://www.yahoo.com/"));
    }

    // remove parameters

    @Test
    public final void givenUrlHasParameters_whenRemovingUrlParametersScenario1_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String urlWithNoParameters = httpService.removeUrlParameters("https://www.yahoo.com?utc=abc");
        final String expectedUrl = "https://www.yahoo.com";
        assertThat(urlWithNoParameters, equalTo(expectedUrl));
    }

    @Test
    public final void givenUrlHasParameters_whenRemovingUrlParametersScenario2_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String urlWithNoParameters = httpService.removeUrlParameters("http://www.baeldung.com/something?utc=abc");
        final String expectedUrl = "http://www.baeldung.com/something";
        assertThat(urlWithNoParameters, equalTo(expectedUrl));
    }

    @Test
    public final void givenUrlHasParameters_whenRemovingUrlParametersScenario3_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String urlWithNoParameters = httpService.removeUrlParameters("http://www.baeldung.com/spring-nosuchbeandefinitionexception?utm_source=feedburner&utm_medium=feed&utm_campaign=Feed%3A+Baeldung+%28baeldung%29");
        final String expectedUrl = "http://www.baeldung.com/spring-nosuchbeandefinitionexception";
        assertThat(urlWithNoParameters, equalTo(expectedUrl));
    }

    // util

}
