package org.tweet.meta.analysis;

import org.springframework.social.twitter.api.Tweet;
import org.tweet.twitter.service.TweetService;

import com.google.common.base.Predicate;

public final class TweetPassesSet1ChecksPredicate implements Predicate<Tweet> {

    private TweetService tweetService;

    public TweetPassesSet1ChecksPredicate(final TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @Override
    public final boolean apply(final Tweet tweet) {
        return tweetService.passesSet1OfChecks(tweet, null);
    }

}
