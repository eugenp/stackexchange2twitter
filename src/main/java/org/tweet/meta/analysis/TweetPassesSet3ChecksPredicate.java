package org.tweet.meta.analysis;

import org.springframework.social.twitter.api.Tweet;
import org.tweet.twitter.service.TweetService;
import org.tweet.twitter.util.TweetUtil;

import com.google.common.base.Predicate;

public final class TweetPassesSet3ChecksPredicate implements Predicate<Tweet> {

    private TweetService tweetService;

    public TweetPassesSet3ChecksPredicate(final TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @Override
    public final boolean apply(final Tweet tweet) {
        return tweetService.passesSet3OfChecksForAnalysis(TweetUtil.getText(tweet));
    }

}
