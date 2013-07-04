package org.stackexchange.service;

import java.io.IOException;
import java.util.List;

import org.common.text.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.stackexchange.api.client.QuestionsApi;
import org.stackexchange.api.constants.StackSite;
import org.stackexchange.persistence.dao.IQuestionTweetJpaDAO;
import org.stackexchange.persistence.model.QuestionTweet;
import org.stackexchange.strategies.StackExchangePageStrategy;
import org.tweet.twitter.service.TagService;
import org.tweet.twitter.service.TwitterService;
import org.tweet.twitter.util.TwitterUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

@Service
public class TweetStackexchangeService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private QuestionsApi questionsApi;

    @Autowired
    private IQuestionTweetJpaDAO questionTweetApi;

    @Autowired
    private TagService tagService;

    @Autowired
    private StackExchangePageStrategy pageStrategy;

    @Autowired
    private Environment env;

    public TweetStackexchangeService() {
        super();
    }

    // API

    // write

    public void tweetTopQuestionBySite(final StackSite site, final String twitterAccount) throws JsonProcessingException, IOException {
        try {
            tweetTopQuestionBySiteInternal(site, twitterAccount);
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to tweet from site= " + site + " on account= " + twitterAccount, runtimeEx);
        }
    }

    public void tweetTopQuestionBySiteAndTag(final StackSite site, final String twitterAccount) throws JsonProcessingException, IOException {
        try {
            tweetTopQuestionBySiteAndTagInternal(site, twitterAccount);
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to tweet from site= " + site + " on account= " + twitterAccount, runtimeEx);
        }
    }

    // util

    /**
     * - not part of the API because it asks for the page
     */
    void tweetTopQuestionBySite(final StackSite site, final String twitterAccount, final int pageToStartWith) throws JsonProcessingException, IOException {
        try {
            tweetTopQuestionBySiteInternal(site, twitterAccount, pageToStartWith);
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to tweet from site= " + site + " on account= " + twitterAccount, runtimeEx);
        }
    }

    /**
     * - not part of the API because it asks for the tag
     */
    final void tweetTopQuestionBySiteAndTag(final StackSite site, final String stackTag, final String twitterAccount) throws JsonProcessingException, IOException {
        try {
            tweetTopQuestionBySiteAndTagInternal(site, twitterAccount, stackTag);
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to tweet from site=" + site + " and stackTag= " + stackTag + " on account= " + twitterAccount, runtimeEx);
        }
    }

    /**
     * - not part of the API because it asks for the tag and the page
     */
    /*0*/final void tweetTopQuestionBySiteAndTag(final StackSite site, final String stackTag, final String twitterAccount, final int pageToStartWith) throws JsonProcessingException, IOException {
        try {
            tweetTopQuestionBySiteAndTagInternal(site, twitterAccount, stackTag, pageToStartWith);
        } catch (final RuntimeException runtimeEx) {
            logger.error("Unexpected exception when trying to tweet from site=" + site + " and stackTag= " + stackTag + " on account= " + twitterAccount, runtimeEx);
        }
    }

    final void tweetTopQuestionBySiteInternal(final StackSite site, final String twitterAccount) throws JsonProcessingException, IOException {
        final int pageToStartWith = pageStrategy.decidePage(twitterAccount);
        tweetTopQuestionBySiteInternal(site, twitterAccount, pageToStartWith);
    }

    final void tweetTopQuestionBySiteInternal(final StackSite site, final String twitterAccount, final int pageToStartWith) throws JsonProcessingException, IOException {
        logger.debug("Begin trying to tweet from site = {}, on account = {}, pageToStartWith = {}", site.name(), twitterAccount, pageToStartWith);

        int currentPage = pageToStartWith;
        boolean tweetSuccessful = false;
        while (!tweetSuccessful) {
            logger.trace("Trying to tweeting from site = {}, on account = {}, question from page = {}", site.name(), twitterAccount, currentPage);
            final int maxScoreForQuestionsOnThisAccount = env.getProperty(twitterAccount + ".minscore", Integer.class);
            final String siteQuestionsRawJson = questionsApi.questions(maxScoreForQuestionsOnThisAccount, site, currentPage);
            tweetSuccessful = tryTweetTopQuestion(site, twitterAccount, siteQuestionsRawJson);
            currentPage++;
        }
    }

    final void tweetTopQuestionBySiteAndTagInternal(final StackSite site, final String twitterAccount) throws IOException, JsonProcessingException {
        final int pageToStartWith = pageStrategy.decidePage(twitterAccount);
        final String stackTag = tagService.pickStackTagForAccount(twitterAccount);
        tweetTopQuestionBySiteAndTagInternal(site, twitterAccount, stackTag, pageToStartWith);
    }

    final void tweetTopQuestionBySiteAndTagInternal(final StackSite site, final String twitterAccount, final String stackTag) throws IOException, JsonProcessingException {
        final int pageToStartWith = pageStrategy.decidePage(twitterAccount);
        tweetTopQuestionBySiteAndTagInternal(site, twitterAccount, stackTag, pageToStartWith);
    }

    final void tweetTopQuestionBySiteAndTagInternal(final StackSite site, final String twitterAccount, final String stackTag, final int pageToStartWith) throws IOException, JsonProcessingException {
        logger.debug("Begin trying to tweet from site = {}, on account = {}, pageToStartWith = {}", site.name(), twitterAccount, pageToStartWith);

        int currentPage = pageToStartWith;
        boolean tweetSuccessful = false;
        while (!tweetSuccessful) {
            logger.trace("Trying to tweeting from site = {}, on account = {}, pageToStartWith = {}", site.name(), twitterAccount, pageToStartWith);
            Preconditions.checkNotNull(env.getProperty(twitterAccount + ".minscore"), "Unable to find minscore for twitterAccount= " + twitterAccount);
            final int maxScoreForQuestionsOnThisAccount = env.getProperty(twitterAccount + ".minscore", Integer.class);
            final String questionsForTagRawJson = questionsApi.questions(maxScoreForQuestionsOnThisAccount, site, stackTag, currentPage);
            tweetSuccessful = tryTweetTopQuestion(site, twitterAccount, questionsForTagRawJson);
            currentPage++;
        }
    }

    private final boolean tryTweetTopQuestion(final StackSite site, final String twitterAccountName, final String siteQuestionsRawJson) throws IOException, JsonProcessingException {
        final JsonNode siteQuestionsJson = new ObjectMapper().readTree(siteQuestionsRawJson);
        if (!isValidQuestions(siteQuestionsJson, twitterAccountName)) {
            return false;
        }
        final ArrayNode siteQuestionsJsonArray = (ArrayNode) siteQuestionsJson.get("items");
        for (final JsonNode questionJson : siteQuestionsJsonArray) {
            final String questionId = questionJson.get(QuestionsApi.QUESTION_ID).toString();
            final String title = questionJson.get(QuestionsApi.TITLE).toString();
            final String link = questionJson.get(QuestionsApi.LINK).toString();

            logger.trace("Considering to tweet on account= {}, Question= {}", twitterAccountName, questionId);
            if (hasThisQuestionAlreadyBeenTweeted(questionId)) {
                return false;
            }

            logger.info("Tweeting Question: title= {} with id= {}", title, questionId);
            final boolean success = tryTweet(title, link, twitterAccountName);
            if (!success) {
                logger.debug("Tried and failed to tweet on account= {}, tweet text= {}", twitterAccountName, title);
                continue;
            }
            markQuestionTweeted(site, questionId, twitterAccountName);
            return true;
        }

        return false;
    }

    private final boolean hasThisQuestionAlreadyBeenTweeted(final String questionId) {
        final QuestionTweet existingTweet = questionTweetApi.findByQuestionId(questionId);
        return existingTweet != null;
    }

    private final boolean tryTweet(final String text, final String link, final String accountName) {
        final String tweetText = preValidityProcess(text);

        // is it valid?
        if (!TwitterUtil.isTweetTextValid(tweetText)) {
            logger.debug("Tweet invalid (size, link count) on account= {}, tweet text= {}", accountName, tweetText);
            return false;
        }

        String fullTweet = TwitterUtil.prepareTweet(text.substring(1, text.length() - 1), link.substring(1, link.length() - 1));
        fullTweet = TwitterUtil.hashtagWords(fullTweet, twitterTagsToHash(accountName));

        twitterService.tweet(accountName, fullTweet);
        return true;
    }

    private String preValidityProcess(final String title) {
        return TextUtils.preProcessTweetText(title);
    }

    private final void markQuestionTweeted(final StackSite site, final String questionId, final String accountName) {
        // TODO: add site to the question tweet entity
        final QuestionTweet questionTweet = new QuestionTweet(questionId, accountName, site.name());
        questionTweetApi.save(questionTweet);
    }

    private final boolean isValidQuestions(final JsonNode siteQuestionsJson, final String accountName) {
        final JsonNode items = siteQuestionsJson.get("items");
        Preconditions.checkNotNull(items, "For accountName = " + accountName + ", there are no items (null) in the questions json = " + siteQuestionsJson);
        Preconditions.checkState(((ArrayNode) siteQuestionsJson.get("items")).size() > 0, "For accountName = " + accountName + ", there are no items (empty) in the questions json = " + siteQuestionsJson);

        return true;
    }

    private final List<String> twitterTagsToHash(final String accountName) {
        final String wordsToHashForAccount = Preconditions.checkNotNull(env.getProperty(accountName + ".hash"), "No words to hash for account: " + accountName);
        final Iterable<String> split = Splitter.on(',').split(wordsToHashForAccount);
        return Lists.newArrayList(split);
    }

}
