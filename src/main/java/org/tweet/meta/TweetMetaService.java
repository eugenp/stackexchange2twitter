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

    public boolean retweetByHashtag(final String twitterAccount, final String hashtag) throws JsonProcessingException, IOException {
        try {
            return retweetByHashtagInternal(twitterAccount, hashtag);
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to retweet", runtimeEx);
        }

        return false;
    }

    // util

    final boolean retweetByHashtagInternal(final String twitterAccount, final String hashtag) throws JsonProcessingException, IOException {
        logger.debug("Begin trying to retweet on account = {}", twitterAccount);

        logger.trace("Trying to retweet on account = {}", twitterAccount);
        final List<Tweet> tweetsOfHashtag = twitterService.listTweetsOfHashtag(twitterAccount, hashtag);
        return tryRetweetByHashtagInternal(twitterAccount, tweetsOfHashtag, hashtag);
    }

    private final boolean tryRetweetByHashtagInternal(final String twitterAccountName, final List<Tweet> potentialTweets, final String hashtag) throws IOException, JsonProcessingException {
        for (final Tweet potentialTweet : potentialTweets) {
            final long tweetId = potentialTweet.getId();
            logger.trace("If not already retweeted, considering to retweet on account= {}, tweetId= {}", twitterAccountName, tweetId);

            if (!hasThisAlreadyBeenTweeted(tweetId)) {
                final boolean success = tryRetweetOne(potentialTweet, twitterAccountName, hashtag);
                if (!success) {
                    logger.trace("Didn't retweet on account= {}, tweet text= {}", twitterAccountName, potentialTweet.getText());
                    continue;
                }
            }
        }

        return false;
    }

    private final boolean tryRetweetOne(final Tweet potentialTweet, final String twitterAccountName, final String hashtag) {
        final long tweetId = potentialTweet.getId();
        logger.trace("Considering to retweet on account= {}, tweetId= {}", twitterAccountName, tweetId);

        // is it worth it?
        if (!isTweetWorthRetweeting(potentialTweet, hashtag)) {
            return false;
        }

        // is it valid?
        final String tweetText = potentialTweet.getText();
        if (!TwitterUtil.isTweetTextValid(tweetText)) {
            logger.debug("Tweet invalid on account= {}, tweet text= {}", twitterAccountName, tweetText);
            return false;
        }

        logger.info("Retweeting: text= {} with id= {}", tweetText, tweetId);

        tweet(tweetText, twitterAccountName);
        markTweetRetweeted(tweetId, twitterAccountName);
        return true;
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
        return 8;
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

    private final void tweet(final String textRaw, final String accountName) {
        final String text = preprocess(textRaw, accountName);

        twitterService.tweet(accountName, text);
    }

    private String preprocess(final String textRaw, final String accountName) {
        final String text = StringEscapeUtils.unescapeHtml4(textRaw);
        return TwitterUtil.hashtagWords(text, wordsToHash(accountName));
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
