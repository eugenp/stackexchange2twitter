package org.common.metrics;

import org.tweet.meta.service.TweetMetaLiveService;
import org.tweet.twitter.service.live.TwitterReadLiveService;

import com.codahale.metrics.MetricRegistry;

public final class MetricsUtil {

    public static final class Meta {
        public static final String RETWEET_ANY_BY_HASHTAG = MetricRegistry.name(TweetMetaLiveService.class, "any", "by.hashtag");
        public static final String TWITTER_READ_OP = MetricRegistry.name(TwitterReadLiveService.class, "read", "op");
        public static final String TWITTER_WRITE_OP = MetricRegistry.name(TwitterReadLiveService.class, "write", "op");
    }

    private MetricsUtil() {
        throw new AssertionError();
    }

}
