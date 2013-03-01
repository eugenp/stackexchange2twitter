package org.tweet.stackexchange;

import java.io.IOException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stackexchange.api.client.ApiUris;
import org.stackexchange.api.client.QuestionsApi;
import org.stackexchange.api.constants.Site;
import org.tweet.stackexchange.persistence.dao.IQuestionTweetJpaDAO;
import org.tweet.stackexchange.persistence.model.QuestionTweet;
import org.tweet.twitter.service.TwitterService;
import org.tweet.twitter.service.TwitterTemplateCreator;
import org.tweet.twitter.util.TwitterUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class TweetStackexchangeService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterTemplateCreator twitterCreator;

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
    public void tweetTopQuestionBySite(final Site site, final String accountName, final int pageToStartWith) throws JsonProcessingException, IOException {
        try {
            tweetTopQuestionBySiteInternal(site, accountName, pageToStartWith);
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception: ", runtimeEx);
        }
    }

    public void tweetTopQuestionBySiteInternal(final Site site, final String accountName, final int pageToStartWith) throws JsonProcessingException, IOException {
        logger.debug("Tweeting from site = {}, on account = {}", site.name(), accountName);

        int currentPage = pageToStartWith;
        boolean tweetSuccessful = false;
        while (!tweetSuccessful) {
            final String siteQuestionsRawJson = questionsApi.questions(70, site, currentPage);
            tweetSuccessful = tweetTopQuestion(accountName, siteQuestionsRawJson);
            currentPage++;
        }
    }

    public void tweetTopQuestionByTag(final Site site, final String accountName, final String tag, final int pageToStartWith) throws JsonProcessingException, IOException {
        logger.debug("Tweeting from site = {}, on account = {}", site.name(), accountName);

        int currentPage = pageToStartWith;
        boolean tweetSuccessful = false;
        while (!tweetSuccessful) {
            final String questionsUriForTag = ApiUris.getTagUri(70, site, tag, currentPage);
            final String questionsForTagRawJson = questionsApi.questions(70, questionsUriForTag);
            tweetSuccessful = tweetTopQuestion(accountName, questionsForTagRawJson);
            currentPage++;
        }
    }

    // util

    private boolean tweetTopQuestion(final String accountName, final String siteQuestionsRawJson) throws IOException, JsonProcessingException {
        final JsonNode siteQuestionsJson = new ObjectMapper().readTree(siteQuestionsRawJson);
        final ArrayNode siteQuestionsJsonArray = (ArrayNode) siteQuestionsJson.get("items");
        for (final JsonNode questionJson : siteQuestionsJsonArray) {
            final String questionId = questionJson.get(QuestionsApi.QUESTION_ID).toString();
            final String title = questionJson.get(QuestionsApi.TITLE).toString();
            final String link = questionJson.get(QuestionsApi.LINK).toString();

            logger.debug("Considering to tweet on account= {}, Question= {}", accountName, questionId);

            if (!hasThisQuestionAlreadyBeenTweeted(questionId)) {
                logger.info("Tweeting Question: title= {} with id= {}", title, questionId);

                final boolean success = tryTweet(title, link, accountName);
                if (!success) {
                    logger.debug("Tried and failed to tweet on account= {}, tweet text= {}", accountName, title);
                    continue;
                }
                markQuestionTweeted(questionId, accountName);
                return true;
            }
        }

        return false;
    }

    private final boolean hasThisQuestionAlreadyBeenTweeted(final String questionId) {
        final QuestionTweet existingTweet = questionTweetApi.findByQuestionId(questionId);
        return existingTweet != null;
    }

    private final boolean tryTweet(final String title, final String link, final String accountName) {
        final String text = StringEscapeUtils.unescapeHtml4(title);
        if (!TwitterUtil.isTweetValid(text)) {
            return false;
        }

        final String fullTweet = TwitterUtil.prepareTweet(text.substring(1, text.length() - 1), link.substring(1, link.length() - 1));

        twitterService.tweet(twitterCreator.getTwitterTemplate(accountName), fullTweet);
        return true;
    }

    private final void markQuestionTweeted(final String questionId, final String accountName) {
        final QuestionTweet questionTweet = new QuestionTweet(questionId, accountName);
        questionTweetApi.save(questionTweet);
    }

}
