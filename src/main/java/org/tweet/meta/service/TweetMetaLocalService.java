package org.tweet.meta.service;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.common.service.LinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.twitter.service.TweetService;
import org.tweet.twitter.util.TwitterUtil;

import com.google.api.client.util.Preconditions;
import com.google.common.collect.Lists;

@Service
public class TweetMetaLocalService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IRetweetJpaDAO retweetApi;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private LinkService linkService;

    public TweetMetaLocalService() {
        super();
    }

    // API

    public final Retweet findLocalCandidateAdvanced(final String fullTextWithUrlAfterProcessing, final String twitterAccount) {
        final List<Retweet> foundLocalCandidates = findLocalCandidatesAdvanced(fullTextWithUrlAfterProcessing, twitterAccount);
        Preconditions.checkNotNull(foundLocalCandidates);
        if (foundLocalCandidates.isEmpty()) {
            return null;
        }

        return foundLocalCandidates.get(0);
    }

    public final List<Retweet> findLocalCandidatesAdvanced(final String fullTextWithUrlAfterProcessing, final String twitterAccount) {
        final Retweet existingTweetByFullText = retweetApi.findOneByTextAndTwitterAccount(fullTextWithUrlAfterProcessing, twitterAccount);
        if (existingTweetByFullText != null) {
            return Lists.newArrayList(existingTweetByFullText);
        }

        // retweet mention
        final String tweetTextPotentiallyWithoutRetweetMention = TwitterUtil.extractTweetFromRt(fullTextWithUrlAfterProcessing);
        final Retweet exactMatchWithoutRetweetMention = retweetApi.findOneByTextAndTwitterAccount(tweetTextPotentiallyWithoutRetweetMention, twitterAccount);
        if (exactMatchWithoutRetweetMention != null) {
            return Lists.newArrayList(exactMatchWithoutRetweetMention);
        }
        final Retweet endsWithOriginalTweet = retweetApi.findOneByTextEndsWithAndTwitterAccount(tweetTextPotentiallyWithoutRetweetMention, twitterAccount);
        if (endsWithOriginalTweet != null) {
            return Lists.newArrayList(endsWithOriginalTweet);
        }
        final Retweet startsWithOriginalTweet = retweetApi.findOneByTextStartsWithAndTwitterAccount(tweetTextPotentiallyWithoutRetweetMention, twitterAccount);
        if (startsWithOriginalTweet != null) {
            return Lists.newArrayList(startsWithOriginalTweet);
        }

        // note: described in: https://github.com/eugenp/stackexchange2twitter/issues/95
        final Pair<String, String> beforeAndAfter = TwitterUtil.breakByUrl(fullTextWithUrlAfterProcessing);
        if (beforeAndAfter == null) {
            return Lists.newArrayList();
        }
        final List<Retweet> partialMatches = Lists.newArrayList();
        if (beforeAndAfter.getLeft().length() > beforeAndAfter.getRight().length()) {
            partialMatches.addAll(retweetApi.findAllByTextStartsWithAndTwitterAccount(beforeAndAfter.getLeft(), twitterAccount));
        } else {
            partialMatches.addAll(retweetApi.findAllByTextEndsWithAndTwitterAccount(beforeAndAfter.getRight(), twitterAccount));
        }
        if (!partialMatches.isEmpty()) {
            if (partialMatches.size() > 1) {
                // if this happens, then I will add more logging around this area
                logger.error("Temporary 1 - yes, more than one retweet found locally: \n-- this tweet = {}\n-- found locally: {}", fullTextWithUrlAfterProcessing, partialMatches);
            }
            return partialMatches;
        }

        // by url
        final String mainUrl = linkService.extractUrl(fullTextWithUrlAfterProcessing);
        final List<Retweet> retweetsPointingToTheSameUrl = retweetApi.findAllByTextContainsAndTwitterAccount(mainUrl, twitterAccount);
        if (!retweetsPointingToTheSameUrl.isEmpty()) {
            if (retweetsPointingToTheSameUrl.size() > 1) {
                // if this happens, then I will add more logging around this area
                logger.error("Temporary 2 - yes, more than one retweet found locally: ");
            }
            return retweetsPointingToTheSameUrl;
        }

        return Lists.newArrayList();
    }

    // template

    protected final boolean hasThisAlreadyBeenTweetedById(final Retweet retweet) {
        final Retweet existingTweetById = retweetApi.findOneByTweetIdAndTwitterAccount(retweet.getTweetId(), retweet.getTwitterAccount());
        return existingTweetById != null;
    }

    protected final void markDone(final Retweet entity) {
        retweetApi.save(entity);
    }

    // util

}
