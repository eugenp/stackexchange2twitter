package org.tweet.meta.util;

import org.springframework.social.twitter.api.Tweet;
import org.tweet.twitter.service.TweetService;

import com.google.common.base.Predicate;

public final class TweetPassesSet2ChecksPredicate implements Predicate<Tweet> {

    private TweetService tweetService;

    public TweetPassesSet2ChecksPredicate(final TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @Override
    public final boolean apply(final Tweet tweet) {
        return tweetService.passesSet2OfChecks(tweet, null);
    }

}
