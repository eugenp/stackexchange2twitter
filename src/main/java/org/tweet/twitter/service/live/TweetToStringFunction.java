package org.tweet.twitter.service.live;

import org.springframework.social.twitter.api.Tweet;
import org.tweet.twitter.util.TweetUtil;

import com.google.common.base.Function;

public final class TweetToStringFunction implements Function<Tweet, String> {

    @Override
    public final String apply(final Tweet input) {
        return TweetUtil.getText(input);
    }

}