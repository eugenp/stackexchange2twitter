package org.rss.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.common.service.BaseTweetFromSourceLiveService;
import org.rss.persistence.dao.IRssEntryJpaDAO;
import org.rss.persistence.model.RssEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.tweet.spring.util.SpringProfileUtil;

import com.google.api.client.util.Preconditions;
import com.google.common.collect.Maps;

@Service
@Profile(SpringProfileUtil.WRITE)
public final class TweetRssLiveService extends BaseTweetFromSourceLiveService<RssEntry> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RssService rssService;

    @Autowired
    private IRssEntryJpaDAO rssEntryApi;

    public TweetRssLiveService() {
        super();
    }

    // API

    public final boolean tweetFromRss(final String rssUri, final String twitterAccount, final String rssOwnerTwitterAccount) {
        try {
            final boolean success = tweetFromRssInternal(rssUri, twitterAccount, rssOwnerTwitterAccount);
            if (!success) {
                logger.warn("Unable to tweet on twitterAccount= {}, by rssUri= {}", twitterAccount, rssUri);
            }
            return success;
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to tweet from RSS on twitterAccount= " + twitterAccount + ", by rssUri= " + rssUri, runtimeEx);
            return false;
        } catch (final Exception ex) {
            logger.error("Unexpected exception when trying to tweet from RSS on twitterAccount= " + twitterAccount + ", by rssUri= " + rssUri, ex);
            return false;
        }
    }

    // util

    private final boolean tweetFromRssInternal(final String rssUri, final String twitterAccount, final String rssOwnerTwitterAccount) {
        logger.debug("Begin trying to tweet from rssUri= {}", rssUri);

        final List<RssEntry> rssEntries = rssService.extractTitlesAndLinks(rssUri);
        for (final RssEntry potentialRssEntry : rssEntries) {
            logger.trace("Considering to tweet on twitterAccount= {}, from rssUri= {}, tweet text= {}", twitterAccount, rssUri, potentialRssEntry);
            if (!hasThisAlreadyBeenTweetedById(new RssEntry(twitterAccount, potentialRssEntry.getLink(), potentialRssEntry.getTitle(), potentialRssEntry.getOriginalPublishDate(), null))) {
                logger.debug("Attempting to tweet on twitterAccount= {}, from rssUri= {}, tweet text= {}", twitterAccount, rssUri, potentialRssEntry);
                final boolean success = tryTweetOneDelegator(potentialRssEntry, twitterAccount, rssOwnerTwitterAccount);
                if (!success) {
                    logger.trace("Didn't tweet on twitterAccount= {}, tweet text= {}", twitterAccount, potentialRssEntry);
                    continue;
                } else {
                    logger.info("Successfully tweeted on twitterAccount= {}, tweet text= {}", twitterAccount, potentialRssEntry);
                    return true;
                }
            }
        }

        logger.debug("Finished tweeting from rssUri= {}", rssUri);
        return false;
    }

    private final boolean tryTweetOneDelegator(final RssEntry potentialRssEntry, final String twitterAccount, final String rssOwnerTwitterAccount) {
        final String textOnly = potentialRssEntry.getTitle();
        final String link = potentialRssEntry.getLink();

        final Map<String, Object> customDetails = Maps.newHashMap();
        customDetails.put("rssEntry", potentialRssEntry);
        customDetails.put("rssOwnerTwitterAccount", rssOwnerTwitterAccount);
        return tryTweetOne(textOnly, link, twitterAccount, customDetails);
    }

    private final boolean shouldBeTweetedByAge(final RssEntry potentialRssEntry) {
        final Date originalPublishDate = potentialRssEntry.getOriginalPublishDate();
        final long DAY_IN_MS = 1000 * 60 * 60 * 24;
        final Date oneDayAgo = new Date(System.currentTimeMillis() - (1 * DAY_IN_MS));
        if (originalPublishDate.after(oneDayAgo)) {
            return true;
        }
        return false;
    }

    // template

    @Override
    protected final boolean tryTweetOne(final String textRaw, final String url, final String twitterAccount, final Map<String, Object> customDetails) {
        logger.trace("Considering to retweet on twitterAccount= {}, RSS title= {}, RSS URL= {}", twitterAccount, textRaw, url);
        final RssEntry potentialRssEntry = (RssEntry) Preconditions.checkNotNull(customDetails.get("rssEntry"));
        final String rssOwnerTwitterAccount = (String) Preconditions.checkNotNull(customDetails.get("rssOwnerTwitterAccount"));
        if (!shouldBeTweetedByAge(potentialRssEntry)) {
            logger.debug("Rss Entry to old to be tweeted; original publish date= {}", potentialRssEntry.getOriginalPublishDate());
            return false;
        }

        // pre-process
        final String cleanTweetText = tweetService.processPreValidity(textRaw);

        // post-process
        String fullyCleanedTweetText = tweetService.postValidityProcessForTweetTextNoUrl(cleanTweetText, twitterAccount);

        // is it valid?
        if (!tweetService.isTweetTextValid(fullyCleanedTweetText)) {
            logger.debug("Tweet invalid (size, link count) on twitterAccount= {}, tweet text= {}", twitterAccount, fullyCleanedTweetText);
            return false;
        }

        if (tweetService.isTweetTextValid("New on @" + rssOwnerTwitterAccount + ": " + fullyCleanedTweetText)) {
            fullyCleanedTweetText = "New on @" + rssOwnerTwitterAccount + ": " + fullyCleanedTweetText;
        }

        // construct full tweet
        final String fullTweet = tweetLiveService.constructTweetLive(fullyCleanedTweetText, url);

        // is it worth it by text only?
        if (!tweetService.isTweetWorthRetweetingByTextWithLink(fullTweet)) {
            return false;
        }

        // is this tweet pointing to something good? - yes

        // is the tweet rejected by some classifier? - no

        // tweet
        final boolean success = twitterWriteLiveService.tweet(twitterAccount, fullTweet);

        // mark
        if (success) {
            markDone(new RssEntry(twitterAccount, url, textRaw, potentialRssEntry.getOriginalPublishDate(), new Date()));
        }

        // done
        return success;
    }

    @Override
    protected final boolean hasThisAlreadyBeenTweetedById(final RssEntry rssEntry) {
        final RssEntry entry = getApi().findOneByLinkAndTwitterAccount(rssEntry.getLink(), rssEntry.getTwitterAccount());
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
