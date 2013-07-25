package org.tweet.meta.service;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.twitter.service.TweetService;
import org.tweet.twitter.util.TwitterUtil;

import com.google.common.collect.Lists;

@Service
public class TweetMetaLocalService {
    // private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IRetweetJpaDAO retweetApi;

    @Autowired
    protected TweetService tweetService;

    public TweetMetaLocalService() {
        super();
    }

    // API

    // template

    protected final boolean hasThisAlreadyBeenTweetedById(final Retweet retweet) {
        final Retweet existingTweetById = retweetApi.findOneByTweetIdAndTwitterAccount(retweet.getTweetId(), retweet.getTwitterAccount());
        return existingTweetById != null;
    }

    protected final Retweet hasThisAlreadyBeenTweetedByText(final String fullTextAfterProcessing, final String twitterAccount) {
        final Retweet existingTweetByFullText = retweetApi.findOneByTextAndTwitterAccount(fullTextAfterProcessing, twitterAccount);
        if (existingTweetByFullText != null) {
            return existingTweetByFullText;
        }

        // retweet mention
        final String tweetTextPotentiallyWithoutRetweetMention = TwitterUtil.extractTweetFromRt(fullTextAfterProcessing);
        final Retweet exactMatchRetweet = retweetApi.findOneByTextAndTwitterAccount(tweetTextPotentiallyWithoutRetweetMention, twitterAccount);
        if (exactMatchRetweet != null) {
            return exactMatchRetweet;
        }
        final Retweet endsWithOriginalTweet = retweetApi.findOneByTextEndsWithAndTwitterAccount(tweetTextPotentiallyWithoutRetweetMention, twitterAccount);
        if (endsWithOriginalTweet != null) {
            return endsWithOriginalTweet;
        }
        final Retweet startsWithOriginalTweet = retweetApi.findOneByTextStartsWithAndTwitterAccount(tweetTextPotentiallyWithoutRetweetMention, twitterAccount);
        if (startsWithOriginalTweet != null) {
            return startsWithOriginalTweet;
        }

        // note: described in: https://github.com/eugenp/stackexchange2twitter/issues/95
        final Pair<String, String> beforeAndAfter = TwitterUtil.breakByUrl(fullTextAfterProcessing);
        if (beforeAndAfter == null) {
            return null;
        }
        final List<Retweet> partialMatches = Lists.newArrayList();
        if (beforeAndAfter.getLeft().length() > beforeAndAfter.getRight().length()) {
            partialMatches.addAll(retweetApi.findAllByTextStartsWithAndTwitterAccount(beforeAndAfter.getLeft(), twitterAccount));
        } else {
            partialMatches.addAll(retweetApi.findAllByTextEndsWithAndTwitterAccount(beforeAndAfter.getRight(), twitterAccount));
        }
        if (!partialMatches.isEmpty()) {
            return partialMatches.get(0);
        }

        return null;
    }

    protected final void markDone(final Retweet entity) {
        retweetApi.save(entity);
    }

    // util

}
