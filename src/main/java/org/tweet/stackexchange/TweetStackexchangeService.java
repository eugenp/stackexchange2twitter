package org.tweet.stackexchange;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TweetStackexchangeService {

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
