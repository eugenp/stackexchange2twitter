package org.tweet.twitter.service;

import java.util.ArrayList;
import java.util.List;

import org.common.service.LinkService;
import org.common.util.TextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Entities;
import org.springframework.social.twitter.api.HashTagEntity;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;
import org.tweet.twitter.component.MinRtRetriever;
import org.tweet.twitter.component.TwitterHashtagsRetriever;
import org.tweet.twitter.util.HashtagWordFunction;
import org.tweet.twitter.util.TweetUtil;
import org.tweet.twitter.util.TwitterUtil;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
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

    // checks

    /**
     * Determines if a tweet is worth tweeting based on only its text; by the following <b>criteria</b>: <br/>
     * - has link <br/>
     * - is not banned (mostly by keywords, expressions and regexes) <br/>
     * - does not contain link to banned services<br/>
     * - it does not contain more than one link
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

        if (linkService.extractUrls(potentialTweetText).size() > 1) {
            // keep below error - there are a lot of tweets that fall into this category and it's not really relevant
            logger.debug("Rejecting tweet because it has more than one link; tweet text= {}", potentialTweetText);
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

        if (potentialTweet.getLanguageCode() == null) {
            // temporary error
            logger.error("potentialTweet= {} on twitterTag= {} rejected because it has the no language", potentialTweet, twitterTag);
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

        final boolean shouldByNumberOfHashtags = isTweetWorthRetweetingByNumberOfHashtags(potentialTweet);
        if (!shouldByNumberOfHashtags) {
            logger.error("potentialTweet= {} on twitterTag= {} rejected because the it contained to many hashtags", TweetUtil.getText(potentialTweet), twitterTag);
            // error temporary - debug or trace
            return false;
        }

        return true;
    }

    /**
     * Verifies that: <br/>
     * - the text has <b>no link</b> <br/>
     * - the text has the <b>correct length</b> <br/>
     */
    public final boolean isTweetTextValid(final String tweetTextNoUrl) {
        return TwitterUtil.isTweetTextWithoutLinkValid(tweetTextNoUrl);
    }

    /**
     * Verifies that: <br/>
     * - the text has the <b>correct length</b> <br/>
     */
    public final boolean isTweetFullValid(final String tweetTextWithUrl) {
        if (!TwitterUtil.isTweetTextWithLinkValid(tweetTextWithUrl)) {
            return false;
        }

        if (tweetTextWithUrl.matches(".*&\\S*;.*")) {
            // after cleanup, still contains unclean characters - fail validation
            logger.error("Probably unclean characters in: {}", tweetTextWithUrl);
            return false;
        }

        return true;
    }

    // processing

    /**
     * - cleans the invalid characters from text
     */
    public final String processPreValidity(final String title) {
        String resultAfterCleanup = TextUtil.cleanupInvalidCharacters(title);
        resultAfterCleanup = TextUtil.trimTweet(resultAfterCleanup);

        return resultAfterCleanup;
    }

    /**
     * - adds hashtags, trims <br/>
     * - <b>note</b>: the text should be the full tweet (including url) <br/>
     */
    public final String postValidityProcessForFullTweet(final String fullTweet, final String twitterAccount) {
        String tweetTextProcessed = hashtagWordsFullTweet(fullTweet, twitterTagsToHash(twitterAccount));
        if (tweetTextProcessed.startsWith("\"") && tweetTextProcessed.endsWith("\"")) {
            logger.error("It's happening; original text= {}", tweetTextProcessed);
            tweetTextProcessed = tweetTextProcessed.substring(1, tweetTextProcessed.length() - 1);
            logger.error("It has happened; original text= {}", tweetTextProcessed);
        }

        return tweetTextProcessed;
    }

    /**
     * - adds hashtags, trims <br/>
     * - <b>note</b>: the text should be the tweet text only (no url) <br/>
     */
    public final String postValidityProcessForTweetTextOnly(final String textOnly, final String twitterAccount) {
        String tweetTextProcessed = hashtagWordsTweetTextOnly(textOnly, twitterTagsToHash(twitterAccount));
        if (tweetTextProcessed.startsWith("\"") && tweetTextProcessed.endsWith("\"")) {
            logger.error("It's happening; original text= {}", tweetTextProcessed);
            tweetTextProcessed = tweetTextProcessed.substring(1, tweetTextProcessed.length() - 1);
            logger.error("It has happened; original text= {}", tweetTextProcessed);
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

    public final int countHashtags(final Tweet tweet) {
        return getHashtags(tweet).size();
    }

    public final int getCharacterLenghtOfHashTags(final Tweet tweet) {
        int size = 0;
        for (final HashTagEntity hashTag : getHashtags(tweet)) {
            size += hashTag.getText().length();
            size += 2;
        }

        return size;
    }

    // util

    final boolean isTweetWorthRetweetingByNumberOfHashtags(final Tweet tweet) {
        final int countHashtags = countHashtags(tweet);
        if (countHashtags > 5) {
            return false;
        }

        return true;
    }

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

    final String hashtagWordsTweetTextOnly(final String tweetTextOnly, final List<String> wordsToHash) {
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

    final List<HashTagEntity> getHashtags(final Tweet tweet) {
        final Entities entities = tweet.getEntities();
        if (entities == null) {
            return Lists.newArrayList();
        }
        final List<HashTagEntity> hashTags = entities.getHashTags();
        if (hashTags == null) {
            return Lists.newArrayList();
        }

        return hashTags;
    }

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
