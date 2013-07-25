package org.tweet.twitter.service;

import java.util.ArrayList;
import java.util.List;

import org.common.service.LinkService;
import org.common.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;
import org.tweet.twitter.component.MinRtRetriever;
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
    private LinkService linkService;

    @Autowired
    private MinRtRetriever minRtRetriever;

    public TweetService() {
        super();
    }

    // API

    /**
     * Determines if a tweet is worth tweeting based on only its text; by the following <b>criteria</b>: <br/>
     * - has link <br/>
     * - is not banned (mostly by keywords, expressions and regexes) <br/>
     * - does not contain link to banned services<br/>
     */
    public final boolean isTweetWorthRetweetingByText(final String potentialTweetText) {
        if (!containsLink(potentialTweetText)) {
            return false;
        }
        if (TwitterUtil.isTweetBanned(potentialTweetText)) {
            return false;
        }
        if (containsLinkToBannedServices(potentialTweetText)) {
            return false;
        }

        // is retweet check moved from here to isTweetWorthRetweetingByFullTweet
        return true;
    }

    /**
     * Determines if a tweet is worth retweeting based on the following criteria: <br/>
     * - <b>number of retweets</b> over a certain threshold (the threshold is per hashtag) <br/>
     * - is in <b>English</b> <br/>
     * - the <b>user</b> is not banned for retweeting <br/>
     * - (this is changing) is not already a retweet <br/>
     * 
     * - favorites are not yet considered <br/>
     */
    /*meta*/public final boolean isTweetWorthRetweetingByFullTweet(final Tweet potentialTweet, final String twitterTag) {
        final int requiredMinRts = minRtRetriever.minRt(twitterTag);
        if (potentialTweet.getRetweetCount() < requiredMinRts) {
            logger.trace("potentialTweet= {} on twitterTag= {} rejected because it only has= {} retweets and it needs= {}", potentialTweet, twitterTag, potentialTweet.getRetweetCount(), requiredMinRts);
            return false;
        }

        if (!potentialTweet.getLanguageCode().equals("en")) {
            logger.info("potentialTweet= {} on twitterTag= {} rejected because it has the language= {}", potentialTweet, twitterTag, potentialTweet.getLanguageCode());
            // info temporary - should be debug
            return false;
        }

        if (TwitterUtil.isUserBannedFromRetweeting(potentialTweet.getFromUser())) {
            logger.debug("potentialTweet= {} on twitterTag= {} rejected because the original user is banned= {}", potentialTweet, twitterTag, potentialTweet.getFromUser());
            // debug temporary - should be trace
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
        return TwitterUtil.isTweetTextWithoutLinkValid(tweetTextNoUrl);
    }

    public final String constructTweetSimple(final String tweetTextNoUrl, final String url) {
        Preconditions.checkNotNull(tweetTextNoUrl);
        Preconditions.checkNotNull(url);

        final String textOfTweet = tweetTextNoUrl;
        final String tweet = textOfTweet + " - " + url;
        return tweet;
    }

    /**
     * - example: <i>RT @someuser: Original Tweet Stuff</i>
     */
    public final boolean isRetweetMention(final String potentialTweet) {
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
        return text.contains("http://") || text.contains("https://");
    }

    /**
     * - current banned services: instagram, pic.twitter
     */
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

}
