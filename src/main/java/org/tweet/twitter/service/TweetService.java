package org.tweet.twitter.service;

import java.util.ArrayList;
import java.util.List;

import org.common.service.HttpLiveService;
import org.common.service.LinkService;
import org.common.util.TextUtils;
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
    private HttpLiveService httpService;

    @Autowired
    private LinkService linkService;

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
    public final boolean isTweetWorthRetweetingByText(final String potentialTweetText) {
        if (!containsLink(potentialTweetText)) {
            return false;
        }
        if (TwitterUtil.isTweetBanned(potentialTweetText)) {
            return false;
        }
        if (isRetweet(potentialTweetText)) {
            // TODO: error temporarily to get results back about this category and improve it: https://github.com/eugenp/stackexchange2twitter/issues/33
            // logger.error("Tweet that was already a retweet: " + potentialTweetText);
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
        if (!containsLink(potentialTweetText)) {
            return false;
        }
        if (containsLinkToBannedServices(potentialTweetText)) {
            return false;
        }
        if (TwitterUtil.isTweetBanned(potentialTweetText)) {
            return false;
        }
        if (isRetweet(potentialTweetText)) {
            // TODO: error temporarily to get results back about this category and improve it: https://github.com/eugenp/stackexchange2twitter/issues/33
            // logger.error("Tweet that was already a retweet: " + potentialTweetText);
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
        String tweetTextProcessed = TwitterUtil.hashtagWords(tweetText, twitterTagsToHash(twitterAccount));
        if (tweetTextProcessed.startsWith("\"") && tweetTextProcessed.endsWith("\"")) {
            tweetTextProcessed = tweetTextProcessed.substring(1, tweetTextProcessed.length() - 1);
        }

        return tweetTextProcessed;
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

        final String expandedUrl = Preconditions.checkNotNull(httpService.expand(url));
        final String cleanExpandedUrl = linkService.removeUrlParameters(expandedUrl);

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
    final boolean containsLink(final String text) {
        return text.contains("http://") || text.contains("https://");
    }

    private final boolean containsLinkToBannedServices(final String tweetText) {
        final ArrayList<String> bannedServices = Lists.newArrayList("http://instagram.com/", "pic.twitter.com");

        for (final String bannedService : bannedServices) {
            final boolean linkToBannedService = tweetText.contains(bannedService);
            if (linkToBannedService) {
                logger.trace("Tweet = {} contains link to banned service= {} - skipping", tweetText, bannedService);
                return true;
            }
        }

        return false;
    }

    final boolean isRetweet(final String potentialTweet) {
        final boolean startsWith = potentialTweet.startsWith("RT @");
        if (startsWith) {
            return true;
        }
        final boolean contains = potentialTweet.contains("RT @");
        if (contains) {
            return true;
        }

        final boolean containsRtInfo = potentialTweet.matches(".*RT.{0,3}@.*");
        if (containsRtInfo) {
            return true;
        }

        return false;
    }

}
