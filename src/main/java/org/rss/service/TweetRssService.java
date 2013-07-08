package org.rss.service;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.rss.persistence.dao.IRssEntryJpaDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class TweetRssService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RssService rssService;

    @Autowired
    private IRssEntryJpaDAO rssEntryApi;

    public TweetRssService() {
        super();
    }

    // API

    public final void tweetFromRssInternal(final String rssFeedUri) {
        logger.debug("Begin trying to tweet from RSS= {}", rssFeedUri);

        final List<Pair<String, String>> rssEntries = rssService.extractTitlesAndLinks(rssFeedUri);
        for (final Pair<String, String> potentialRssEntry : rssEntries) {
            if (!hasThisAlreadyBeenTweeted(potentialRssEntry.getLeft(), potentialRssEntry.getRight())) {
                logger.info("Tweeting the RssEntry= {}", potentialRssEntry);
            }
        }

        logger.debug("Finished tweeting from RSS= {}", rssFeedUri);
    }

    // util

    private final boolean hasThisAlreadyBeenTweeted(final String rssUri, final String title) {
        return rssEntryApi.findOneByRssUriAndTitle(rssUri, title) != null;
    }

}
