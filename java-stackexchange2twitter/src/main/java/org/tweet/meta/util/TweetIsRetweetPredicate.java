package org.tweet.meta.util;

import org.springframework.social.twitter.api.Tweet;

import com.google.common.base.Predicate;

public final class TweetIsRetweetPredicate implements Predicate<Tweet> {

    @Override
    public final boolean apply(final Tweet tweet) {
        return tweet.isRetweet();
    }

}
