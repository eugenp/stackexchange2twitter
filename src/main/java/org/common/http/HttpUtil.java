package org.common.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stackexchange.api.client.HttpFactory;

import com.google.api.client.util.Preconditions;
import com.google.common.net.HttpHeaders;

public final class HttpUtil {
    static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private HttpUtil() {
        throw new AssertionError();
    }

    // API

    public static String expand(final String urlArg) throws ClientProtocolException, IOException {
        String originalUrl = urlArg;
        String newUrl = expandSingleLevel(originalUrl);
        while (!originalUrl.equals(newUrl)) {
            originalUrl = newUrl;
            newUrl = expandSingleLevel(originalUrl);
        }

        return newUrl;
    }

    public static boolean isHomepageUrl(final String unshortenedUrl) {
        String path = null;
        try {
            path = new URL(unshortenedUrl).getPath();
        } catch (final MalformedURLException ex) {
            logger.error("Unable to parse URL: " + unshortenedUrl, ex);
            return false;
        }

        if (path == null || path.length() <= 1) {
            return true;
        }

        return false;
    }

    // util

    static String expandSingleLevel(final String url) throws ClientProtocolException, IOException {
        final DefaultHttpClient client = HttpFactory.httpClient(false);

        final HttpGet request = new HttpGet(url);
        final HttpResponse response = client.execute(request);

        if (response.getStatusLine().getStatusCode() != 301) {
            return url;
        }

        final Header[] headers = response.getHeaders(HttpHeaders.LOCATION);
        Preconditions.checkState(headers.length == 1);
        final String newUrl = headers[0].getValue();

        return newUrl;
    }

}
