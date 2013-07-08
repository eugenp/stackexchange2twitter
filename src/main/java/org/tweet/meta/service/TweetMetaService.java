package org.tweet.meta.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.common.service.BaseTweetFromSourceService;
import org.common.service.HttpService;
import org.common.service.classification.ClassificationService;
import org.common.text.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.twitter.component.MaxRtRetriever;
import org.tweet.twitter.service.TagRetrieverService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Ordering;

@Service
public class TweetMetaService extends BaseTweetFromSourceService<Retweet> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TagRetrieverService tagService;

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private HttpService httpService;

    @Autowired
    private IRetweetJpaDAO retweetApi;

    @Autowired
    private MaxRtRetriever maxRtRetriever;

    public TweetMetaService() {
        super();
    }

    // API

    // write

    public boolean retweetByHashtag(final String twitterAccount) throws JsonProcessingException, IOException {
        final String twitterTag = tagService.pickTwitterTag(twitterAccount);
        return retweetByHashtag(twitterAccount, twitterTag);
    }

    boolean retweetByHashtag(final String twitterAccount, final String twitterTag) throws JsonProcessingException, IOException {
        try {
            final boolean success = retweetByHashtagInternal(twitterAccount, twitterTag);
            if (!success) {
                logger.warn("Unable to retweet any tweet on twitterAccount = {}, by twitterTag = {}", twitterAccount, twitterTag);
            }
            return success;
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to retweet", runtimeEx);
        }

        return false;
    }

    // util

    final boolean retweetByHashtagInternal(final String twitterAccount, final String hashtag) throws JsonProcessingException, IOException {
        logger.debug("Begin trying to retweet on twitterAccount = {}", twitterAccount);

        logger.trace("Trying to retweet on twitterAccount = {}", twitterAccount);
        final List<Tweet> tweetsOfHashtag = twitterLiveService.listTweetsOfHashtag(twitterAccount, hashtag);
        Collections.sort(tweetsOfHashtag, Ordering.from(new Comparator<Tweet>() {
            @Override
            public final int compare(final Tweet t1, final Tweet t2) {
                return t2.getRetweetCount().compareTo(t1.getRetweetCount());
            }
        }));

        return tryRetweetByHashtagInternal(twitterAccount, tweetsOfHashtag, hashtag);
    }

    private final boolean tryRetweetByHashtagInternal(final String twitterAccount, final List<Tweet> potentialTweets, final String hashtag) throws IOException, JsonProcessingException {
        for (final Tweet potentialTweet : potentialTweets) {
            final long tweetId = potentialTweet.getId();
            logger.trace("If not already retweeted, considering to retweet on twitterAccount= {}, tweetId= {}", twitterAccount, tweetId);

            if (!hasThisAlreadyBeenTweeted(new Retweet(tweetId, twitterAccount))) {
                final boolean success = tryTweetOne(potentialTweet, twitterAccount, hashtag);
                if (!success) {
                    logger.trace("Didn't retweet on twitterAccount= {}, tweet text= {}", twitterAccount, potentialTweet.getText());
                    continue;
                } else {
                    logger.info("Successfully retweeted on twitterAccount= {}, tweet text= {}\n --- Additional meta info: id= {}, rt= {}", twitterAccount, potentialTweet.getText(), tweetId, potentialTweet.getRetweetCount());
                    return true;
                }
            }
        }

        return false;
    }

    private final boolean tryTweetOne(final Tweet potentialTweet, final String twitterAccount, final String hashtag) {
        final String text = potentialTweet.getText();
        final long tweetId = potentialTweet.getId();
        logger.trace("Considering to retweet on twitterAccount= {}, tweetId= {}, tweetText= {}", twitterAccount, tweetId, text);

        // is it worth it by itself?
        if (!tweetService.isTweetWorthRetweetingByItself(text)) {
            return false;
        }

        // is it worth it in the context of all the current list of tweets?
        if (!isTweetWorthRetweetingInContext(potentialTweet, hashtag)) {
            return false;
        }

        // pre-process
        final String tweetText = tweetService.preValidityProcess(text);

        // is it valid?
        if (!tweetService.isTweetTextValid(tweetText)) {
            logger.debug("Tweet invalid (size, link count) on twitterAccount= {}, tweet text= {}", twitterAccount, tweetText);
            return false;
        }

        // is this tweet pointing to something good?
        if (!isTweetPointingToSomethingGood(tweetText)) {
            logger.debug("Tweet not pointing to something good on twitterAccount= {}, tweet text= {}", twitterAccount, tweetText);
            return false;
        }

        // is the tweet rejected by some classifier?
        if (isTweetRejectedByClassifier(tweetText)) {
            logger.debug("Tweet rejected by a classifier on twitterAccount= {}, tweet text= {}", twitterAccount, tweetText);
            return false;
        }

        // post-process
        final String processedTweetText = tweetService.postValidityProcess(tweetText, twitterAccount);

        // tweet
        twitterLiveService.tweet(twitterAccount, processedTweetText);

        // mark
        markDone(tweetId, twitterAccount);

        // done
        return true;
    }

    private boolean isTweetRejectedByClassifier(final String text) {
        if (classificationService.isCommercial(text)) {
            // return true;
            return false; // temporarily, until there is more classification training data for commercial-noncommercial
        }
        return false;
    }

    private boolean isTweetPointingToSomethingGood(final String potentialTweet) {
        String singleMainUrl = TextUtils.extractUrls(potentialTweet).get(0);
        try {
            singleMainUrl = httpService.expandInternal(singleMainUrl);
        } catch (final RuntimeException ex) {
            logger.error("Unable to expand URL: " + singleMainUrl, ex); // may become warn
            return false;
        } catch (final IOException ioEx) {
            logger.error("Unable to expand URL: " + singleMainUrl, ioEx);
            return false;
        }

        if (httpService.isHomepageUrl(singleMainUrl)) {
            return false;
        }

        return true;
    }

    /**
     * Determines if a tweet is worth retweeting based on the following criteria: 
     * - number of retweets over a certain threshold (the threshold is per hashtag)
     * - number of favorites (not yet)
     */
    private boolean isTweetWorthRetweetingInContext(final Tweet potentialTweet, final String twitterTag) {
        if (potentialTweet.getRetweetCount() < maxRtRetriever.maxRt(twitterTag)) {
            return false;
        }
        return true;
    }

    @Override
    protected final boolean hasThisAlreadyBeenTweeted(final Retweet retweet) {
        final Retweet existingTweet = retweetApi.findByTweetId(retweet.getTweetId());
        return existingTweet != null;
    }

    private final void markDone(final long tweetId, final String twitterAccount) {
        final Retweet retweet = new Retweet(tweetId, twitterAccount);
        retweetApi.save(retweet);
    }

}
