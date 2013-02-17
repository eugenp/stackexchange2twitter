package org.tweet.stackexchange;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stackexchange.api.client.QuestionsApi;
import org.stackexchange.api.constants.Site;
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
        final String siteQuestionsRawJson = questionsApi.questions(100, Site.serverfault);

        final JsonNode siteQuestionsJson = new ObjectMapper().readTree(siteQuestionsRawJson);
        final ArrayNode siteQuestionsJsonArray = (ArrayNode) siteQuestionsJson.get("items");
        for (final JsonNode questionJson : siteQuestionsJsonArray) {
            logger.debug("Considering to tweet question with id=" + questionJson.get(QuestionsApi.QUESTION_ID));
            if (!hasThisQuestionAlreadyBeenTweeted(questionJson)) {
                logger.info("Tweeting Question: title= {} with id= {}", questionJson.get(QuestionsApi.TITLE), questionJson.get(QuestionsApi.QUESTION_ID));
                tweet(questionJson);
                markQuestionTweeted(questionJson);
                break;
            }
        }
    }

    // util

    private final boolean hasThisQuestionAlreadyBeenTweeted(final JsonNode question) {
        final String questionId = question.get(QuestionsApi.QUESTION_ID).toString();
        final QuestionTweet existingTweet = questionTweetApi.findByQuestionId(questionId);

        return existingTweet != null;
    }

    private final void tweet(final JsonNode question) {
        final String title = question.get(QuestionsApi.TITLE).toString();
        final String link = question.get(QuestionsApi.LINK).toString();
        final String fullTweet = title.subSequence(1, title.length() - 1) + " - " + link.subSequence(1, link.length() - 1);

        twitterService.tweet(fullTweet);
    }

    private final void markQuestionTweeted(final JsonNode question) {
        final String questionId = question.get(QuestionsApi.QUESTION_ID).toString();
        final QuestionTweet questionTweet = new QuestionTweet(questionId);
        questionTweetApi.save(questionTweet);
    }

}
