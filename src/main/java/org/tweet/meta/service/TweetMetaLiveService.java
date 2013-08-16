package org.tweet.meta.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

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
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.component.PredefinedAccountRetriever;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.component.MinRtRetriever;
import org.tweet.twitter.service.TagRetrieverService;
import org.tweet.twitter.service.TweetMentionService;
import org.tweet.twitter.util.TweetUtil;
import org.tweet.twitter.util.TwitterInteraction;
import org.tweet.twitter.util.TwitterInteractionWithValue;
import org.tweet.twitter.util.TwitterUtil;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.client.util.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
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

    /**any*/
    public final boolean retweetAnyByHashtag(final String twitterAccount) throws JsonProcessingException, IOException {
        String twitterTag = null;
        try {
            twitterTag = tagRetrieverService.pickTwitterTag(twitterAccount);
            final boolean success = retweetAnyByHashtagInternal(twitterAccount, twitterTag);
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

    /**any*/
    public final boolean retweetAnyByHashtagOnlyFromPredefinedAccounts(final String twitterAccount) throws JsonProcessingException, IOException {
        String twitterTag = null;
        try {
            twitterTag = tagRetrieverService.pickTwitterTag(twitterAccount);
            final boolean success = retweetAnyByHashtagOnlyFromPredefinedAccountsInternal(twitterAccount, twitterTag);
            if (!success) {
                logger.warn("Unable to retweet any tweet on twitterAccount= {}, by twitterTag= {}, from predefined accounts", twitterAccount, twitterTag);
            }
            return success;
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by twitterTag= " + twitterTag + ", from predefined accounts", runtimeEx);
            metrics.counter(MetricsUtil.Meta.RETWEET_ANY_ERROR).inc();
            return false;
        } catch (final Exception ex) {
            logger.error("Unexpected exception when trying to retweet on twitterAccount= " + twitterAccount + ", by twitterTag= " + twitterTag + ", from predefined accounts", ex);
            metrics.counter(MetricsUtil.Meta.RETWEET_ANY_ERROR).inc();
            return false;
        }
    }

    // util - any

    /**any*/
    /*test only*/final boolean retweetAnyByHashtag(final String twitterAccount, final String hashtag) throws JsonProcessingException, IOException {
        try {
            final boolean success = retweetAnyByHashtagInternal(twitterAccount, hashtag);
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

    /**any*/
    private final boolean retweetAnyByHashtagInternal(final String twitterAccount, final String hashtag) throws JsonProcessingException, IOException {
        logger.info("Begin trying to retweet on twitterAccount= {}, by hashtag= {}", twitterAccount, hashtag);

        final List<Tweet> tweetsOfHashtag = twitterReadLiveService.listTweetsOfHashtag(twitterAccount, hashtag);

        final Collection<Tweet> prunedTweets = pruneTweets(tweetsOfHashtag, hashtag);

        return retweetAnyByHashtagInternal(twitterAccount, prunedTweets, hashtag);
    }

    private final Collection<Tweet> pruneTweets(final List<Tweet> tweetsOfHashtag, final String hashtag) {
        final Set<Tweet> tweetsSet = Sets.newHashSet();
        for (final Tweet tweet : tweetsOfHashtag) {
            tweetsSet.add(TweetUtil.getTweet(tweet));
        }

        final int minRt = minRtRetriever.minRt(hashtag);
        final Collection<Tweet> tweetSetFiltered = Collections2.filter(tweetsSet, new Predicate<Tweet>() {
            @Override
            public final boolean apply(final Tweet tweet) {
                if (tweet.getRetweetCount() <= (minRt / 2)) {
                    return false;
                }
                if (!tweetService.isTweetWorthRetweetingByText(tweet.getText(), hashtag)) {
                    return false;
                }
                return true;
            }
        });

        final List<Tweet> tweets = Lists.newArrayList(tweetSetFiltered);
        Collections.sort(tweets, Ordering.from(new TweetByRtComparator()));

        final Map<TwitterInteractionWithValue, Tweet> valueToTweet = Maps.newTreeMap(new Comparator<TwitterInteractionWithValue>() {
            @Override
            public final int compare(final TwitterInteractionWithValue o1, final TwitterInteractionWithValue o2) {
                return Integer.compare(o2.getVal(), o1.getVal());
            }
        });

        for (final Tweet tweet : tweets) {
            final TwitterInteractionWithValue interactionValue = interactionLiveService.decideBestInteractionRaw(tweet);
            // tweak score based on the number of RTs
            final int newScore = interactionValue.getVal() + tweet.getRetweetCount() * 30 / 100;
            valueToTweet.put(new TwitterInteractionWithValue(interactionValue.getTwitterInteraction(), newScore), tweet);
        }

        // adjust the RT counts by adding some fraction of the overall value
        for (final Map.Entry<TwitterInteractionWithValue, Tweet> valueAndTweet : valueToTweet.entrySet()) {
            final Tweet theTweet = valueAndTweet.getValue();
            final int newRtCount = theTweet.getRetweetCount() + valueAndTweet.getKey().getVal() / 15;
            theTweet.setRetweetCount(newRtCount);
        }

        return Lists.newArrayList(valueToTweet.values());
        // return tweets;
    }

    /**any*/
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
        pruneTweets(tweetsFromOnlyPredefinedAccounts, hashtag);

        return retweetAnyByHashtagInternal(twitterAccount, tweetsFromOnlyPredefinedAccounts, hashtag);
    }

    /**any*/
    private final boolean retweetAnyByHashtagInternal(final String twitterAccount, final Collection<Tweet> potentialTweetsSorted, final String hashtag) throws IOException, JsonProcessingException {
        if (!TwitterAccountEnum.valueOf(twitterAccount).isRt()) {
            logger.error("Should not retweet on twitterAccount= {}", twitterAccount);
        }
        for (final Tweet potentialTweetUnprocessed : potentialTweetsSorted) {
            final Tweet potentialTweet = TweetUtil.getTweet(potentialTweetUnprocessed);
            final long tweetId = potentialTweet.getId();
            logger.trace("Considering to retweet on twitterAccount= {}, from hashtag= {}, tweetId= {}", twitterAccount, hashtag, tweetId);
            if (!hasThisAlreadyBeenTweetedById(new Retweet(tweetId, twitterAccount, null, null))) {
                logger.debug("Attempting to tweet on twitterAccount= {}, from hashtag= {}, tweetId= {}", twitterAccount, hashtag, tweetId);
                final boolean success = tryTweetOneWrap(potentialTweet, hashtag, twitterAccount);
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

    // util - one

    /**one*/
    private final boolean tryTweetOneWrap(final Tweet potentialTweet, final String hashtag, final String twitterAccount) {
        try {
            return tryTweetOnePrepare(potentialTweet, hashtag, twitterAccount);
        } catch (final RuntimeException runEx) {
            // this is new - the point it to recover, log and keep analyzing
            logger.error("Unexpected exception trying to tweet on twitterAccount= " + twitterAccount + ", tweetText= " + potentialTweet.getText(), runEx);
            metrics.counter(MetricsUtil.Meta.RETWEET_ONE_ERROR).inc();
            return false;
        }
    }

    /**one*/
    private final boolean tryTweetOnePrepare(final Tweet potentialTweet, final String hashtag, final String twitterAccount) {
        Preconditions.checkState(potentialTweet.getRetweetedStatus() == null, "By the `one` level - this should be the original tweet");
        final String text = potentialTweet.getText();
        final long tweetId = potentialTweet.getId();
        logger.trace("Considering to retweet on twitterAccount= {}, tweetId= {}, tweetText= {}", twitterAccount, tweetId, text);

        final Map<String, Object> customDetails = Maps.newHashMap();
        customDetails.put("tweetId", tweetId);
        customDetails.put("hashtag", hashtag);
        customDetails.put("potentialTweet", potentialTweet);

        return tryTweetOne(text, null, twitterAccount, customDetails);
    }

    /**one*/
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

        // pre-validity processing
        final String fullTweetProcessedPreValidity = tweetService.processPreValidity(fullTweet);
        // is it valid?
        if (!tweetService.isTweetFullValid(fullTweetProcessedPreValidity)) {
            final String tweetUrl = "https://twitter.com/" + potentialTweet.getFromUser() + "/status/" + potentialTweet.getId();
            logger.error("Tweet invalid (size, link count) on twitterAccount= {}, tweet text= {}\n- url={}", twitterAccount, fullTweetProcessedPreValidity, tweetUrl);
            // TODO: was debug, temporarily error because for meta, this should not happen
            return false;
        }
        // post-validity processing
        final String fullTweetProcessed = tweetService.postValidityProcessTweetTextWithUrl(fullTweetProcessedPreValidity, twitterAccount);

        // after text processing, check again if this has already been retweeted
        final Retweet alreadyExistingRetweetByText = hasThisAlreadyBeenTweetedByText(fullTweetProcessed, twitterAccount);
        if (alreadyExistingRetweetByText != null) {
            // was warn, but an already existing tweet is likely OK so - debug
            logger.debug("Tweet with retweet mention already exists:\n-original tweet= {}\n-new tweet (not retweeted)= {}", alreadyExistingRetweetByText.getText(), fullTweetProcessed);
            return false;
        }

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

        boolean success = false;
        if (tweetMentionService.isRetweetMention(fullTweetProcessed)) {
            success = dealWithRtMention(twitterAccount, potentialTweet, fullTweetProcessed);
        } else {
            final TwitterInteraction bestInteraction = interactionLiveService.decideBestInteraction(potentialTweet);
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
        }

        // mark
        if (success) {
            markDone(new Retweet(tweetId, twitterAccount, fullTweetProcessed, new Date()));
        }

        // done
        return success;
    }

    /**one*/
    private final boolean dealWithRtMention(final String twitterAccount, final Tweet potentialTweet, final String fullTweetProcessed) {
        boolean success;
        final String tweetUrl = "https://twitter.com/" + potentialTweet.getFromUser() + "/status/" + potentialTweet.getId();
        logger.error("(temporary error)Tweet is a retweet mention - url= {}\nTweeet= {}", tweetUrl, fullTweetProcessed); // TODO: temporarily error

        // TODO: if this code path is actually executed, then look into extracting the original user from the tweet itself, not from the text
        final String originalUserFromRt = Preconditions.checkNotNull(TwitterUtil.extractOriginalUserFromRt(fullTweetProcessed));
        final TwitterProfile profileOfUser = twitterReadLiveService.getProfileOfUser(originalUserFromRt);

        final boolean isUserWorthInteractingWith = interactionLiveService.isUserWorthInteractingWithLive(profileOfUser, originalUserFromRt);
        if (isUserWorthInteractingWith) {
            success = twitterWriteLiveService.tweet(twitterAccount, fullTweetProcessed, potentialTweet);
        } else {
            logger.info("Tweet rejected on twitterAccount= {}, tweet text= {}\nReason: not worth interacting with user= {}", twitterAccount, fullTweetProcessed, originalUserFromRt);
            success = false;
        }
        return success;
    }

    /**one*/
    private final boolean isTweetRejectedByClassifier(final String text) {
        if (classificationService.isCommercialDefault(text)) {
            return true; // temporarily - to see how it works
            // return false; // temporarily, until there is more classification training data for commercial-noncommercial
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
    private final boolean isTweetPointingToSomethingGood(final String potentialTweet) {
        final List<String> extractedUrls = linkService.extractUrls(potentialTweet);
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

        if (LinkUtil.belongsToBannedDomain(singleMainUrl)) {
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
