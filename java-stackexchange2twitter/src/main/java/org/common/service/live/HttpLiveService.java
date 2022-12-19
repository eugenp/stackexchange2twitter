package org.common.service.live;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
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
import com.google.common.collect.Lists;
import com.google.common.net.HttpHeaders;

@Service
@Profile(SpringProfileUtil.LIVE)
public class HttpLiveService implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private HttpClient client;

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
                // target may be down is a valid option - the stack should be hidden if not debuggin
                logger.info("Target host may be down - error when expanding the url: " + urlArg);
                logger.debug("Target host may be down - error when expanding the url: " + urlArg, ex);
                return null;
            }
            if (cause != null && cause instanceof ConnectTimeoutException) {
                // keep at warn or below - no need to know when this happens all the time
                logger.warn("Target host may be timing out - error when expanding the url: " + urlArg, ex);
                return null;
            }
            if (cause != null && cause instanceof SocketTimeoutException) {
                // keep at warn or below - no need to know when this happens all the time
                logger.warn("Target host socket data may be timing out - error when expanding the url: " + urlArg);
                logger.debug("Target host socket data may be timing out - error when expanding the url: " + urlArg, ex);
                return null;
            }
            // connection refused
            if (cause != null && cause instanceof HttpHostConnectException) {
                // keep at warn or below - no need to know when this happens all the time
                logger.warn("Connection to target host was refused - error when expanding the url: " + urlArg, ex);
                return null;
            }
            if (cause != null && cause instanceof NoHttpResponseException) {
                // keep at warn or below - no need to know when this happens all the time
                logger.warn("No response from connection to target host was refused - error when expanding the url: " + urlArg, ex);
                return null;
            }

            logger.error("Error when expanding the url: " + urlArg, ex);
            return null;
        }
    }

    final String expandInternal(final String urlArg) throws IOException {
        final String originalUrlWithParams = urlArg;
        String originalUrl = linkService.removeUrlParameters(originalUrlWithParams);
        String newUrl = expandSingleLevel(originalUrl).getRight();
        final List<String> alreadyVisited = Lists.newArrayList(originalUrl, newUrl);
        while (!originalUrl.equals(newUrl)) {
            originalUrl = newUrl;
            final Pair<Integer, String> statusAndUrl = expandSingleLevel(originalUrl);
            newUrl = statusAndUrl.getRight();
            if ((statusAndUrl.getLeft() == 301 || statusAndUrl.getLeft() == 302) && alreadyVisited.contains(newUrl)) {
                throw new IllegalStateException("Likely a redirect loop");
            }
            alreadyVisited.add(newUrl);
        }

        return newUrl;
    }

    // util

    final Pair<Integer, String> expandSingleLevel(final String url) throws IOException {
        HttpGet request = null;
        HttpEntity httpEntity = null;

        try {
            request = new HttpGet(url);
            final HttpResponse httpResponse = client.execute(request);
            metrics.counter(MetricsUtil.Meta.HTTP_OK);

            httpEntity = httpResponse.getEntity();

            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != 301 && statusCode != 302) {
                return new ImmutablePair<Integer, String>(statusCode, url);
            }
            final Header[] headers = httpResponse.getHeaders(HttpHeaders.LOCATION);
            Preconditions.checkState(headers.length == 1);
            String newUrl = headers[0].getValue();
            newUrl = processNewUrl(url, newUrl);

            return new ImmutablePair<Integer, String>(statusCode, newUrl);
        } catch (final IllegalArgumentException uriEx) {
            logger.warn("Unable to parse the URL: " + url, uriEx);
            metrics.counter(MetricsUtil.Meta.HTTP_ERR);
            return new ImmutablePair<Integer, String>(500, url);
        } catch (final IOException ex) {
            metrics.counter(MetricsUtil.Meta.HTTP_ERR);
            throw new IllegalStateException(ex);
        } finally {
            if (httpEntity != null) {
                EntityUtils.consume(httpEntity);
            }
            if (request != null) {
                request.releaseConnection();
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
        // client = HttpFactory.httpClientOld(false);
    }

}
