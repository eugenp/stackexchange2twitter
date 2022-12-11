package org.common.metrics;

import org.tweet.meta.service.TweetMetaLiveService;
import org.tweet.twitter.service.live.TwitterReadLiveService;
import org.tweet.twitter.service.live.TwitterWriteLiveService;

import com.codahale.metrics.MetricRegistry;

public final class MetricsUtil {

    public static final class Meta {
        private static final String OK = "ok";
        private static final String ERROR = "err";

        private static final String READ = "read";
        private static final String WRITE = "write";

        public static final String RETWEET_ANY_ERROR = MetricRegistry.name(TweetMetaLiveService.class, "any", ERROR);
        public static final String RETWEET_ONE_ERROR = MetricRegistry.name(TweetMetaLiveService.class, "one", ERROR);

        public static final String TWITTER_READ_OK = MetricRegistry.name(TwitterReadLiveService.class, READ, OK);
        public static final String TWITTER_READ_ERR = MetricRegistry.name(TwitterReadLiveService.class, READ, ERROR);

        public static final String TWITTER_WRITE_OK = MetricRegistry.name(TwitterWriteLiveService.class, WRITE, OK);
        public static final String TWITTER_WRITE_ERR = MetricRegistry.name(TwitterWriteLiveService.class, WRITE, ERROR);

        public static final String HTTP_OK = MetricRegistry.name(TwitterWriteLiveService.class, WRITE, OK);
        public static final String HTTP_ERR = MetricRegistry.name(TwitterWriteLiveService.class, WRITE, "orr");
    }

    private MetricsUtil() {
        throw new AssertionError();
    }

}
