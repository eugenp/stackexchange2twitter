package org.common.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LinkService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public LinkService() {
        super();
    }

    // API

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

    public final String removeUrlParameters(final String urlWithPotentialParameters) {
        URL url = null;
        try {
            url = new URL(urlWithPotentialParameters);
        } catch (final MalformedURLException ex) {
            logger.error("Unable to parse URL: " + urlWithPotentialParameters, ex);
        }
        if (url.getQuery() == null || url.getQuery().isEmpty()) {
            return urlWithPotentialParameters;
        }

        final StringBuilder urlWithNoParams = new StringBuilder(url.getProtocol());
        final int port = url.getPort();
        if (port > 0 && port != 80) {
            throw new IllegalStateException("Invalid Port: " + port + " for URL: " + urlWithPotentialParameters);
        }

        urlWithNoParams.append("://");
        urlWithNoParams.append(url.getHost());
        urlWithNoParams.append(url.getPath());

        return urlWithNoParams.toString();
    }

    public final boolean isKnownShortenedUrl(final String url) {
        final boolean twitter = url.startsWith("http://t.co/");
        final boolean bitly = url.startsWith("http://bit.ly/");
        final boolean google = url.startsWith("http://goo.gl/");

        return twitter || bitly || google;
    }

    // count links

    public final int countLinksToDomain(final Iterable<String> tweets, final String domain) {
        int count = 0;
        for (final String tweet : tweets) {
            if (tweet.contains(domain)) {
                count++;
            }
        }

        return count;
    }

    // util

}
