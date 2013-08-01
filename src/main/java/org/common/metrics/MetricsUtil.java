package org.common.metrics;

import org.tweet.meta.service.TweetMetaLiveService;

import com.codahale.metrics.MetricRegistry;

public final class MetricsUtil {

    public static final class Meta {
        public static final String RETWEET_ANY_BY_HASHTAG = MetricRegistry.name(TweetMetaLiveService.class, "any", "by.hashtag");
    }

    private MetricsUtil() {
        throw new AssertionError();
    }

}
