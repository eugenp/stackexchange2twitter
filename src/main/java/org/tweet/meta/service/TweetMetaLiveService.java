package org.tweet.meta.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.classification.service.ClassificationService;
import org.common.service.BaseTweetFromSourceLiveService;
import org.common.service.LinkService;
import org.common.service.live.HttpLiveService;
import org.common.util.LinkUtils;
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
import org.tweet.twitter.util.TwitterUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.client.util.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

@Service
@Profile(SpringProfileUtil.WRITE)
public class TweetMetaLiveService extends BaseTweetFromSourceLiveService<Retweet> {
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

    public TweetMetaLiveService() {
        super();
    }

    // API

    public final boolean retweetByHashtagOnlyFromPredefinedAccounts(final String twitterAccount) throws JsonProcessingException, IOException {
        String twitterTag = null;
        try {
            twitterTag = tagService.pickTwitterTag(twitterAccount);
            final boolean success = retweetByHashtagOnlyFromPredefinedAccountsInternal(twitterAccount, twitterTag);
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

        // is it worth it by text only?
        if (!tweetService.isTweetWorthRetweetingByText(text)) {
            logger.debug("Tweet not worth retweeting (by text only) on twitterAccount= {}, tweet text= {}", twitterAccount, text);
            return false;
        }

        // is it worth it by full tweet?
        if (!tweetService.isTweetWorthRetweetingByFullTweet(potentialTweet, hashtag)) {
            logger.debug("Tweet not worth retweeting (by full tweet) on twitterAccount= {}, tweet text= {}", twitterAccount, text);
            return false;
        }

        // pre-validity
        final String tweetText = tweetService.preValidityProcess(text);
        // is it valid?
        if (!tweetService.isTweetTextValid(tweetText)) {
            logger.debug("Tweet invalid (size, link count) on twitterAccount= {}, tweet text= {}", twitterAccount, tweetText);
            return false;
        }
        // post-validity
        final String processedTweetText = tweetService.postValidityProcess(tweetText, twitterAccount);

        // is this tweet pointing to something good?
        if (!isTweetPointingToSomethingGood(tweetText)) {
            logger.debug("Tweet not pointing to something good on twitterAccount= {}, tweet text= {}", twitterAccount, tweetText);
            return false;
        }

        // is the tweet rejected by some classifier?
        if (isTweetRejectedByClassifier(processedTweetText)) {
            logger.error("Tweet rejected by a classifier on twitterAccount= {}, tweet text= {}", twitterAccount, processedTweetText);
            return false;
        }

        // after text processing, check again if this has already been retweeted
        final Retweet alreadyExistingRetweetByText = hasThisAlreadyBeenTweetedByText(processedTweetText, twitterAccount);
        if (alreadyExistingRetweetByText != null) {
            logger.warn("Tweet with retweet mention already exists; original tweet= {}\n new tweet(not retweeted)= ", alreadyExistingRetweetByText, processedTweetText); // TODO: temporarily warn - should get to debug
            return false;
        }

        boolean success = false;
        if (tweetService.isRetweetMention(processedTweetText)) {
            final String tweetUrl = "https://twitter.com/" + potentialTweet.getFromUser() + "/status/" + potentialTweet.getId();
            logger.error("Tweet is a retweet mention - url= {}\nTweeet= {}", tweetUrl, processedTweetText); // TODO: temporarily error

            final String originalUserFromRt = Preconditions.checkNotNull(TwitterUtil.extractOriginalUserFromRt(processedTweetText));
            final TwitterProfile profileOfUser = twitterReadLiveService.getProfileOfUser(originalUserFromRt);
            final boolean isUserWorthInteractingWith = retweetStrategy.isUserWorthInteractingWith(profileOfUser, originalUserFromRt);
            if (isUserWorthInteractingWith) {
                success = twitterWriteLiveService.tweet(twitterAccount, processedTweetText);
            } else {
                logger.info("Tweet rejected on twitterAccount= {}, tweet text= {}\nReason: not worth interacting with user= {}", twitterAccount, processedTweetText, originalUserFromRt);
                return false;
            }
        } else { // not retweet mention - normal tweet
            if (retweetStrategy.shouldRetweetRandomized(potentialTweet)) {
                success = twitterWriteLiveService.retweet(twitterAccount, tweetId);
            } else {
                success = twitterWriteLiveService.tweet(twitterAccount, processedTweetText);
            }
        }

        // mark
        markDone(new Retweet(tweetId, twitterAccount, processedTweetText));

        // done
        return success;
    }

    @Override
    protected final boolean hasThisAlreadyBeenTweetedById(final Retweet retweet) {
        return tweetMetaLocalService.hasThisAlreadyBeenTweetedById(retweet);
    }

    protected final Retweet hasThisAlreadyBeenTweetedByText(final String text, final String twitterAccount) {
        return tweetMetaLocalService.hasThisAlreadyBeenTweetedByText(text, twitterAccount);
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

    private final boolean retweetByHashtagOnlyFromPredefinedAccountsInternal(final String twitterAccount, final String hashtag) throws JsonProcessingException, IOException {
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

        return tryRetweetByHashtagInternal(twitterAccount, tweetsFromOnlyPredefinedAccounts, hashtag);
    }

    private final boolean retweetByHashtagInternal(final String twitterAccount, final String hashtag) throws JsonProcessingException, IOException {
        logger.info("Begin trying to retweet on twitterAccount= {}, by hashtag= {}", twitterAccount, hashtag);

        logger.trace("Trying to retweet on twitterAccount= {}", twitterAccount);
        final List<Tweet> tweetsOfHashtag = twitterReadLiveService.listTweetsOfHashtag(twitterAccount, hashtag);
        Collections.sort(tweetsOfHashtag, Ordering.from(new Comparator<Tweet>() {
            @Override
            public final int compare(final Tweet t1, final Tweet t2) {
                return t2.getRetweetCount().compareTo(t1.getRetweetCount());
            }
        }));

        return tryRetweetByHashtagInternal(twitterAccount, tweetsOfHashtag, hashtag);
    }

    private final boolean tryRetweetByHashtagInternal(final String twitterAccount, final List<Tweet> potentialTweets, final String hashtag) throws IOException, JsonProcessingException {
        if (!TwitterAccountEnum.valueOf(twitterAccount).isRt()) {
            logger.error("Should not retweet on twitterAccount= {}", twitterAccount);
        }
        for (final Tweet potentialTweet : potentialTweets) {
            final long tweetId = potentialTweet.getId();
            logger.trace("Considering to retweet on twitterAccount= {}, from hashtag= {}, tweetId= {}", twitterAccount, hashtag, tweetId);
            if (!hasThisAlreadyBeenTweetedById(new Retweet(tweetId, twitterAccount, null))) {
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
        String singleMainUrl = LinkUtils.extractUrls(potentialTweet).get(0);
        try {
            singleMainUrl = httpService.expandInternal(singleMainUrl);
        } catch (final RuntimeException ex) {
            logger.error("Unable to expand URL: " + singleMainUrl, ex); // may become warn
            return false;
        } catch (final IOException ioEx) {
            logger.error("Unable to expand URL: " + singleMainUrl, ioEx);
            return false;
        }

        if (linkService.isHomepageUrl(singleMainUrl)) {
            logger.debug("potentialTweet= {} rejected because the it is pointing to a homepage= {}", potentialTweet, singleMainUrl);
            return false;
        }

        if (LinkUtils.belongsToBannedDomain(singleMainUrl)) {
            logger.debug("potentialTweet= {} rejected because the it is pointing to a banned domain= {}", potentialTweet, singleMainUrl);
            return false;
        }

        return true;
    }

}
