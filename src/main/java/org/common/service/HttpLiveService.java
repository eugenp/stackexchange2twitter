package org.common.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.stackexchange.api.client.HttpFactory;

import com.google.api.client.util.Preconditions;
import com.google.common.net.HttpHeaders;

@Service
public class HttpLiveService implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private DefaultHttpClient client;

    public HttpLiveService() {
        super();
    }

    // API

    /**
     * - note: will return null (in case of any kind of IO error)
     */
    public final String expand(final String urlArg) {
        try {
            return expandInternal(urlArg);
        } catch (final IOException | IllegalStateException ex) {
            logger.error("Error when expanding the url: " + urlArg, ex);
            return null;
        }
    }

    public final String expandInternal(final String urlArg) throws IOException {
        String originalUrl = urlArg;
        String newUrl = expandSingleLevel(originalUrl);
        while (!originalUrl.equals(newUrl)) {
            originalUrl = newUrl;
            newUrl = expandSingleLevel(originalUrl);
        }

        return newUrl;
    }

    // util

    final String expandSingleLevel(final String url) throws IOException {
        HttpGet request = null;
        HttpEntity httpEntity = null;
        InputStream entityContentStream = null;

        try {
            request = new HttpGet(url);
            final HttpResponse httpResponse = client.execute(request);
            httpEntity = httpResponse.getEntity();
            entityContentStream = httpEntity.getContent();

            if (httpResponse.getStatusLine().getStatusCode() != 301) {
                return url;
            }
            final Header[] headers = httpResponse.getHeaders(HttpHeaders.LOCATION);
            Preconditions.checkState(headers.length == 1);
            final String newUrl = headers[0].getValue();

            return newUrl;
        } catch (final IllegalArgumentException uriEx) {
            logger.warn("Unable to parse the URL: " + url, uriEx);
            return url;
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
            if (entityContentStream != null) {
                entityContentStream.close();
            }
            if (httpEntity != null) {
                EntityUtils.consume(httpEntity);
            }
        }
    }

    // spring

    @Override
    public final void afterPropertiesSet() {
        client = HttpFactory.httpClient(false);
    }

}
