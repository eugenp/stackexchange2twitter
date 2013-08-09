package org.common.service.live;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.common.metrics.MetricsUtil;
import org.common.service.LinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.stackexchange.api.client.HttpFactory;
import org.tweet.spring.util.SpringProfileUtil;

import com.codahale.metrics.MetricRegistry;
import com.google.api.client.util.Preconditions;
import com.google.common.net.HttpHeaders;

@Service
@Profile(SpringProfileUtil.LIVE)
public class HttpLiveService implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private DefaultHttpClient client;

    @Autowired
    private LinkService linkService;

    @Autowired
    private MetricRegistry metrics;

    public HttpLiveService() {
        super();
    }

    // API

    /**
     * - <b>live</b><br/>
     * - note: will return null (in case of any kind of IO error) or null input
     */
    public final String expand(final String urlArg) {
        if (urlArg == null) {
            return null;
        }
        try {
            final String expandedInternal = expandInternal(urlArg);
            return expandedInternal;
        } catch (final IOException | IllegalStateException ex) {
            final Throwable cause = ex.getCause();
            if (cause != null && cause instanceof UnknownHostException) {
                // keep at warn or below - no need to know when this happens all the time
                logger.warn("Target host may be down - error when expanding the url: " + urlArg, ex);
                return null;
            }
            if (cause != null && cause instanceof ConnectTimeoutException) {
                // keep at warn or below - no need to know when this happens all the time
                logger.warn("Target host may be timing out - error when expanding the url: " + urlArg, ex);
                return null;
            }
            if (cause != null && cause instanceof SocketTimeoutException) {
                // keep at warn or below - no need to know when this happens all the time
                logger.warn("Target host socket data may be timing out - error when expanding the url: " + urlArg, ex);
                return null;
            }
            // connection refused
            if (cause != null && cause instanceof HttpHostConnectException) {
                // keep at warn or below - no need to know when this happens all the time
                logger.warn("Connection to target host was refused - error when expanding the url: " + urlArg, ex);
                return null;
            }

            logger.error("Error when expanding the url: " + urlArg, ex);
            return null;
        }
    }

    final String expandInternal(final String urlArg) throws IOException {
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
            metrics.counter(MetricsUtil.Meta.HTTP_OK);

            httpEntity = httpResponse.getEntity();
            entityContentStream = httpEntity.getContent();

            if (httpResponse.getStatusLine().getStatusCode() != 301) {
                return url;
            }
            final Header[] headers = httpResponse.getHeaders(HttpHeaders.LOCATION);
            Preconditions.checkState(headers.length == 1);
            String newUrl = headers[0].getValue();
            newUrl = processNewUrl(url, newUrl);

            return newUrl;
        } catch (final IllegalArgumentException uriEx) {
            logger.warn("Unable to parse the URL: " + url, uriEx);
            metrics.counter(MetricsUtil.Meta.HTTP_ERR);
            return url;
        } catch (final IOException ex) {
            metrics.counter(MetricsUtil.Meta.HTTP_ERR);
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

    private final String processNewUrl(final String url, final String newUrl) {
        if (newUrl.startsWith("/")) {
            final String referenfeHost = linkService.getFullHostOfUrl(url).toString();
            return referenfeHost + newUrl;
        }
        return newUrl;
    }

    // spring

    @Override
    public final void afterPropertiesSet() {
        client = HttpFactory.httpClient(false);
    }

}
