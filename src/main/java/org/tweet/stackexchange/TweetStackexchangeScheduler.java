package org.tweet.stackexchange;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class TweetStackexchangeScheduler {

    @Autowired
    private TweetStackexchangeService service;

    public TweetStackexchangeScheduler() {
        super();
    }

    // API

    // 6 hours
    @Scheduled(fixedRate = 21600000)
    public void tweetStackExchangeTopQuestion() throws JsonProcessingException, IOException {
        service.tweetStackExchangeTopQuestion();
    }

}
