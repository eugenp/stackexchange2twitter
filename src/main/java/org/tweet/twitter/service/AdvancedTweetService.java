package org.tweet.twitter.service;

import java.util.List;

import org.common.service.LinkService;
import org.common.util.TextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;
import org.tweet.twitter.component.MinRtRetriever;
import org.tweet.twitter.component.TwitterHashtagsRetriever;
import org.tweet.twitter.util.HashtagWordFunction;
import org.tweet.twitter.util.TwitterUtil;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * - local
 */
@Service
public class AdvancedTweetService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterHashtagsRetriever twitterHashtagsRetriever;

    @Autowired
    private MinRtRetriever minRtRetriever;

    @Autowired
    LinkService linkService;

    @Autowired
    TweetService tweetService;

    public AdvancedTweetService() {
        super();
    }

    // API

    // checks

    /**
     * Determines if a tweet is worth retweeting based on the following criteria: <br/>
     * - <b>number of retweets</b> over a certain threshold (the threshold is per hashtag) <br/>
     * - is in <b>English</b> <br/>
     * - the <b>user</b> is not banned for retweeting <br/>
     * - does not contain to many hashtags (7 is max) <br/>
     * 
     * - favorites are not yet considered <br/>
     */
    public final boolean isTweetWorthRetweetingByRawTweet(final Tweet tweet, final String hashtag) {
        if (!tweetService.passesSet1OfChecks(tweet, hashtag)) {
            return false;
        }

        if (!tweetService.passesLanguageChecksForTweeting(tweet, hashtag)) {
            return false;
        }

        final int requiredMinRts = minRtRetriever.minRt(hashtag);
        if (tweet.getRetweetCount() < requiredMinRts) {
            // TODO: this is a problem now that the tweets are no longer strictly sorted by RT count
            logger.trace("tweet= {} on twitterTag= {} \nrejected because it only has= {} retweets and it needs= {}", tweet, hashtag, tweet.getRetweetCount(), requiredMinRts);
            return false;
        }

        return true;
    }

    // processing

    /**
     * - cleans the invalid characters from text
     */
    public final String processPreValidity(final String text) {
        String resultAfterCleanup = TextUtil.cleanupInvalidCharacters(text);
        resultAfterCleanup = TextUtil.cleanupInvalidCharacters(resultAfterCleanup);
        resultAfterCleanup = TextUtil.trimTweet(resultAfterCleanup);

        return resultAfterCleanup;
    }

    /**
     * - adds hashtags, trims <br/>
     * - <b>note</b>: the text should be the full tweet (including url) <br/>
     */
    public final String postValidityProcessTweetTextWithUrl(final String fullTweet, final String twitterAccount) {
        String tweetTextProcessed = hashtagWordsFullTweet(fullTweet, twitterTagsToHash(twitterAccount));
        if (tweetTextProcessed.startsWith("\"") && tweetTextProcessed.endsWith("\"")) {
            tweetTextProcessed = tweetTextProcessed.substring(1, tweetTextProcessed.length() - 1);
        }

        return tweetTextProcessed;
    }

    /**
     * - adds hashtags, trims <br/>
     * - <b>note</b>: the text should be the tweet text only (no url) <br/>
     */
    public final String postValidityProcessForTweetTextNoUrl(final String textOnly, final String twitterAccount) {
        String tweetTextProcessed = hashtagWordsTweetTextOnly(textOnly, twitterTagsToHash(twitterAccount));
        if (tweetTextProcessed.startsWith("\"") && tweetTextProcessed.endsWith("\"")) {
            // status - no need for more than trace
            logger.trace("Cleaning up \"; final original text= {}", tweetTextProcessed);
            tweetTextProcessed = tweetTextProcessed.substring(1, tweetTextProcessed.length() - 1);
        }

        return tweetTextProcessed;
    }

    public final String constructTweetSimple(final String tweetTextNoUrl, final String url) {
        Preconditions.checkNotNull(tweetTextNoUrl);
        Preconditions.checkNotNull(url);

        final String textOfTweet = tweetTextNoUrl;
        final String tweet = textOfTweet + " - " + url;
        return tweet;
    }

    // util

    final String hashtagWordsFullTweet(final String fullTweet, final List<String> wordsToHash) {
        final Iterable<String> tokens = TwitterUtil.splitter.split(fullTweet);

        final HashtagWordFunction hashtagWordFunction = new HashtagWordFunction(wordsToHash);
        final Iterable<String> transformedTokens = Iterables.transform(tokens, hashtagWordFunction);

        final String processedTweet = TwitterUtil.joiner.join(transformedTokens);

        // check that hashtags + original tweet do not go over 141 chars
        if (fullTweet.length() + hashtagWordFunction.getTransformationsDone() > 141) {
            return fullTweet;
        }

        return processedTweet;
    }

    private final String hashtagWordsTweetTextOnly(final String tweetTextOnly, final List<String> wordsToHash) {
        final Iterable<String> tokens = TwitterUtil.splitter.split(tweetTextOnly);

        final HashtagWordFunction hashtagWordFunction = new HashtagWordFunction(wordsToHash);
        final Iterable<String> transformedTokens = Iterables.transform(tokens, hashtagWordFunction);

        final String processedTweet = TwitterUtil.joiner.join(transformedTokens);

        // check that hashtags + original tweet do not go over 142 chars
        if (tweetTextOnly.length() + hashtagWordFunction.getTransformationsDone() > 122) {
            return tweetTextOnly;
        }

        return processedTweet;
    }

    private final List<String> twitterTagsToHash(final String twitterAccount) {
        final String wordsToHashForAccount = twitterHashtagsRetriever.hashtags(twitterAccount);
        final Iterable<String> split = Splitter.on(',').split(wordsToHashForAccount);
        return Lists.newArrayList(split);
    }

}
