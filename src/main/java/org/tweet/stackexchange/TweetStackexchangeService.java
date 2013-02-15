package org.tweet.stackexchange;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.stackexchange.api.client.QuestionsApi;
import org.tweet.twitter.TwitterService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class TweetStackexchangeService {

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private QuestionsApi questionsApi;

    public TweetStackexchangeService() {
        super();
    }

    // API

    // 6 hours
    @Scheduled(fixedRate = 21600000)
    public void tweetStackExchangeTopQuestion() throws JsonProcessingException, IOException {
        final String questionsAsJson = questionsApi.questions(100);
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode rootNode = mapper.readTree(questionsAsJson);
        final ArrayNode questionsArray = (ArrayNode) rootNode.get("items");
    }

}
