package org.rss.service;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class TweetRssService {

    @Autowired
    private RssService rssService;

    public TweetRssService() {
        super();
    }

    // API

    public final void tweetFromRss(final String rssFeedUri) {
        final List<Pair<String, String>> rssEntries = rssService.extractTitlesAndLinks(rssFeedUri);
        for (final Pair<String, String> entry : rssEntries) {
            System.out.println(entry);
        }
    }

}
