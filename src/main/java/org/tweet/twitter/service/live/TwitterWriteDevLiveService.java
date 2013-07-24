package org.tweet.twitter.service.live;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.tweet.spring.util.SpringProfileUtil;

@Service
@Profile(SpringProfileUtil.DEV)
public class TwitterWriteDevLiveService implements ITwitterWriteLiveService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public TwitterWriteDevLiveService() {
        super();
    }

    // API

    // write

    @Override
    public boolean retweet(final String twitterAccount, final long tweetId) {
        logger.warn("Simmulating retweet on twitterAccount= {} or tweetId= {}", twitterAccount, tweetId);
        return true;
    }

    @Override
    public boolean tweet(final String twitterAccount, final String tweetText) {
        logger.warn("Simmulating tweet on twitterAccount= {} or tweetText= {}", twitterAccount, tweetText);
        return true;
    }

}
