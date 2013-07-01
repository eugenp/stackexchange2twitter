package org.common.service;

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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.stackexchange.api.client.HttpFactory;

import com.google.api.client.util.Preconditions;
import com.google.common.net.HttpHeaders;

@Service
public class HttpService implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private DefaultHttpClient client;

    public HttpService() {
        super();
    }

    // API

    public final String expand(final String urlArg) throws ClientProtocolException, IOException {
        String originalUrl = urlArg;
        String newUrl = expandSingleLevel(originalUrl);
        while (!originalUrl.equals(newUrl)) {
            originalUrl = newUrl;
            newUrl = expandSingleLevel(originalUrl);
        }

        return newUrl;
    }

    public final boolean isHomepageUrl(final String unshortenedUrl) {
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

    final String expandSingleLevel(final String url) throws ClientProtocolException, IOException {
        HttpGet request = null;
        try {
            request = new HttpGet(url);
            final HttpResponse httpResponse = client.execute(request);
            if (httpResponse.getStatusLine().getStatusCode() != 301) {
                return url;
            }
            final Header[] headers = httpResponse.getHeaders(HttpHeaders.LOCATION);
            Preconditions.checkState(headers.length == 1);
            final String newUrl = headers[0].getValue();

            return newUrl;
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
        }
    }

    // spring

    @Override
    public final void afterPropertiesSet() {
        client = HttpFactory.httpClient(false);
    }

}
