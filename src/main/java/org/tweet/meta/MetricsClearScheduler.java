package org.tweet.meta;

import java.io.IOException;

import org.common.metrics.MetricsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tweet.spring.util.SpringProfileUtil;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Profile(SpringProfileUtil.WRITE_PRODUCTION)
public class MetricsClearScheduler {
    // private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MetricRegistry metrics;

    public MetricsClearScheduler() {
        super();
    }

    // API

    // git - not 100% sure that hashtag will only return relevant tweets - look into this further
    // for accounts - not yet: BestBash,EclipseFacts,BestGit,BestJPA,BestMaven,BestOfRuby,SpringAtSO,ServerFaultBest,JavaTopSO,RESTDaily

    @Scheduled(cron = "0 59 9,10,11,12,13,14,15,16,17,18,19,20,21,22,23 * * *")
    public void tweetMeta1() throws JsonProcessingException, IOException {
        final Counter retweetAnyErrorCounter = metrics.counter(MetricsUtil.Meta.RETWEET_ANY_ERROR);
        retweetAnyErrorCounter.dec(retweetAnyErrorCounter.getCount());

        final Counter retweetOneErrorCounter = metrics.counter(MetricsUtil.Meta.RETWEET_ONE_ERROR);
        retweetOneErrorCounter.dec(retweetOneErrorCounter.getCount());

        final Counter httpErrorCounter = metrics.counter(MetricsUtil.Meta.HTTP_ERR);
        httpErrorCounter.dec(httpErrorCounter.getCount());

        final Counter httpOkCounter = metrics.counter(MetricsUtil.Meta.HTTP_OK);
        httpOkCounter.dec(httpOkCounter.getCount());

        final Counter twitterReadOkCounter = metrics.counter(MetricsUtil.Meta.TWITTER_READ_OK);
        twitterReadOkCounter.dec(twitterReadOkCounter.getCount());

        final Counter twitterWriteOkCounter = metrics.counter(MetricsUtil.Meta.TWITTER_WRITE_OK);
        twitterWriteOkCounter.dec(twitterWriteOkCounter.getCount());
    }

}
