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

    // 12 hours = 1000 * 60 * 60 * 12
    @Scheduled(fixedRate = 43200000)
    public void tweetStackExchangeTopQuestion() throws JsonProcessingException, IOException {
        service.tweetStackExchangeTopQuestion();
    }

}
