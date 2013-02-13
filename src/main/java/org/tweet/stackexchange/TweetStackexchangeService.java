package org.tweet.stackexchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.tweet.twitter.TwitterService;

@Service
public class TweetStackexchangeService {

    @Autowired
    private TwitterService twitterService;

    public TweetStackexchangeService() {
        super();
    }

    // API

    // 6 hours
    @Scheduled(fixedRate = 21600000)
    public void tweetStackExchangeTopQuestion() {
        //
    }

}
