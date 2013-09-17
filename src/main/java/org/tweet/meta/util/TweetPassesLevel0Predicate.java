package org.tweet.meta.util;

import org.springframework.social.twitter.api.Tweet;
import org.tweet.twitter.service.TweetService;

import com.google.common.base.Predicate;

public final class TweetPassesLevel0Predicate implements Predicate<Tweet> {

    private TweetService tweetService;

    public TweetPassesLevel0Predicate(final TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @Override
    public final boolean apply(final Tweet tweet) {
        return tweetService.passesLevel0MinimalChecks(tweet, null);
    }

}
