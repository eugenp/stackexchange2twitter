package org.common.http;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

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

    // util

    private final boolean isKnownShortenedUrl(final String url) {
        final boolean twitter = url.startsWith("http://t.co/");
        final boolean bitly = url.startsWith("http://bit.ly/");
        final boolean google = url.startsWith("http://goo.gl/");

        return twitter || bitly || google;
    }

}
