package org.tweet.meta.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.classification.service.ClassificationService;
import org.common.service.BaseTweetFromSourceService;
import org.common.service.HttpService;
import org.common.util.HttpUtils;
import org.common.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.twitter.component.MinRtRetriever;
import org.tweet.twitter.service.TagRetrieverService;
import org.tweet.twitter.util.TwitterUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Maps;
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
    private MinRtRetriever minRtRetriever;

    @Autowired
    private RetweetStrategy retweetStrategy;

    public TweetMetaService() {
        super();
    }

    // API

    public final boolean retweetByHashtag(final String twitterAccount) throws JsonProcessingException, IOException {
        String twitterTag = null;
        try {
            twitterTag = tagService.pickTwitterTag(twitterAccount);
            final boolean success = retweetByHashtagInternal(twitterAccount, twitterTag);
            if (!success) {
                logger.warn("Unable to retweet any tweet on twitterAccount= {}, by twitterTag= {}", twitterAccount, twitterTag);
            }
            return success;
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by twitterTag= " + twitterTag, runtimeEx);
            return false;
        } catch (final Exception ex) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by twitterTag= " + twitterTag, ex);
            return false;
        }
    }

    public final boolean retweetByHashtag(final String twitterAccount, final String twitterTag) throws JsonProcessingException, IOException {
        try {
            final boolean success = retweetByHashtagInternal(twitterAccount, twitterTag);
            if (!success) {
                logger.warn("Unable to retweet any tweet on twitterAccount= {}, by twitterTag= {}", twitterAccount, twitterTag);
            }
            return success;
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by twitterTag= " + twitterTag, runtimeEx);
            return false;
        } catch (final Exception ex) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by twitterTag= " + twitterTag, ex);
            return false;
        }
    }

    // template

    @Override
    protected final boolean tryTweetOne(final String text, final String url, final String twitterAccount, final Map<String, Object> customDetails) {
        final long tweetId = (long) customDetails.get("tweetId");
        final String hashtag = (String) customDetails.get("hashtag");
        final Tweet potentialTweet = (Tweet) customDetails.get("potentialTweet");

        logger.trace("Considering to retweet on twitterAccount= {}, tweetId= {}, tweetText= {}", twitterAccount, tweetId, text);

        // is it worth it by itself?
        if (!tweetService.isTweetWorthRetweetingByText(text)) {
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
            logger.error("Tweet rejected by a classifier on twitterAccount= {}, tweet text= {}", twitterAccount, tweetText);
            return false;
        }

        // post-process
        final String processedTweetText = tweetService.postValidityProcess(tweetText, twitterAccount);

        // tweet
        if (retweetStrategy.shouldRetweetRandomized(potentialTweet)) {
            twitterLiveService.retweet(twitterAccount, tweetId);
        } else {
            twitterLiveService.tweet(twitterAccount, processedTweetText);
        }

        // mark
        markDone(new Retweet(tweetId, twitterAccount));

        // done
        return true;
    }

    @Override
    protected final boolean hasThisAlreadyBeenTweeted(final Retweet retweet) {
        final Retweet existingTweet = retweetApi.findOneByTweetIdAndTwitterAccount(retweet.getTweetId(), retweet.getTwitterAccount());
        return existingTweet != null;
    }

    @Override
    protected final void markDone(final Retweet entity) {
        retweetApi.save(entity);
    }

    @Override
    protected final IRetweetJpaDAO getApi() {
        return retweetApi;
    }

    // util

    private final boolean retweetByHashtagInternal(final String twitterAccount, final String hashtag) throws JsonProcessingException, IOException {
        logger.info("Begin trying to retweet on twitterAccount= {}, by hashtag= {}", twitterAccount, hashtag);

        logger.trace("Trying to retweet on twitterAccount= {}", twitterAccount);
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
            logger.trace("Considering to retweet on twitterAccount= {}, from hashtag= {}, tweetId= {}", twitterAccount, hashtag, tweetId);
            if (!hasThisAlreadyBeenTweeted(new Retweet(tweetId, twitterAccount))) {
                logger.debug("Attempting to tweet on twitterAccount= {}, from hashtag= {}, tweetId= {}", twitterAccount, hashtag, tweetId);
                final boolean success = tryTweetOneDelegator(potentialTweet, hashtag, twitterAccount);
                if (!success) {
                    logger.trace("Didn't retweet on twitterAccount= {}, from hashtag= {}, tweet text= {}", twitterAccount, hashtag, potentialTweet.getText());
                    continue;
                } else {
                    final String tweetUrl = "https://twitter.com/" + potentialTweet.getFromUser() + "/status/" + potentialTweet.getId();
                    logger.info("Successfully retweeted on twitterAccount= {}, from hashtag= {}, tweet text= {}\n --- Additional meta info: tweet url= {}", twitterAccount, hashtag, potentialTweet.getText(), tweetUrl);
                    return true;
                }
            }
        }

        return false;
    }

    private final boolean tryTweetOneDelegator(final Tweet potentialTweet, final String hashtag, final String twitterAccount) {
        final String text = potentialTweet.getText();
        final long tweetId = potentialTweet.getId();
        logger.trace("Considering to retweet on twitterAccount= {}, tweetId= {}, tweetText= {}", twitterAccount, tweetId, text);

        final Map<String, Object> customDetails = Maps.newHashMap();
        customDetails.put("tweetId", tweetId);
        customDetails.put("hashtag", hashtag);
        customDetails.put("potentialTweet", potentialTweet);

        return tryTweetOne(text, null, twitterAccount, customDetails);
    }

    // checks

    private final boolean isTweetRejectedByClassifier(final String text) {
        if (classificationService.isCommercialDefault(text)) {
            return true; // temporarily - to see how it works
            // return false; // temporarily, until there is more classification training data for commercial-noncommercial
        }
        return false;
    }

    private final boolean isTweetPointingToSomethingGood(final String potentialTweet) {
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
            logger.debug("potentialTweet= {} rejected because the it is pointing to a homepage= {}", potentialTweet, singleMainUrl);
            return false;
        }

        if (HttpUtils.belongsToBannedDomain(singleMainUrl)) {
            logger.debug("potentialTweet= {} rejected because the it is pointing to a banned domain= {}", potentialTweet, singleMainUrl);
            return false;
        }

        return true;
    }

    /**
     * Determines if a tweet is worth retweeting based on the following criteria: 
     * - number of retweets over a certain threshold (the threshold is per hashtag)
     * - number of favorites (not yet)
     */
    private final boolean isTweetWorthRetweetingInContext(final Tweet potentialTweet, final String twitterTag) {
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

}
