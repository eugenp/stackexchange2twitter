package org.tweet.twitter.service;

import java.util.List;

import org.common.text.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tweet.twitter.component.TwitterHashtagsRetriever;
import org.tweet.twitter.util.TwitterUtil;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

@Service
public class TweetService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterHashtagsRetriever twitterHashtagsRetriever;

    public TweetService() {
        super();
    }

    // API

    /**
     * Determines if a tweet is worth retweeting based on the following criteria: 
     * - has link
     * - contains any banned keywords
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

    public final String preValidityProcess(final String title) {
        return TextUtils.preProcessTweetText(title);
    }

    public final String postValidityProcess(final String text, final String twitterAccount) {
        return TwitterUtil.hashtagWords(text, twitterTagsToHash(twitterAccount));
    }

    private final List<String> twitterTagsToHash(final String twitterAccount) {
        final String wordsToHashForAccount = twitterHashtagsRetriever.hashtags(twitterAccount);
        final Iterable<String> split = Splitter.on(',').split(wordsToHashForAccount);
        return Lists.newArrayList(split);
    }

    public boolean isTweetTextValid(final String text) {
        return TwitterUtil.isTweetTextValid(text);
    }

    // util

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
