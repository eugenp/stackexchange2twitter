package org.common.http;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

public class HttpUtilUnitTest {

    // tests

    @Test
    public final void givenUrlUnshortened_whenVerifyingIfUrlIsHomepageScenario1_thenResultIsCorrect() throws ClientProtocolException, IOException {
        assertTrue(HttpUtil.isHomepageUrl("http://www.google.com"));
    }

    @Test
    public final void givenUrlUnshortened_whenVerifyingIfUrlIsHomepageScenario2_thenResultIsCorrect() throws ClientProtocolException, IOException {
        assertTrue(HttpUtil.isHomepageUrl("http://www.blog.something.org"));
    }

    @Test
    public final void givenUrlUnshortened_whenVerifyingIfUrlIsHomepageScenario3_thenResultIsCorrect() throws ClientProtocolException, IOException {
        assertTrue(HttpUtil.isHomepageUrl("http://www.yahoo.com/"));
    }

    // util

}
