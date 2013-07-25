package org.tweet.twitter.service;

import org.common.service.LinkService;
import org.common.service.live.HttpLiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.tweet.spring.util.SpringProfileUtil;

import com.google.common.base.Preconditions;

@Service
@Profile(SpringProfileUtil.LIVE)
public class TweetLiveService {
    // private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpLiveService httpService;

    @Autowired
    private LinkService linkService;

    public TweetLiveService() {
        super();
    }

    // API

    public final String constructTweetLive(final String text, final String url) {
        Preconditions.checkNotNull(text);
        Preconditions.checkNotNull(url);

        final String expandedUrl = Preconditions.checkNotNull(httpService.expand(url));
        final String cleanExpandedUrl = linkService.removeUrlParameters(expandedUrl);

        final String textOfTweet = text;
        final String tweet = textOfTweet + " - " + cleanExpandedUrl;
        return tweet;
    }

    // util

}
