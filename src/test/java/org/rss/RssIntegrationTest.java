package org.rss;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RssIntegrationTest {

    @Test
    public final void whenConsumingAnRssFeed_theeptions() throws IOException, IllegalArgumentException, FeedException {
        final URL url = new URL("http://feeds.feedburner.com/baeldung");
        final HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        // Reading the feed
        final SyndFeedInput input = new SyndFeedInput();
        final SyndFeed feed = input.build(new XmlReader(httpcon));
        final List entries = feed.getEntries();
        final Iterator itEntries = entries.iterator();

        while (itEntries.hasNext()) {
            final SyndEntry entry = (SyndEntry) itEntries.next();
            System.out.println("Title: " + entry.getTitle());
            System.out.println("Link: " + entry.getLink());
            System.out.println("Author: " + entry.getAuthor());
            System.out.println("Publish Date: " + entry.getPublishedDate());
            System.out.println("Description: " + entry.getDescription().getValue());
            System.out.println();
        }
    }

}
