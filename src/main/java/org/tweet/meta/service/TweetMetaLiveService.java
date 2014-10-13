package org.tweet.meta.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.classification.service.ClassificationService;
import org.common.metrics.MetricsUtil;
import org.common.service.BaseTweetFromSourceLiveService;
import org.common.service.LinkService;
import org.common.service.live.HttpLiveService;
import org.common.util.LinkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;
import org.stackexchange.util.TwitterAccountEnum;
import org.stackexchange.util.TwitterTag;
import org.tweet.meta.component.PredefinedAccountRetriever;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.component.MinRtRetriever;
import org.tweet.twitter.service.TagRetrieverService;
import org.tweet.twitter.service.TweetMentionService;
import org.tweet.twitter.service.TweetType;
import org.tweet.twitter.util.ErrorUtil;
import org.tweet.twitter.util.TweetUtil;
import org.tweet.twitter.util.TwitterInteraction;
import org.tweet.twitter.util.TwitterInteractionWithValue;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.client.util.Preconditions;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

@Service
@Profile(SpringProfileUtil.WRITE)
public class TweetMetaLiveService extends BaseTweetFromSourceLiveService<Retweet> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TagRetrieverService tagRetrieverService;

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private HttpLiveService httpLiveService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private MinRtRetriever minRtRetriever;

    @Autowired
    private PredefinedAccountRetriever predefinedAccountRetriever;

    @Autowired
    private InteractionLiveService interactionLiveService;

    @Autowired
    private TweetMetaLocalService tweetMetaLocalService;

    @Autowired
    private TweetMentionService tweetMentionService;

    // metrics

    @Autowired
    private MetricRegistry metrics;

    public TweetMetaLiveService() {
        super();
    }

    // API

    public final boolean retweetAnyByHashtag(final String twitterAccount) throws JsonProcessingException, IOException {
        return retweetAnyByHashtag(twitterAccount, TweetType.Standard);
    }

    public final boolean retweetAnyByHashtag(final String twitterAccount, final TweetType tweetType) throws JsonProcessingException, IOException {
        String twitterTag = null;
        try {
            twitterTag = tagRetrieverService.pickTwitterTag(twitterAccount);
            final boolean success = retweetAnyByHashtagInternal(twitterAccount, twitterTag, tweetType);
            if (!success) {
                logger.warn("Unable to retweet any tweet on twitterAccount= {}, by twitterTag= {}", twitterAccount, twitterTag);
            }
            return success;
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by twitterTag= " + twitterTag, runtimeEx);
            metrics.counter(MetricsUtil.Meta.RETWEET_ANY_ERROR).inc();
            return false;
        } catch (final Exception ex) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by twitterTag= " + twitterTag, ex);
            metrics.counter(MetricsUtil.Meta.RETWEET_ANY_ERROR).inc();
            return false;
        }
    }

    public final boolean retweetAnyByWord(final String twitterAccount) throws JsonProcessingException, IOException {
        return retweetAnyByWord(twitterAccount, TweetType.Standard);
    }

    public final boolean retweetAnyByWord(final String twitterAccount, final TweetType tweetType) throws JsonProcessingException, IOException {
        String word = null;
        try {
            word = tagRetrieverService.pickTwitterTag(twitterAccount);
            final boolean success = retweetAnyByWordInternal(twitterAccount, word, tweetType);
            if (!success) {
                logger.warn("Unable to retweet any tweet on twitterAccount= {}, by twitterTag= {}", twitterAccount, word);
            }
            return success;
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by word= " + word, runtimeEx);
            metrics.counter(MetricsUtil.Meta.RETWEET_ANY_ERROR).inc();
            return false;
        } catch (final Exception ex) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by word= " + word, ex);
            metrics.counter(MetricsUtil.Meta.RETWEET_ANY_ERROR).inc();
            return false;
        }
    }

    public final boolean retweetAnyByHashtag(final String twitterAccount, final String hashtag) throws JsonProcessingException, IOException {
        return retweetAnyByHashtag(twitterAccount, hashtag, TweetType.Standard);
    }

    public final boolean retweetAnyByHashtag(final String twitterAccount, final String hashtag, final TweetType tweetType) throws JsonProcessingException, IOException {
        try {
            final boolean success = retweetAnyByHashtagInternal(twitterAccount, hashtag, tweetType);
            if (!success) {
                logger.warn("Unable to retweet any tweet on twitterAccount= {}, by twitterTag= {}", twitterAccount, hashtag);
            }
            return success;
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by twitterTag= " + hashtag, runtimeEx);
            metrics.counter(MetricsUtil.Meta.RETWEET_ANY_ERROR).inc();
            return false;
        } catch (final Exception ex) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by twitterTag= " + hashtag, ex);
            metrics.counter(MetricsUtil.Meta.RETWEET_ANY_ERROR).inc();
            return false;
        }
    }

    public final boolean retweetAnyByWord(final String twitterAccount, final String word) throws JsonProcessingException, IOException {
        return retweetAnyByWord(twitterAccount, word, TweetType.Standard);
    }

    public final boolean retweetAnyByWord(final String twitterAccount, final String word, final TweetType tweetType) throws JsonProcessingException, IOException {
        try {
            final boolean success = retweetAnyByWordInternal(twitterAccount, word, tweetType);
            if (!success) {
                logger.warn("Unable to retweet any tweet on twitterAccount= {}, by word= {}", twitterAccount, word);
            }
            return success;
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by word= " + word, runtimeEx);
            metrics.counter(MetricsUtil.Meta.RETWEET_ANY_ERROR).inc();
            return false;
        } catch (final Exception ex) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by word= " + word, ex);
            metrics.counter(MetricsUtil.Meta.RETWEET_ANY_ERROR).inc();
            return false;
        }
    }

    // util - any

    /**any*/
    private final boolean retweetAnyByWordInternal(final String twitterAccount, final String word, final TweetType tweetType) throws JsonProcessingException, IOException {
        logger.info("Begin trying to retweet on twitterAccount= {}, by word= {}", twitterAccount, word);

        final List<Tweet> tweetsOfHashtag = twitterReadLiveService.listTweetsByWord(twitterAccount, word);

        final Collection<Tweet> prunedTweetsLocal = pruneTweetsLocal(tweetsOfHashtag, word, twitterAccount);
        final Collection<Tweet> prunedTweets = pruneTweets(prunedTweetsLocal, word, twitterAccount, tweetType);

        return retweetAnyByWordInternal(twitterAccount, prunedTweets, word, tweetType);
    }

    /**any*/
    private final boolean retweetAnyByHashtagInternal(final String twitterAccount, final String hashtag, final TweetType tweetType) throws JsonProcessingException, IOException {
        logger.info("Begin trying to retweet on twitterAccount= {}, by hashtag= {}", twitterAccount, hashtag);

        final List<Tweet> tweetsOfHashtag = twitterReadLiveService.listTweetsByHashtag(twitterAccount, hashtag);

        final Collection<Tweet> prunedTweets = pruneTweets(tweetsOfHashtag, hashtag, twitterAccount, tweetType);

        return retweetAnyByHashtagInternal(twitterAccount, prunedTweets, hashtag, tweetType);
    }

    /**
     * Verifies that the tweets contain the actual word - sometimes twitter matches on other weird things and this basically eliminates these invalid results <br/>
     * Note: this is only done when retweeting by word
     */
    private final Collection<Tweet> pruneTweetsLocal(final Collection<Tweet> tweets, final String word, final String twitterAccount) {
        final Collection<Tweet> filtered = Collections2.filter(tweets, new TweetContainsWordPredicate(word));
        return filtered;
    }

    private final Collection<Tweet> pruneTweets(final Collection<Tweet> tweetsOfHashtag, final String hashtag, final String twitterAccount, final TweetType tweetType) {
        final Set<Tweet> tweetsSet = Sets.newHashSet();
        for (final Tweet tweet : tweetsOfHashtag) {
            tweetsSet.add(TweetUtil.getTweet(tweet));
        }

        final int minRt = minRtRetriever.minRt(hashtag);
        final Collection<Tweet> tweetSetFiltered = Collections2.filter(tweetsSet, new Predicate<Tweet>() {
            @Override
            public final boolean apply(final Tweet tweet) {
                if (tweet.getRetweetCount() <= (minRt / 1.75)) {
                    return false;
                }
                if (hasThisAlreadyBeenTweetedById(new Retweet(tweet.getId(), twitterAccount, null, null))) {
                    return false;
                }
                if (!tweetService.isTweetWorthRetweetingByTextWithLink(tweet.getText(), tweetType)) {
                    return false;
                }
                if (!tweetService.passesSet1OfChecks(tweet, hashtag)) {
                    return false;
                }
                if (!tweetService.passesLanguageChecksForTweeting(tweet, hashtag)) {
                    return false;
                }
                return true;
            }
        });

        final List<Tweet> tweets = Lists.newArrayList(tweetSetFiltered);
        Collections.sort(tweets, Ordering.from(new TweetByRtComparator()));
        if (tweets.size() > 14) {
            final TwitterTag theTag = TwitterTag.valueOf(hashtag);
            if (theTag == null || theTag.isGenerateLogs()) {
                logger.error("To many - after pruning, still {} results for hashtag= {}", tweets.size(), hashtag);
            }
        }
        if (tweets.size() < 6) {
            if (minRt > 1) {
                final TwitterTag theTag = TwitterTag.valueOf(hashtag);
                if (theTag == null || theTag.isGenerateLogs()) {
                    logger.error("To few - after pruning, still {} results for hashtag= {}", tweets.size(), hashtag);
                }
            }
        }

        //
        final List<Pair<TwitterInteractionWithValue, Tweet>> valuesAndTweets = Lists.newArrayList();
        for (final Tweet tweet : tweets) {
            final TwitterInteractionWithValue interactionValue = interactionLiveService.determineBestInteraction(tweet, twitterAccount);
            valuesAndTweets.add(new ImmutablePair<TwitterInteractionWithValue, Tweet>(interactionValue, tweet));
        }
        Collections.sort(valuesAndTweets, new Comparator<Pair<TwitterInteractionWithValue, Tweet>>() {
            @Override
            public final int compare(final Pair<TwitterInteractionWithValue, Tweet> o1, final Pair<TwitterInteractionWithValue, Tweet> o2) {
                return Double.compare(o2.getLeft().getVal(), o1.getLeft().getVal());
            }
        });

        // slightly adjust the RT counts by adding some fraction of the overall value
        // rule of thumb: 15 should cover minRt/2 =>
        for (final Pair<TwitterInteractionWithValue, Tweet> interactionAndTweet : valuesAndTweets) {
            final Tweet theTweet = interactionAndTweet.getValue();
            final int newRtCount = (int) (theTweet.getRetweetCount() + interactionAndTweet.getKey().getVal() / (15f * 2f / minRt));
            theTweet.setRetweetCount(newRtCount);
        }

        final List<Tweet> tweetsOrdered = Lists.transform(valuesAndTweets, new Function<Pair<TwitterInteractionWithValue, Tweet>, Tweet>() {
            @Override
            public final Tweet apply(final Pair<TwitterInteractionWithValue, Tweet> input) {
                return input.getRight();
            }
        });

        return tweetsOrdered;
    }

    /**any*/
    private final boolean retweetAnyByHashtagInternal(final String twitterAccount, final Collection<Tweet> potentialTweetsSorted, final String hashtag, final TweetType tweetType) throws IOException, JsonProcessingException {
        if (!TwitterAccountEnum.valueOf(twitterAccount).isRt()) {
            logger.error("Should not retweet on twitterAccount= {}", twitterAccount);
        }
        for (final Tweet potentialTweetUnprocessed : potentialTweetsSorted) {
            final Tweet potentialTweet = TweetUtil.getTweet(potentialTweetUnprocessed);
            final long tweetId = potentialTweet.getId();
            logger.trace("Considering to retweet on twitterAccount= {}, from hashtag= {}, tweetId= {}", twitterAccount, hashtag, tweetId);
            if (!hasThisAlreadyBeenTweetedById(new Retweet(tweetId, twitterAccount, null, null))) {
                logger.debug("Attempting to tweet on twitterAccount= {}, from hashtag= {}, tweetId= {}", twitterAccount, hashtag, tweetId);
                final boolean success = tryTweetOneWrap(potentialTweet, hashtag, twitterAccount, tweetType);
                if (!success) {
                    logger.trace("Didn't retweet on twitterAccount= {}, from hashtag= {}, tweet text= {}", twitterAccount, hashtag, TweetUtil.getText(potentialTweet));
                    continue;
                } else {
                    final String tweetUrl = "https://twitter.com/" + potentialTweet.getFromUser() + "/status/" + potentialTweet.getId();
                    logger.info("Successfully retweeted on twitterAccount= {}, from hashtag= {}, tweet text= {}\n --- Additional meta info: tweet url= {}", twitterAccount, hashtag, TweetUtil.getText(potentialTweet), tweetUrl);
                    return true;
                }
            }
        }

        return false;
    }

    /**any*/
    private final boolean retweetAnyByWordInternal(final String twitterAccount, final Collection<Tweet> potentialTweetsSorted, final String word, final TweetType tweetType) throws IOException, JsonProcessingException {
        if (!TwitterAccountEnum.valueOf(twitterAccount).isRt()) {
            logger.error("Should not retweet on twitterAccount= {}", twitterAccount);
        }
        for (final Tweet potentialTweetUnprocessed : potentialTweetsSorted) {
            final Tweet potentialTweet = TweetUtil.getTweet(potentialTweetUnprocessed);
            final long tweetId = potentialTweet.getId();
            logger.trace("Considering to retweet on twitterAccount= {}, from word= {}, tweetId= {}", twitterAccount, word, tweetId);
            if (!hasThisAlreadyBeenTweetedById(new Retweet(tweetId, twitterAccount, null, null))) {
                logger.debug("Attempting to tweet on twitterAccount= {}, from word= {}, tweetId= {}", twitterAccount, word, tweetId);
                final boolean success = tryTweetOneWrap(potentialTweet, word, twitterAccount, tweetType);
                if (!success) {
                    logger.trace("Didn't retweet on twitterAccount= {}, from word= {}, tweet text= {}", twitterAccount, word, TweetUtil.getText(potentialTweet));
                    continue;
                } else {
                    final String tweetUrl = "https://twitter.com/" + potentialTweet.getFromUser() + "/status/" + potentialTweet.getId();
                    logger.info("Successfully retweeted on twitterAccount= {}, from word= {}, tweet text= {}\n --- Additional meta info: tweet url= {}", twitterAccount, word, TweetUtil.getText(potentialTweet), tweetUrl);
                    return true;
                }
            }
        }

        return false;
    }

    // util - one

    /**one*/
    private final boolean tryTweetOneWrap(final Tweet potentialTweet, final String hashtag, final String twitterAccount, final TweetType tweetType) {
        try {
            return tryTweetOnePrepare(potentialTweet, hashtag, twitterAccount, tweetType);
        } catch (final RuntimeException runEx) {
            // this is new - the point it to recover, log and keep analyzing
            logger.error("Unexpected exception trying to tweet on twitterAccount= " + twitterAccount + ", tweetText= " + potentialTweet.getText(), runEx);
            metrics.counter(MetricsUtil.Meta.RETWEET_ONE_ERROR).inc();
            return false;
        }
    }

    /**one*/
    private final boolean tryTweetOnePrepare(final Tweet potentialTweet, final String hashtag, final String twitterAccount, final TweetType tweetType) {
        Preconditions.checkState(potentialTweet.getRetweetedStatus() == null, "By the `one` level - this should be the original tweet");
        final String text = potentialTweet.getText();
        final long tweetId = potentialTweet.getId();
        logger.trace("Considering to retweet on twitterAccount= {}, tweetId= {}, tweetText= {}", twitterAccount, tweetId, text);

        final Map<String, Object> customDetails = Maps.newHashMap();
        customDetails.put("tweetId", tweetId);
        customDetails.put("hashtag", hashtag);
        customDetails.put("potentialTweet", potentialTweet);
        customDetails.put("tweetType", tweetType);

        return tryTweetOne(text, null, twitterAccount, customDetails);
    }

    /**one*/
    @Override
    protected final boolean tryTweetOne(final String fullTweet, final String noUrl, final String twitterAccount, final Map<String, Object> customDetails) {
        final long tweetId = (long) customDetails.get("tweetId");
        final String hashtag = (String) customDetails.get("hashtag");
        final Tweet potentialTweet = (Tweet) customDetails.get("potentialTweet");
        final TweetType tweetType = (TweetType) customDetails.get("tweetType");

        logger.trace("Considering to retweet on twitterAccount= {}, tweetId= {}, tweetText= {}", twitterAccount, tweetId, fullTweet);

        // is it worth it by text only?
        if (!tweetService.isTweetWorthRetweetingByTextWithLink(fullTweet)) {
            logger.debug("Tweet not worth retweeting (by text only) on twitterAccount= {}, tweet text= {}", twitterAccount, fullTweet);
            return false;
        }

        // is it worth it by full tweet?
        if (!advancedTweetService.isTweetWorthRetweetingByRawTweet(potentialTweet, hashtag)) {
            logger.debug("Tweet not worth retweeting (by full tweet) on twitterAccount= {}, tweet text= {}", twitterAccount, fullTweet);
            return false;
        }

        // pre-validity processing
        final String fullTweetProcessedPreValidity = tweetService.processPreValidity(fullTweet);
        // is it valid?
        if (!tweetService.isTweetFullValid(fullTweetProcessedPreValidity)) {
            final String tweetUrl = "https://twitter.com/" + potentialTweet.getFromUser() + "/status/" + potentialTweet.getId();
            logger.debug("Tweet invalid (size, link count) on twitterAccount= {}, tweet text= {}\n- url={}", twitterAccount, fullTweetProcessedPreValidity, tweetUrl);
            return false;
        }
        // post-validity processing
        final String fullTweetProcessed = advancedTweetService.postValidityProcessTweetTextWithUrl(fullTweetProcessedPreValidity, twitterAccount);

        // after text processing, check again if this has already been retweeted
        final Retweet alreadyExistingRetweetByText = hasThisAlreadyBeenTweetedByText(fullTweetProcessed, twitterAccount);
        if (alreadyExistingRetweetByText != null) {
            // was warn, but an already existing tweet is likely OK so - debug
            logger.debug("Tweet with retweet mention already exists:\n-original tweet= {}\n-new tweet (not retweeted)= {}", alreadyExistingRetweetByText.getText(), fullTweetProcessed);
            return false;
        }

        // is this tweet pointing to something good?
        if (tweetType == TweetType.Standard) {
            if (!isTweetPointingToSomethingGoodTechnical(fullTweetProcessed)) {
                logger.debug("Tweet not pointing to something good on twitterAccount= {}, tweet text= {}", twitterAccount, fullTweetProcessed);
                return false;
            }
        } else if (tweetType == TweetType.NonTech) {
            if (!isTweetPointingToSomethingGoodNonTechnical(fullTweetProcessed)) {
                logger.debug("Tweet not pointing to something good on twitterAccount= {}, tweet text= {}", twitterAccount, fullTweetProcessed);
                return false;
            }
        } else {
            throw new UnsupportedOperationException();
        }

        // is the tweet rejected by some classifier?
        if (isTweetRejectedByClassifier(fullTweetProcessed)) {
            ErrorUtil.registerError(ErrorUtil.rejectedByClassifierJob, fullTweetProcessed);
            // logger.error("Tweet rejected by a classifier on twitterAccount= " + twitterAccount + "\n--tweet text= \n" + fullTweetProcessed);
            return false;
        }

        boolean success = false;
        final TwitterInteraction bestInteraction = interactionLiveService.determineBestInteraction(potentialTweet, twitterAccount).getTwitterInteraction();
        switch (bestInteraction) {
        case None:
            success = twitterWriteLiveService.tweet(twitterAccount, fullTweetProcessed, potentialTweet);
            break;
        case Mention:
            // TODO: add mention
            final String fullTweetProcessedWithMention = tweetMentionService.addMention(potentialTweet.getFromUser(), fullTweetProcessed);
            success = twitterWriteLiveService.tweet(twitterAccount, fullTweetProcessedWithMention, potentialTweet);
            break;
        case Retweet:
            success = twitterWriteLiveService.retweet(twitterAccount, tweetId);
            break;
        default:
            logger.error("Invalid State");
            break;
        }

        // mark
        if (success) {
            markDone(new Retweet(tweetId, twitterAccount, fullTweetProcessed, new Date()));
        }

        // done
        return success;
    }

    /**one*/
    private final boolean isTweetRejectedByClassifier(final String text) {
        if (classificationService.isJobDefault(text)) {
            return true; // temporarily - to see how it works
            // return false; // temporarily, until there is more classification training data for jobs-nonjobs
        }
        return false;
    }

    /**one*/
    /**
     * Tweet is <b>not</b> pointing to something good if: <br/>
     * - it has <b>no url</b><br/>
     * - it points to a <b>homepage</b><br/>
     * - it points to a <b>banned domain</b><br/>
     */
    final boolean isTweetPointingToSomethingGoodTechnical(final String potentialTweet) {
        final Set<String> extractedUrls = linkService.extractUrls(potentialTweet);
        if (extractedUrls.isEmpty()) {
            logger.trace("Tweet rejected because the it contains no urls\n- potentialTweet= {} ", potentialTweet);
            return false;
        }
        String singleMainUrl = linkService.determineMainUrl(extractedUrls);
        try {
            singleMainUrl = httpLiveService.expand(singleMainUrl);
        } catch (final RuntimeException ex) {
            logger.error("Unexpected error from URL expansion: " + singleMainUrl, ex);
            return false;
        }
        if (singleMainUrl == null) {
            logger.trace("Tweet rejected because the main url couldn't be identified\n- potentialTweet= {} ", potentialTweet);
            return false;
        }

        if (linkService.isHomepageUrl(singleMainUrl)) {
            // note: this will experience high bursts when a popular tweet is returned (and analyzed) 20-30 times at once (do not set on error)
            logger.trace("Tweet rejected because the it is pointing to a homepage= {}\n- potentialTweet= {} ", singleMainUrl, potentialTweet);
            return false;
        }

        if (LinkUtil.belongsToBannedDomainTechnical(singleMainUrl)) {
            logger.debug("potentialTweet= {} rejected because the it is pointing to a banned domain= {}", potentialTweet, singleMainUrl);
            return false;
        }

        return true;
    }

    /**
     * Tweet is <b>not</b> pointing to something good if: <br/>
     * - it has <b>no url</b><br/>
     * - it points to a <b>homepage</b><br/>
     * - it points to a <b>banned domain</b><br/>
     */
    final boolean isTweetPointingToSomethingGoodNonTechnical(final String potentialTweet) {
        final Set<String> extractedUrls = linkService.extractUrls(potentialTweet);
        if (extractedUrls.isEmpty()) {
            logger.trace("Tweet rejected because the it contains no urls\n- potentialTweet= {} ", potentialTweet);
            return false;
        }
        String singleMainUrl = linkService.determineMainUrl(extractedUrls);
        try {
            singleMainUrl = httpLiveService.expand(singleMainUrl);
        } catch (final RuntimeException ex) {
            logger.error("Unexpected error from URL expansion: " + singleMainUrl, ex);
            return false;
        }
        if (singleMainUrl == null) {
            logger.trace("Tweet rejected because the main url couldn't be identified\n- potentialTweet= {} ", potentialTweet);
            return false;
        }

        if (linkService.isHomepageUrl(singleMainUrl)) {
            // note: this will experience high bursts when a popular tweet is returned (and analyzed) 20-30 times at once (do not set on error)
            logger.trace("Tweet rejected because the it is pointing to a homepage= {}\n- potentialTweet= {} ", singleMainUrl, potentialTweet);
            return false;
        }

        if (LinkUtil.belongsToBannedDomainCommon(singleMainUrl)) {
            logger.debug("potentialTweet= {} rejected because the it is pointing to a banned domain= {}", potentialTweet, singleMainUrl);
            return false;
        }

        return true;
    }

    /**one*/
    @Override
    protected final boolean hasThisAlreadyBeenTweetedById(final Retweet retweet) {
        return tweetMetaLocalService.hasThisAlreadyBeenTweetedById(retweet);
    }

    /**one*/
    protected final Retweet hasThisAlreadyBeenTweetedByText(final String text, final String twitterAccount) {
        return tweetMetaLocalService.findLocalCandidateAdvanced(text, twitterAccount);
    }

    /**one*/
    @Override
    protected final void markDone(final Retweet entity) {
        tweetMetaLocalService.markDone(entity);
    }

    // helpers

    @Override
    protected final IRetweetJpaDAO getApi() {
        throw new UnsupportedOperationException();
    }

}
