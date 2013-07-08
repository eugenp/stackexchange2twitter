package org.tweet.twitter.service;

import java.util.List;

import org.common.service.HttpService;
import org.common.text.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tweet.twitter.component.TwitterHashtagsRetriever;
import org.tweet.twitter.util.TwitterUtil;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

@Service
public class TweetService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterHashtagsRetriever twitterHashtagsRetriever;

    @Autowired
    private HttpService httpService;

    public TweetService() {
        super();
    }

    // API

    /**
     * Determines if a tweet is worth tweeting based on the following  <b>criteria</b>: <br/>
     * - has link <br/>
     * - contains any banned keywords <br/>
     * - is not already a retweet <br/>
     */
    public final boolean isTweetWorthRetweetingByItself(final String potentialTweet) {
        if (!containsLink(potentialTweet)) {
            return false;
        }
        if (TwitterUtil.tweetContainsBannedKeywords(potentialTweet)) {
            return false;
        }
        if (isRetweet(potentialTweet)) {
            return false;
        }
        return true;
    }

    /**
     * Determines if a tweet is worth tweeting based on the following  <b>criteria</b>: <br/>
     * - contains any banned keywords <br/>
     * - is not already a retweet <br/>
    */
    public final boolean isTweetTextWorthTweetingByItself(final String potentialTweetText) {
        if (containsLink(potentialTweetText)) {
            return false;
        }
        if (TwitterUtil.tweetContainsBannedKeywords(potentialTweetText)) {
            return false;
        }
        if (isRetweet(potentialTweetText)) {
            return false;
        }
        return true;
    }

    public final String preValidityProcess(final String title) {
        return TextUtils.preProcessTweetText(title);
    }

    /**
     * - <b>note</b>: accepts tweet text with or without the URL
     */
    public final String postValidityProcess(final String tweetText, final String twitterAccount) {
        return TwitterUtil.hashtagWords(tweetText, twitterTagsToHash(twitterAccount));
    }

    public final boolean isTweetTextValid(final String tweetTextNoUrl) {
        return TwitterUtil.isTweetTextValid(tweetTextNoUrl);
    }

    public final String constructTweetSimple(final String tweetTextNoUrl, final String url) {
        Preconditions.checkNotNull(tweetTextNoUrl);
        Preconditions.checkNotNull(url);

        final String textOfTweet = tweetTextNoUrl;
        final String tweet = textOfTweet + " - " + url;
        return tweet;
    }

    public final String constructTweet(final String text, final String url) {
        Preconditions.checkNotNull(text);
        Preconditions.checkNotNull(url);

        final String expandedUrl = httpService.expand(url);
        final String cleanExpandedUrl = httpService.removeUrlParameters(expandedUrl);

        final String textOfTweet = text;
        final String tweet = textOfTweet + " - " + cleanExpandedUrl;
        return tweet;
    }

    // util

    private final List<String> twitterTagsToHash(final String twitterAccount) {
        final String wordsToHashForAccount = twitterHashtagsRetriever.hashtags(twitterAccount);
        final Iterable<String> split = Splitter.on(',').split(wordsToHashForAccount);
        return Lists.newArrayList(split);
    }

    /**
     * Determines if the tweet text contains a link
     */
    private final boolean containsLink(final String text) {
        return text.contains("http://");
    }

    private final boolean isRetweet(final String potentialTweet) {
        return potentialTweet.startsWith("RT @");
    }

}
