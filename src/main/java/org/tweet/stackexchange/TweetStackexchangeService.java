package org.tweet.stackexchange;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stackexchange.api.client.QuestionsApi;
import org.tweet.stackexchange.persistence.dao.IQuestionTweetJpaDAO;
import org.tweet.stackexchange.persistence.model.QuestionTweet;
import org.tweet.twitter.TwitterService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class TweetStackexchangeService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private QuestionsApi questionsApi;

    @Autowired
    private IQuestionTweetJpaDAO questionTweetApi;

    public TweetStackexchangeService() {
        super();
    }

    // API

    public void tweetStackExchangeTopQuestion() throws JsonProcessingException, IOException {
        final String questionsAsJson = questionsApi.questions(100);

        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode rootNode = mapper.readTree(questionsAsJson);
        final ArrayNode questionsArray = (ArrayNode) rootNode.get("items");
        for (final JsonNode question : questionsArray) {
            logger.debug("Considering to tweet question with id=" + question.get("question_id"));
            if (!hasThisQuestionAlreadyBeenTweeted(question)) {
                logger.info("Tweeting Question: title= {} with id= {}", question.get("title"), question.get("question_id"));
                tweet(question);
                break;
            }
        }
    }

    // util

    private boolean hasThisQuestionAlreadyBeenTweeted(final JsonNode question) {
        final String questionId = question.get("question_id").toString();
        final QuestionTweet existingTweet = questionTweetApi.findByQuestionId(questionId);

        return existingTweet != null;
    }

    private void tweet(final JsonNode question) {
        final String title = question.get("title").toString();
        final String link = question.get("link").toString();
        final String fullTweet = title.subSequence(1, title.length() - 1) + " - " + link.subSequence(1, link.length() - 1);

        twitterService.tweet(fullTweet);
    }

}
