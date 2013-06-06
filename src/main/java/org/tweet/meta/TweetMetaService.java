package org.tweet.meta;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.twitter.service.TwitterService;
import org.tweet.twitter.util.TwitterUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

@Service
public class TweetMetaService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private Environment env;

    @Autowired
    private IRetweetJpaDAO retweetApi;

    public TweetMetaService() {
        super();
    }

    // API

    // write

    public void tweetTopQuestionByHashtag(final String twitterAccount, final String hashtag) throws JsonProcessingException, IOException {
        try {
            tweetTopQuestionByHashtagInternal(twitterAccount, hashtag);
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to tweet", runtimeEx);
        }
    }

    // util

    final void tweetTopQuestionByHashtagInternal(final String twitterAccount, final String hashtag) throws JsonProcessingException, IOException {
        logger.debug("Begin trying to retweet on account = {}", twitterAccount);

        logger.trace("Trying to tweeting on account = {}", twitterAccount);
        final List<Tweet> tweetsOfHashtag = twitterService.listTweetsOfHashtag(twitterAccount, hashtag);
        tryTweetTopQuestionByHashtagInternal(twitterAccount, tweetsOfHashtag, hashtag);
    }

    private final boolean tryTweetTopQuestionByHashtagInternal(final String twitterAccountName, final List<Tweet> potentialTweets, final String hashtag) throws IOException, JsonProcessingException {
        for (final Tweet potentialTweet : potentialTweets) {
            final long tweetId = potentialTweet.getId();
            logger.trace("If not already retweeted, considering to retweet on account= {}, tweetId= {}", twitterAccountName, tweetId);

            if (!hasThisAlreadyBeenTweeted(tweetId)) {
                logger.debug("Considering to retweet on account= {}, tweetId= {}", twitterAccountName, tweetId);
                if (!isTweetWorthRetweeting(potentialTweet, hashtag)) {
                    continue;
                }

                final String tweetText = potentialTweet.getText();
                logger.info("Retweeting: text= {} with id= {}", tweetText, tweetId);
                final boolean success = tryTweet(tweetText, twitterAccountName);
                if (!success) {
                    logger.debug("Tried and failed to retweet on account= {}, tweet text= {}", twitterAccountName, tweetText);
                    continue;
                }
                markTweetRetweeted(tweetId, twitterAccountName);
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if a tweet is worth retweeting based on the following criteria: 
     * - number of retweets over a certain threshold (the threshold is per hashtag)
     * - number of favorites
     */
    private boolean isTweetWorthRetweeting(final Tweet potentialTweet, final String hashtag) {
        if (potentialTweet.getRetweetCount() < getRetweetThreasholdForHashtag(hashtag)) {
            return false;
        }
        if (tweetContainsBannedKeywords(potentialTweet.getText())) {
            return false;
        }
        return true;
    }

    /**
     * TODO: figure out an adaptive retweet threshold for the given hashtag
     * for example, #java may get a lot of retweets, and so the threshold may be higher than say #hadoop 
     */
    private final int getRetweetThreasholdForHashtag(final String hashtag) {
        return 15;
    }

    /**
     * TODO: banned words: freelance, job, consulting, etc
     */
    private final boolean tweetContainsBannedKeywords(final String text) {
        return false;
    }

    private final boolean hasThisAlreadyBeenTweeted(final long tweetId) {
        final Retweet existingTweet = retweetApi.findByTweetId(tweetId);
        return existingTweet != null;
    }

    private final boolean tryTweet(final String textRaw, final String accountName) {
        final String text = StringEscapeUtils.unescapeHtml4(textRaw);
        if (!TwitterUtil.isTweetTextValid(text)) {
            return false;
        }

        // temporary try-catch
        try {
            TwitterUtil.hashWords(textRaw, wordsToHash(accountName));
        } catch (final RuntimeException ex) {
            logger.error("Error postprocessing the tweet" + textRaw, ex);
        }

        twitterService.tweet(accountName, textRaw);
        return true;
    }

    private final void markTweetRetweeted(final long tweetId, final String accountName) {
        final Retweet retweet = new Retweet(tweetId, accountName);
        retweetApi.save(retweet);
    }

    private final List<String> wordsToHash(final String accountName) {
        final String wordsToHashForAccount = env.getProperty(accountName + ".hash");
        final Iterable<String> split = Splitter.on(',').split(wordsToHashForAccount);
        return Lists.newArrayList(split);
    }

}
