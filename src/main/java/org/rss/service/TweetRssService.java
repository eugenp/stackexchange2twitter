package org.rss.service;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.common.service.BaseTweetFromSourceService;
import org.rss.persistence.dao.IRssEntryJpaDAO;
import org.rss.persistence.model.RssEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class TweetRssService extends BaseTweetFromSourceService<RssEntry> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RssService rssService;

    @Autowired
    private IRssEntryJpaDAO rssEntryApi;

    public TweetRssService() {
        super();
    }

    // API

    public final boolean tweetFromRssInternal(final String rssFeedUri, final String twitterAccount) {
        logger.debug("Begin trying to tweet from RSS= {}", rssFeedUri);

        final List<Pair<String, String>> rssEntries = rssService.extractTitlesAndLinks(rssFeedUri);
        for (final Pair<String, String> potentialRssEntry : rssEntries) {
            if (!hasThisAlreadyBeenTweeted(new RssEntry(twitterAccount, potentialRssEntry.getLeft(), potentialRssEntry.getRight()))) {
                logger.info("Trying to tweeting the RssEntry= {}", potentialRssEntry);
                final boolean success = tryTweetOne(potentialRssEntry, twitterAccount);
                if (!success) {
                    logger.trace("Didn't tweet on twitterAccount= {}, tweet text= {}", twitterAccount, potentialRssEntry);
                    continue;
                } else {
                    logger.info("Successfully retweeted on twitterAccount= {}, tweet text= {}", twitterAccount, potentialRssEntry);
                    return true;
                }
            }
        }

        logger.debug("Finished tweeting from RSS= {}", rssFeedUri);
        return false;
    }

    // util

    private final boolean tryTweetOne(final Pair<String, String> potentialRssEntry, final String twitterAccount) {
        final String title = potentialRssEntry.getLeft();
        final String url = potentialRssEntry.getRight();
        logger.trace("Considering to retweet on twitterAccount= {}, RSS title= {}, RSS URL= {}", twitterAccount, title, url);

        // is it worth it by itself?
        if (!tweetService.isTweetTextWorthTweetingByItself(title)) {
            return false;
        }

        // is it worth it in the context of all the current list of tweets? - yes

        // process
        final String tweetText = tweetService.preValidityProcess(title);

        // is it valid?
        if (!tweetService.isTweetTextValid(tweetText)) {
            logger.debug("Tweet invalid (size, link count) on twitterAccount= {}, tweet text= {}", twitterAccount, tweetText);
            return false;
        }

        // // is this tweet pointing to something good? - yes

        // // is the tweet rejected by some classifier? - no

        // post-process
        final String processedTweetText = tweetService.postValidityProcess(tweetText, twitterAccount);

        // construct full tweet
        final String fullTweet = tweetService.constructTweet(processedTweetText, url);

        // tweet
        twitterLiveService.tweet(twitterAccount, fullTweet);

        // mark
        markDone(title, url, twitterAccount);

        // done
        return true;
    }

    @Override
    protected final boolean hasThisAlreadyBeenTweeted(final RssEntry rssEntry) {
        return rssEntryApi.findOneByRssUriAndTitle(rssEntry.getRssUri(), rssEntry.getTitle()) != null;
    }

    private final void markDone(final String title, final String url, final String twitterAccount) {
        final RssEntry rssEntry = new RssEntry(twitterAccount, url, title);
        rssEntryApi.save(rssEntry);
    }

}
