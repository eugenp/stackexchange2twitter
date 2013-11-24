package org.common.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Set;

import org.common.util.LinkUtil;
import org.common.util.LinkUtil.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * - local
 */
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

    /**
     * - local
     */
    public final String removeUrlParameters(final String urlWithPotentialParameters) {
        Preconditions.checkNotNull(urlWithPotentialParameters);

        URL url = null;
        try {
            url = new URL(urlWithPotentialParameters);
        } catch (final MalformedURLException ex) {
            logger.error("Unable to parse URL: " + urlWithPotentialParameters, ex);
            return urlWithPotentialParameters;
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

    public final StringBuilder getFullHostOfUrl(final String urlRaw) {
        URL url = null;
        try {
            url = new URL(urlRaw);
        } catch (final MalformedURLException ex) {
            logger.error("Unable to parse URL: " + urlRaw, ex);
            return null;
        }

        final StringBuilder urlBuilder = new StringBuilder(url.getProtocol());
        final int port = url.getPort();
        if (port > 0 && port != 80) {
            throw new IllegalStateException("Invalid Port: " + port + " for URL: " + urlRaw);
        }

        urlBuilder.append("://");
        urlBuilder.append(url.getHost());

        return urlBuilder;
    }

    public final boolean isKnownShortenedUrl(final String url) {
        final boolean twitter = url.startsWith("http://t.co/");
        final boolean bitly = url.startsWith("http://bit.ly/");
        final boolean google = url.startsWith("http://goo.gl/");

        return twitter || bitly || google;
    }

    /**
     * - note: may return null
     */
    public final String extractUrl(final String textWithUrl) {
        final String mainUrl = determineMainUrl(extractUrls(textWithUrl));
        return mainUrl;
    }

    /**
     * - <b>local</b> <br/>
     * - <b>note</b>: may return null
     */
    public final String determineMainUrl(final Set<String> extractedUrls) {
        final int size = extractedUrls.size();
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            return extractedUrls.iterator().next();
        }

        final Collection<String> extractedUrlsWithoutBannedDomains = Collections2.filter(extractedUrls, new Predicate<String>() {
            @Override
            public final boolean apply(final String input) {
                for (final String banned : Common.bannedDomainsByContainsMaybe) {
                    if (input.contains(banned)) {
                        return false;
                    }
                }
                return true;
            }
        });

        if (!extractedUrlsWithoutBannedDomains.isEmpty()) {
            return extractedUrlsWithoutBannedDomains.iterator().next();
        }

        return extractedUrls.iterator().next();
    }

    /**
     * - <b>local</b> <br/>
     * - note: will NOT return null
     */
    public final Set<String> extractUrls(final String input) {
        return LinkUtil.extractUrls(input);
    }

    // util

}
