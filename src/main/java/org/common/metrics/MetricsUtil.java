package org.common.metrics;

import org.tweet.meta.service.TweetMetaLiveService;
import org.tweet.twitter.service.live.TwitterReadLiveService;
import org.tweet.twitter.service.live.TwitterWriteLiveService;

import com.codahale.metrics.MetricRegistry;

public final class MetricsUtil {

    public static final class Meta {
        public static final String RETWEET_ANY_ERROR = MetricRegistry.name(TweetMetaLiveService.class, "any", "error");
        public static final String RETWEET_ONE_ERROR = MetricRegistry.name(TweetMetaLiveService.class, "one", "error");

        public static final String TWITTER_READ_OP = MetricRegistry.name(TwitterReadLiveService.class, "read", "op");
        public static final String TWITTER_WRITE_OP = MetricRegistry.name(TwitterWriteLiveService.class, "write", "op");
    }

    private MetricsUtil() {
        throw new AssertionError();
    }

}
