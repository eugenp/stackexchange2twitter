package org.rss.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@Service
public class RssService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public RssService() {
        super();
    }

    // API

    public final List<Pair<String, String>> extractTitlesAndLinks(final String rssUri) {
        try {
            return extractTitlesAndLinksInternal(rssUri);
        } catch (IllegalArgumentException | IOException | FeedException ex) {
            logger.error("Unable to parse feed", ex);
            return null;
        }
    }

    // util

    final void printEntry(final SyndEntry entry) {
        System.out.println("Title: " + entry.getTitle());
        System.out.println("Link: " + entry.getLink());
        System.out.println("Author: " + entry.getAuthor());
        System.out.println("Publish Date: " + entry.getPublishedDate());
        System.out.println("Description: " + entry.getDescription().getValue());
        System.out.println();
    }

    @SuppressWarnings("unchecked")
    final List<Pair<String, String>> extractTitlesAndLinksInternal(final String rssUri) throws IOException, IllegalArgumentException, FeedException {
        final URL url = new URL(rssUri);
        final HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        // Reading the feed
        final SyndFeedInput input = new SyndFeedInput();
        final SyndFeed feed = input.build(new XmlReader(httpcon));
        final List<SyndEntry> entries = feed.getEntries();

        final List<Pair<String, String>> collector = Lists.newArrayList();
        for (final SyndEntry entry : entries) {
            collector.add(new ImmutablePair<String, String>(entry.getTitle(), entry.getLink()));
        }

        return collector;
    }

}
