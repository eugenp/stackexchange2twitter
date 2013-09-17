package org.tweet.meta.util;

import org.springframework.social.twitter.api.Tweet;
import org.tweet.twitter.service.TweetService;
import org.tweet.twitter.util.TweetUtil;

import com.google.common.base.Predicate;

public final class TweetPassesLevel2Predicate implements Predicate<Tweet> {

    private TweetService tweetService;

    public TweetPassesLevel2Predicate(final TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @Override
    public final boolean apply(final Tweet tweet) {
        return tweetService.passesLevel2Checks(TweetUtil.getText(tweet));
    }

}
