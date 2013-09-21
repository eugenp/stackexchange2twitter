package org.tweet.meta.util;

import org.springframework.social.twitter.api.Tweet;
import org.tweet.twitter.service.TweetService;

import com.google.common.base.Predicate;

public final class TweetPassesSet1And2ChecksPredicate implements Predicate<Tweet> {

    private TweetService tweetService;

    public TweetPassesSet1And2ChecksPredicate(final TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @Override
    public final boolean apply(final Tweet tweet) {
        return tweetService.passesSet1AndSet2OfChecks(tweet, null);
    }

}
