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
        final List<Retweet> foundLocalCandidates = findLocalCandidatesStrict(fullTextWithUrlAfterProcessing, twitterAccount);
        Preconditions.checkNotNull(foundLocalCandidates);
        if (foundLocalCandidates.isEmpty()) {
            return null;
        }

        return foundLocalCandidates.get(0);
    }

    public final List<Retweet> findLocalCandidatesStrict(final String fullTextWithUrlAfterProcessing, final String twitterAccount) {
        final List<Retweet> byExactMatch = findByExactMatch(fullTextWithUrlAfterProcessing, twitterAccount);
        if (byExactMatch != null) {
            return byExactMatch;
        }

        final List<Retweet> byPartialMatch = findByTweetExtractedFromRtMention(fullTextWithUrlAfterProcessing, twitterAccount);
        if (byPartialMatch != null) {
            return byPartialMatch;
        }

        // note: described in: https://github.com/eugenp/stackexchange2twitter/issues/95
        final List<Retweet> partialResultsFromPrePostUrl = findPartialResultsFromPrePostUrl(fullTextWithUrlAfterProcessing, twitterAccount);
        if (partialResultsFromPrePostUrl != null) {
            return partialResultsFromPrePostUrl;
        }

        // by URL
        final List<Retweet> partialResultsFromUrl = findPartialResultsFromUrl(fullTextWithUrlAfterProcessing, twitterAccount);
        if (partialResultsFromUrl != null) {
            return partialResultsFromUrl;
        }

        return Lists.newArrayList();
    }

    public final List<Retweet> findLocalCandidatesRelaxed(final String fullTextWithUrlAfterProcessing, final String twitterAccount) {
        // note: described in: https://github.com/eugenp/stackexchange2twitter/issues/95
        final List<Retweet> partialResultsFromPrePostUrl = findPartialResultsFromPrePostUrl(fullTextWithUrlAfterProcessing, twitterAccount);
        if (partialResultsFromPrePostUrl != null) {
            return partialResultsFromPrePostUrl;
        }

        // by URL
        final List<Retweet> partialResultsFromUrl = findPartialResultsFromUrl(fullTextWithUrlAfterProcessing, twitterAccount);
        if (partialResultsFromUrl != null) {
            return partialResultsFromUrl;
        }

        final List<Retweet> byPartialMatch = findByTweetExtractedFromRtMention(fullTextWithUrlAfterProcessing, twitterAccount);
        if (byPartialMatch != null) {
            return byPartialMatch;
        }

        final List<Retweet> byExactMatch = findByExactMatch(fullTextWithUrlAfterProcessing, twitterAccount);
        if (byExactMatch != null) {
            return byExactMatch;
        }

        return Lists.newArrayList();
    }

    final List<Retweet> findByExactMatch(final String fullTextWithUrlAfterProcessing, final String twitterAccount) {
        final Retweet existingTweetByFullText = retweetApi.findOneByTextAndTwitterAccount(fullTextWithUrlAfterProcessing, twitterAccount);
        if (existingTweetByFullText != null) {
            return Lists.newArrayList(existingTweetByFullText);
        }

        return null;
    }

    final List<Retweet> findByTweetExtractedFromRtMention(final String fullTextWithUrlAfterProcessing, final String twitterAccount) {
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

        return null;
    }

    final List<Retweet> findPartialResultsFromUrl(final String fullTextWithUrlAfterProcessing, final String twitterAccount) {
        final String mainUrl = linkService.extractUrl(fullTextWithUrlAfterProcessing);
        final List<Retweet> retweetsPointingToTheSameUrl = retweetApi.findAllByTextContainsAndTwitterAccount(mainUrl, twitterAccount);
        if (!retweetsPointingToTheSameUrl.isEmpty()) {
            if (retweetsPointingToTheSameUrl.size() > 1) {
                // if this happens, then I will add more logging around this area
                logger.error("Temporary 1 - yes, more than ONE retweet found locally: \n-- this tweet = {}\n-- found locally: {}", fullTextWithUrlAfterProcessing, retweetsPointingToTheSameUrl);
                if (retweetsPointingToTheSameUrl.size() > 2) {
                    // mistake - to many results - probably an truncated link like `http://t.co` resulting in a lot of results
                    logger.error("Temporary 2 - more than TWO retweets found locally: \n-- this tweet = {}\n-- found locally: {}", fullTextWithUrlAfterProcessing, retweetsPointingToTheSameUrl);
                    return Lists.newArrayList();
                }
            }
            return retweetsPointingToTheSameUrl;
        }

        return null;
    }

    final List<Retweet> findPartialResultsFromPrePostUrl(final String fullTextWithUrlAfterProcessing, final String twitterAccount) {
        final Pair<String, String> beforeAndAfter = TwitterUtil.breakByUrl(fullTextWithUrlAfterProcessing);
        if (beforeAndAfter != null) {
            final List<Retweet> partialMatches = Lists.newArrayList();
            if (beforeAndAfter.getLeft().length() > beforeAndAfter.getRight().length()) {
                partialMatches.addAll(retweetApi.findAllByTextStartsWithAndTwitterAccount(beforeAndAfter.getLeft(), twitterAccount));
            } else {
                partialMatches.addAll(retweetApi.findAllByTextEndsWithAndTwitterAccount(beforeAndAfter.getRight(), twitterAccount));
            }
            if (!partialMatches.isEmpty()) {
                if (partialMatches.size() > 1) {
                    // if this happens, then I will add more logging around this area
                    logger.error("Temporary 3 - yes, more than ONE retweet found locally: \n-- this tweet = {}\n-- found locally: {}", fullTextWithUrlAfterProcessing, partialMatches);
                }
                return partialMatches;
            }
        }

        return null;
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
