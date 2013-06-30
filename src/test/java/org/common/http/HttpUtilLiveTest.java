package org.common.http;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

public class HttpUtilLiveTest {

    // tests

    @Test
    public final void whenShortenedUriIsUnshortednedBySingleLevelScenario1_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String unshortenedUrl = HttpUtil.expandSingleLevel("http://t.co/wCD5WnAFGi");
        System.out.println(unshortenedUrl);
        assertNotNull(unshortenedUrl);
        assertThat(unshortenedUrl, not(containsString("t.co")));
    }

    @Test
    public final void whenStandardUriIsUnshortednedBySingleLevelScenario1_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String url = "http://www.yahoo.com";
        final String unshortenedUrl = HttpUtil.expandSingleLevel(url);
        System.out.println(unshortenedUrl);
        assertNotNull(unshortenedUrl);
        assertThat(unshortenedUrl, equalTo(url));
    }

    @Test
    public final void whenShortenedUriIsUnshortednedScenario1_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String unshortenedUrl = HttpUtil.expand("http://t.co/wCD5WnAFGi");
        System.out.println(unshortenedUrl);
        assertNotNull(unshortenedUrl);
        assertFalse(isKnownShortenedUrl(unshortenedUrl));
    }

    @Test
    public final void givenUrlUnshortened_whenVerifyingIfUrlIsHomepage_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String candidateUrl = HttpUtil.expand("http://bit.ly/N7vAX");
        System.out.println(candidateUrl);
        assertTrue(HttpUtil.isHomepageUrl(candidateUrl));
    }

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

    private final boolean isKnownShortenedUrl(final String url) {
        final boolean twitter = url.startsWith("http://t.co/");
        final boolean bitly = url.startsWith("http://bit.ly/");
        final boolean google = url.startsWith("http://goo.gl/");

        return twitter || bitly || google;
    }

}
