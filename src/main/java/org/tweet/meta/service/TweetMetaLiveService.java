package org.tweet.meta.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.classification.service.ClassificationService;
import org.common.metrics.MetricsUtil;
import org.common.service.BaseTweetFromSourceLiveService;
import org.common.service.LinkService;
import org.common.service.live.HttpLiveService;
import org.common.util.LinkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.component.PredefinedAccountRetriever;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.component.MinRtRetriever;
import org.tweet.twitter.service.TagRetrieverService;
import org.tweet.twitter.util.TwitterUtil;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.client.util.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

@Service
@Profile(SpringProfileUtil.WRITE)
public class TweetMetaLiveService extends BaseTweetFromSourceLiveService<Retweet> implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TagRetrieverService tagService;

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private HttpLiveService httpService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private MinRtRetriever minRtRetriever;

    @Autowired
    private PredefinedAccountRetriever predefinedAccountRetriever;

    @Autowired
    private RetweetLiveStrategy retweetStrategy;

    @Autowired
    private TweetMetaLocalService tweetMetaLocalService;

    // metrics

    @Autowired
    private MetricRegistry metrics;
    private Counter anyByHashtagErrorsCounter;

    public TweetMetaLiveService() {
        super();
    }

    // API

    public final boolean retweetAnyByHashtag(final String twitterAccount) throws JsonProcessingException, IOException {
        String twitterTag = null;
        try {
            twitterTag = tagService.pickTwitterTag(twitterAccount);
            final boolean success = retweetAnyByHashtagInternal(twitterAccount, twitterTag);
            if (!success) {
                logger.warn("Unable to retweet any tweet on twitterAccount= {}, by twitterTag= {}", twitterAccount, twitterTag);
            }
            return success;
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by twitterTag= " + twitterTag, runtimeEx);
            anyByHashtagErrorsCounter.inc();
            return false;
        } catch (final Exception ex) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by twitterTag= " + twitterTag, ex);
            anyByHashtagErrorsCounter.inc();
            return false;
        }
    }

    public final boolean retweetAnyByHashtagOnlyFromPredefinedAccounts(final String twitterAccount) throws JsonProcessingException, IOException {
        String twitterTag = null;
        try {
            twitterTag = tagService.pickTwitterTag(twitterAccount);
            final boolean success = retweetAnyByHashtagOnlyFromPredefinedAccountsInternal(twitterAccount, twitterTag);
            if (!success) {
                logger.warn("Unable to retweet any tweet on twitterAccount= {}, by twitterTag= {}, from predefined accounts", twitterAccount, twitterTag);
            }
            return success;
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by twitterTag= " + twitterTag + ", from predefined accounts", runtimeEx);
            return false;
        } catch (final Exception ex) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by twitterTag= " + twitterTag + ", from predefined accounts", ex);
            return false;
        }
    }

    /*test only*/final boolean retweetAnyByHashtag(final String twitterAccount, final String twitterTag) throws JsonProcessingException, IOException {
        try {
            final boolean success = retweetAnyByHashtagInternal(twitterAccount, twitterTag);
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
    protected final boolean tryTweetOne(final String fullTweet, final String noUrl, final String twitterAccount, final Map<String, Object> customDetails) {
        final long tweetId = (long) customDetails.get("tweetId");
        final String hashtag = (String) customDetails.get("hashtag");
        final Tweet potentialTweet = (Tweet) customDetails.get("potentialTweet");

        logger.trace("Considering to retweet on twitterAccount= {}, tweetId= {}, tweetText= {}", twitterAccount, tweetId, fullTweet);

        // is it worth it by text only?
        if (!tweetService.isTweetWorthRetweetingByText(fullTweet)) {
            logger.debug("Tweet not worth retweeting (by text only) on twitterAccount= {}, tweet text= {}", twitterAccount, fullTweet);
            return false;
        }

        // is it worth it by full tweet?
        if (!tweetService.isTweetWorthRetweetingByFullTweet(potentialTweet, hashtag)) {
            logger.debug("Tweet not worth retweeting (by full tweet) on twitterAccount= {}, tweet text= {}", twitterAccount, fullTweet);
            return false;
        }

        // pre-validity
        final String fullTweetProcessedPreValidity = tweetService.processPreValidity(fullTweet);
        // is it valid?
        if (!tweetService.isTweetFullValid(fullTweetProcessedPreValidity)) {
            // TODO: was debug, temporarily error because for meta, this should not happen
            final String tweetUrl = "https://twitter.com/" + potentialTweet.getFromUser() + "/status/" + potentialTweet.getId();
            logger.error("Tweet invalid (size, link count) on twitterAccount= {}, tweet text= {}\n- url={}", twitterAccount, fullTweetProcessedPreValidity, tweetUrl);
            return false;
        }
        // post-validity
        final String fullTweetProcessed = tweetService.postValidityProcessForFullTweet(fullTweetProcessedPreValidity, twitterAccount);

        // is this tweet pointing to something good?
        if (!isTweetPointingToSomethingGood(fullTweetProcessed)) {
            logger.debug("Tweet not pointing to something good on twitterAccount= {}, tweet text= {}", twitterAccount, fullTweetProcessed);
            return false;
        }

        // is the tweet rejected by some classifier?
        if (isTweetRejectedByClassifier(fullTweetProcessed)) {
            logger.error("Tweet rejected by a classifier on twitterAccount= {}\n--tweet text= {}", twitterAccount, fullTweetProcessed);
            return false;
        }

        // after text processing, check again if this has already been retweeted
        final Retweet alreadyExistingRetweetByText = hasThisAlreadyBeenTweetedByText(fullTweetProcessed, twitterAccount);
        if (alreadyExistingRetweetByText != null) {
            logger.warn("Tweet with retweet mention already exists:\n-original tweet= {}\n-new tweet (not retweeted)= {}", alreadyExistingRetweetByText.getText(), fullTweetProcessed); // TODO: temporarily warn - should get to debug
            return false;
        }

        boolean success = false;
        if (tweetService.isRetweetMention(fullTweetProcessed)) {
            final String tweetUrl = "https://twitter.com/" + potentialTweet.getFromUser() + "/status/" + potentialTweet.getId();
            logger.error("(temporary error)Tweet is a retweet mention - url= {}\nTweeet= {}", tweetUrl, fullTweetProcessed); // TODO: temporarily error

            final String originalUserFromRt = Preconditions.checkNotNull(TwitterUtil.extractOriginalUserFromRt(fullTweetProcessed));
            final TwitterProfile profileOfUser = twitterReadLiveService.getProfileOfUser(originalUserFromRt);
            final boolean isUserWorthInteractingWith = retweetStrategy.isUserWorthInteractingWith(profileOfUser, originalUserFromRt);
            if (isUserWorthInteractingWith) {
                success = twitterWriteLiveService.tweet(twitterAccount, fullTweetProcessed, potentialTweet);
            } else {
                logger.info("Tweet rejected on twitterAccount= {}, tweet text= {}\nReason: not worth interacting with user= {}", twitterAccount, fullTweetProcessed, originalUserFromRt);
                return false;
            }
        } else { // not retweet mention - normal tweet
            if (retweetStrategy.shouldRetweet(potentialTweet)) {
                success = twitterWriteLiveService.retweet(twitterAccount, tweetId);
            } else {
                success = twitterWriteLiveService.tweet(twitterAccount, fullTweetProcessed, potentialTweet);
            }
        }

        // mark
        if (success) {
            markDone(new Retweet(tweetId, twitterAccount, fullTweetProcessed, new Date()));
        }

        // done
        return success;
    }

    @Override
    protected final boolean hasThisAlreadyBeenTweetedById(final Retweet retweet) {
        return tweetMetaLocalService.hasThisAlreadyBeenTweetedById(retweet);
    }

    protected final Retweet hasThisAlreadyBeenTweetedByText(final String text, final String twitterAccount) {
        return tweetMetaLocalService.findLocalCandidateAdvanced(text, twitterAccount);
    }

    @Override
    protected final void markDone(final Retweet entity) {
        tweetMetaLocalService.markDone(entity);
    }

    @Override
    protected final IRetweetJpaDAO getApi() {
        throw new UnsupportedOperationException();
    }

    // util

    private final boolean retweetAnyByHashtagOnlyFromPredefinedAccountsInternal(final String twitterAccount, final String hashtag) throws JsonProcessingException, IOException {
        logger.info("Begin trying to retweet on twitterAccount= {}, by hashtag= {}", twitterAccount, hashtag);

        logger.trace("Trying to retweet on twitterAccount= {}", twitterAccount);
        final List<Tweet> tweetsOfHashtag = twitterReadLiveService.listTweetsOfHashtag(twitterAccount, hashtag);

        // filter out tweets not on predefined accounts
        final List<String> predefinedAccounts = predefinedAccountRetriever.predefinedAccount(twitterAccount);
        final Iterable<Tweet> tweetsFromOnlyPredefinedAccountsRaw = Iterables.filter(tweetsOfHashtag, new Predicate<Tweet>() {
            @Override
            public final boolean apply(@Nullable final Tweet input) {
                final String fromUser = input.getFromUser();
                return predefinedAccounts.contains(fromUser);
            }
        });

        final List<Tweet> tweetsFromOnlyPredefinedAccounts = Lists.newArrayList(tweetsFromOnlyPredefinedAccountsRaw);
        Collections.sort(tweetsFromOnlyPredefinedAccounts, Ordering.from(new Comparator<Tweet>() {
            @Override
            public final int compare(final Tweet t1, final Tweet t2) {
                return t2.getRetweetCount().compareTo(t1.getRetweetCount());
            }
        }));

        return tryRetweetAnyByHashtagInternal(twitterAccount, tweetsFromOnlyPredefinedAccounts, hashtag);
    }

    private final boolean retweetAnyByHashtagInternal(final String twitterAccount, final String hashtag) throws JsonProcessingException, IOException {
        logger.info("Begin trying to retweet on twitterAccount= {}, by hashtag= {}", twitterAccount, hashtag);

        logger.trace("Trying to retweet on twitterAccount= {}", twitterAccount);
        final List<Tweet> tweetsOfHashtag = twitterReadLiveService.listTweetsOfHashtag(twitterAccount, hashtag);
        Collections.sort(tweetsOfHashtag, Ordering.from(new Comparator<Tweet>() {
            @Override
            public final int compare(final Tweet t1, final Tweet t2) {
                return t2.getRetweetCount().compareTo(t1.getRetweetCount());
            }
        }));

        return tryRetweetAnyByHashtagInternal(twitterAccount, tweetsOfHashtag, hashtag);
    }

    private final boolean tryRetweetAnyByHashtagInternal(final String twitterAccount, final List<Tweet> potentialTweets, final String hashtag) throws IOException, JsonProcessingException {
        if (!TwitterAccountEnum.valueOf(twitterAccount).isRt()) {
            logger.error("Should not retweet on twitterAccount= {}", twitterAccount);
        }
        for (final Tweet potentialTweet : potentialTweets) {
            // TODO: add a tryRetweetOneByHashtagInternal and have a custom enum return from that
            final long tweetId = potentialTweet.getId();
            logger.trace("Considering to retweet on twitterAccount= {}, from hashtag= {}, tweetId= {}", twitterAccount, hashtag, tweetId);
            if (!hasThisAlreadyBeenTweetedById(new Retweet(tweetId, twitterAccount, null, null))) {
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
        try {
            return tryTweetOneDelegatorInternal(potentialTweet, hashtag, twitterAccount);
        } catch (final RuntimeException runEx) {
            // this is new - the point it to recover, log and keep analyzing
            logger.error("Unexpected exception trying to tweet on twitterAccount= {}, tweetText= {}", twitterAccount, potentialTweet);
            return false;
        }
    }

    private final boolean tryTweetOneDelegatorInternal(final Tweet potentialTweet, final String hashtag, final String twitterAccount) {
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

    /**
     * Tweet is <b>not</b> pointing to something good if: <br/>
     * - it has <b>no url</b><br/>
     * - it points to a <b>homepage</b><br/>
     * - it points to a <b>banned domain</b><br/>
     */
    private final boolean isTweetPointingToSomethingGood(final String potentialTweet) {
        final List<String> extractedUrls = linkService.extractUrls(potentialTweet);
        if (extractedUrls.isEmpty()) {
            logger.trace("Tweet rejected because the it contains no urls\n- potentialTweet= {} ", potentialTweet);
            return false;
        }
        String singleMainUrl = linkService.determineMainUrl(extractedUrls);
        try {
            singleMainUrl = httpService.expand(singleMainUrl);
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

        if (LinkUtil.belongsToBannedDomain(singleMainUrl)) {
            logger.debug("potentialTweet= {} rejected because the it is pointing to a banned domain= {}", potentialTweet, singleMainUrl);
            return false;
        }

        return true;
    }

    // Spring

    @Override
    public void afterPropertiesSet() {
        anyByHashtagErrorsCounter = metrics.counter(MetricsUtil.Meta.RETWEET_ANY_BY_HASHTAG);
    }

}
