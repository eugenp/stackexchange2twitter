package org.common.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * - note: may return null
     */
    public final String determineMainUrl(final List<String> extractedUrls) {
        for (final String urlCandidate : extractedUrls) {
            if (urlCandidate.contains("plus.google.com") || urlCandidate.endsWith(".git") || urlCandidate.contains("youtube.com")) {
                continue;
            }

            return urlCandidate;
        }

        return null;
    }

    public final List<String> extractUrls(final String input) {
        final List<String> result = new ArrayList<String>();

        final Pattern pattern = Pattern.compile("\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)" + "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov" + "|mil|biz|info|mobi|name|aero|jobs|museum" + "|travel|[a-z]{2}))(:[\\d]{1,5})?"
                + "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?" + "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)" + "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*"
                + "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b");

        final Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }

    // util

}
