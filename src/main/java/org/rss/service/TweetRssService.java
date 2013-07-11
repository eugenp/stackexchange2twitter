package org.rss.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.common.service.BaseTweetFromSourceService;
import org.rss.persistence.dao.IRssEntryJpaDAO;
import org.rss.persistence.model.RssEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

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

    public final boolean tweetFromRss(final String rssUri, final String twitterAccount) throws JsonProcessingException, IOException {
        try {
            final boolean success = tweetFromRssInternal(rssUri, twitterAccount);
            if (!success) {
                logger.warn("Unable to tweet on twitterAccount= {}, by rssUri= {}", twitterAccount, rssUri);
            }
            return success;
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to tweet from RSS", runtimeEx);
            return false;
        }
    }

    // util

    private final boolean tweetFromRssInternal(final String rssUri, final String twitterAccount) {
        logger.debug("Begin trying to tweet from RSS= {}", rssUri);

        final List<Pair<String, String>> rssEntries = rssService.extractTitlesAndLinks(rssUri);
        for (final Pair<String, String> potentialRssEntry : rssEntries) {
            logger.trace("Considering to tweet on twitterAccount= {}, from RSS= {}, tweet text= {}", twitterAccount, rssUri, potentialRssEntry);
            if (!hasThisAlreadyBeenTweeted(new RssEntry(twitterAccount, potentialRssEntry.getRight(), potentialRssEntry.getLeft()))) {
                logger.debug("Attempting to tweet on twitterAccount= {}, from RSS= {}, tweet text= {}", twitterAccount, rssUri, potentialRssEntry);
                final boolean success = tryTweetOneDelegator(potentialRssEntry, twitterAccount);
                if (!success) {
                    logger.trace("Didn't tweet on twitterAccount= {}, tweet text= {}", twitterAccount, potentialRssEntry);
                    continue;
                } else {
                    logger.info("Successfully retweeted on twitterAccount= {}, tweet text= {}", twitterAccount, potentialRssEntry);
                    return true;
                }
            }
        }

        logger.debug("Finished tweeting from RSS= {}", rssUri);
        return false;
    }

    private final boolean tryTweetOneDelegator(final Pair<String, String> potentialRssEntry, final String twitterAccount) {
        final String textOnly = potentialRssEntry.getLeft();
        final String url = potentialRssEntry.getRight();

        return tryTweetOne(textOnly, url, twitterAccount, null);
    }

    // template

    @Override
    protected final boolean tryTweetOne(final String textOnly, final String url, final String twitterAccount, final Map<String, Object> customDetails) {
        logger.trace("Considering to retweet on twitterAccount= {}, RSS title= {}, RSS URL= {}", twitterAccount, textOnly, url);

        // is it worth it by itself?
        if (!tweetService.isTweetTextWorthTweetingByItself(textOnly)) {
            return false;
        }

        // is it worth it in the context of all the current list of tweets? - yes

        // pre-process
        final String tweetText = tweetService.preValidityProcess(textOnly);

        // is it valid?
        if (!tweetService.isTweetTextValid(tweetText)) {
            logger.debug("Tweet invalid (size, link count) on twitterAccount= {}, tweet text= {}", twitterAccount, tweetText);
            return false;
        }

        // is this tweet pointing to something good? - yes

        // is the tweet rejected by some classifier? - no

        // post-process
        final String processedTweetText = tweetService.postValidityProcess(tweetText, twitterAccount);

        // construct full tweet
        final String fullTweet = tweetService.constructTweet(processedTweetText, url);

        // tweet
        twitterLiveService.tweet(twitterAccount, fullTweet);

        // mark
        markDone(new RssEntry(twitterAccount, url, textOnly));

        // done
        return true;
    }

    @Override
    protected final boolean hasThisAlreadyBeenTweeted(final RssEntry rssEntry) {
        final RssEntry entry = getApi().findOneByRssUriAndTwitterAccount(rssEntry.getRssUri(), rssEntry.getTwitterAccount());
        return entry != null;
    }

    @Override
    protected final void markDone(final RssEntry rssEntry) {
        rssEntryApi.save(rssEntry);
    }

    @Override
    protected final IRssEntryJpaDAO getApi() {
        return rssEntryApi;
    }

}
