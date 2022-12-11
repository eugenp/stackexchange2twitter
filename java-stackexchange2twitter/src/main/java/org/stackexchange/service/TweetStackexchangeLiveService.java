package org.stackexchange.service;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.common.service.BaseTweetFromSourceLiveService;
import org.common.service.external.bitly.BitlyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.stackexchange.api.client.QuestionsApi;
import org.stackexchange.api.constants.StackSite;
import org.stackexchange.component.MinStackScoreRetriever;
import org.stackexchange.component.StackExchangePageStrategy;
import org.stackexchange.persistence.dao.IQuestionTweetJpaDAO;
import org.stackexchange.persistence.model.QuestionTweet;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.component.TwitterHashtagsRetriever;
import org.tweet.twitter.service.TagRetrieverService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

@Service
@Profile(SpringProfileUtil.WRITE)
public final class TweetStackexchangeLiveService extends BaseTweetFromSourceLiveService<QuestionTweet> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private QuestionsApi questionsApi;
    @Autowired
    private IQuestionTweetJpaDAO questionTweetApi;
    @Autowired
    private TagRetrieverService tagService;
    @Autowired
    private StackExchangePageStrategy pageStrategy;
    @Autowired
    private MinStackScoreRetriever minStackScoreRetriever;
    @Autowired
    private TwitterHashtagsRetriever twitterHashtagsRetriever;
    @Autowired
    private BitlyService bitlyService;

    public TweetStackexchangeLiveService() {
        super();
    }

    // API

    // write

    /**any*/
    public final boolean tweetAnyTopQuestionBySite(final StackSite stackSite, final String twitterAccount) {
        try {
            final boolean success = tweetAnyTopQuestionBySiteInternal(stackSite, twitterAccount);
            if (!success) {
                logger.warn("Unable to tweet on twitterAccount= {}, from stackSite= {}", twitterAccount, stackSite);
            }
            return success;
        } catch (final RuntimeException runtimeEx) {
            logger.error("1 - Unexpected exception when trying to tweet from stackSite= " + stackSite + " on twitterAccount= " + twitterAccount, runtimeEx);
            return false;
        } catch (final Exception ex) {
            logger.error("2 - Unexpected exception when trying to tweet from stackSite= " + stackSite + " on twitterAccount= " + twitterAccount, ex);
            return false;
        }
    }

    /**any*/
    public final boolean tweetAnyTopQuestionBySiteAndTag(final StackSite stackSite, final String twitterAccount) {
        String stackTag = null;
        try {
            stackTag = tagService.pickStackTag(twitterAccount);
            final int pageToStartWith = pageStrategy.decidePage(twitterAccount);
            final boolean success = tweetAnyTopQuestionBySiteAndTagInternal(stackSite, twitterAccount, stackTag, pageToStartWith);
            if (!success) {
                logger.warn("Unable to tweet on twitterAccount= {}, from stackSite= {}", twitterAccount, stackSite);
            }
            return success;
        } catch (final RuntimeException runtimeEx) {
            logger.error("3 - Unexpected exception when trying to tweet from stackSite= " + stackSite + " and stackTag= " + stackTag + " on twitterAccount= " + twitterAccount, runtimeEx);
            return false;
        } catch (final Exception ex) {
            logger.error("4 - Unexpected exception when trying to tweet from stackSite= " + stackSite + " and stackTag= " + stackTag + " on twitterAccount= " + twitterAccount, ex);
            return false;
        }
    }

    // util

    /**any*/
    /**
     * - not part of the API because it asks for the page
     */
    final boolean tweetAnyTopQuestionBySite(final StackSite stackSite, final String twitterAccount, final int pageToStartWith) throws JsonProcessingException, IOException {
        try {
            return tweetAnyTopQuestionBySiteInternal(stackSite, twitterAccount, pageToStartWith);
        } catch (final RuntimeException runtimeEx) {
            logger.error("5 - Unexpected exception when trying to tweet from stackSite= " + stackSite + " on twitterAccount= " + twitterAccount, runtimeEx);
            return false;
        }
    }

    /**any*/
    /**
     * - not part of the API because it asks for the tag
     */
    final boolean tweetAnyTopQuestionBySiteAndTag(final StackSite stackSite, final String stackTag, final String twitterAccount) throws JsonProcessingException, IOException {
        try {
            return tweetAnyTopQuestionBySiteAndTagInternal(stackSite, twitterAccount, stackTag);
        } catch (final RuntimeException runtimeEx) {
            logger.error("6 - Unexpected exception when trying to tweet from site=" + stackSite + " and stackTag= " + stackTag + " on twitterAccount= " + twitterAccount, runtimeEx);
            return false;
        }
    }

    /**any*/
    /**
     * - not part of the API because it asks for the tag and the page
     */
    /*0*/final boolean tweetAnyTopQuestionBySiteAndTag(final StackSite stackSite, final String stackTag, final String twitterAccount, final int pageToStartWith) throws JsonProcessingException, IOException {
        try {
            return tweetAnyTopQuestionBySiteAndTagInternal(stackSite, twitterAccount, stackTag, pageToStartWith);
        } catch (final RuntimeException runtimeEx) {
            logger.error("7 - Unexpected exception when trying to tweet from stackSite=" + stackSite + " and stackTag= " + stackTag + " on twitterAccount= " + twitterAccount, runtimeEx);
            return false;
        }
    }

    /**any*/
    private final boolean tweetAnyTopQuestionBySiteInternal(final StackSite stackSite, final String twitterAccount) throws JsonProcessingException, IOException {
        final int pageToStartWith = pageStrategy.decidePage(twitterAccount);
        return tweetAnyTopQuestionBySiteInternal(stackSite, twitterAccount, pageToStartWith);
    }

    /**any*/
    private final boolean tweetAnyTopQuestionBySiteInternal(final StackSite stackSite, final String twitterAccount, final int pageToStartWith) throws JsonProcessingException, IOException {
        logger.debug("Begin trying to tweet from stackSite= {}, on twitterAccount= {}, pageToStartWith= {}", stackSite.name(), twitterAccount, pageToStartWith);

        int currentPage = pageToStartWith;
        boolean tweetSuccessful = false;
        final int maxScoreForQuestionsOnThisAccount = minStackScoreRetriever.minScoreByAccount(twitterAccount);
        while (!tweetSuccessful) {
            logger.trace("Trying to tweeting from stackSite= {}, on twitterAccount= {}, question from page= {}", stackSite.name(), twitterAccount, currentPage);

            final String siteQuestionsRawJson = questionsApi.questions(maxScoreForQuestionsOnThisAccount, stackSite, currentPage);
            tweetSuccessful = tryAnyTweetTopQuestion(stackSite, twitterAccount, siteQuestionsRawJson);
            currentPage++;
        }

        return tweetSuccessful;
    }

    /**any*/
    private final boolean tweetAnyTopQuestionBySiteAndTagInternal(final StackSite stackSite, final String twitterAccount, final String stackTag) throws IOException, JsonProcessingException {
        final int pageToStartWith = pageStrategy.decidePage(twitterAccount);
        return tweetAnyTopQuestionBySiteAndTagInternal(stackSite, twitterAccount, stackTag, pageToStartWith);
    }

    /**any*/
    private final boolean tweetAnyTopQuestionBySiteAndTagInternal(final StackSite stackSite, final String twitterAccount, final String stackTag, final int pageToStartWith) throws IOException, JsonProcessingException {
        logger.debug("Begin trying to tweet from stackSite= {}, on twitterAccount= {}, pageToStartWith= {}", stackSite.name(), twitterAccount, pageToStartWith);

        int currentPage = pageToStartWith;
        boolean tweetSuccessful = false;
        final int maxScoreForQuestionsOnThisAccount = minStackScoreRetriever.minScore(stackTag, stackSite, twitterAccount);
        while (!tweetSuccessful) {
            logger.trace("Trying to tweeting from stackSite= {}, on twitterAccount= {}, pageToStartWith= {}", stackSite.name(), twitterAccount, pageToStartWith);

            final String questionsForTagRawJson = questionsApi.questions(maxScoreForQuestionsOnThisAccount, stackSite, stackTag, currentPage);
            tweetSuccessful = tryAnyTweetTopQuestion(stackSite, twitterAccount, questionsForTagRawJson);
            currentPage++;
        }

        return tweetSuccessful;
    }

    /**any*/
    private final boolean tryAnyTweetTopQuestion(final StackSite stackSite, final String twitterAccount, final String siteQuestionsRawJson) throws IOException, JsonProcessingException {
        final JsonNode siteQuestionsJson = new ObjectMapper().readTree(siteQuestionsRawJson);
        if (!isValidQuestions(siteQuestionsJson, twitterAccount)) {
            return false;
        }
        final ArrayNode siteQuestionsJsonArray = (ArrayNode) siteQuestionsJson.get("items");
        for (final JsonNode questionJson : siteQuestionsJsonArray) {
            final String questionId = questionJson.get(QuestionsApi.QUESTION_ID).toString();
            final String title = questionJson.get(QuestionsApi.TITLE).toString();
            final String link = questionJson.get(QuestionsApi.LINK).toString();
            logger.trace("Considering to tweet on twitterAccount= {}, questionId= {}", twitterAccount, questionId);
            if (!hasThisAlreadyBeenTweetedById(new QuestionTweet(questionId, twitterAccount, null, null))) {
                logger.debug("Attempting to tweet on twitterAccount= {}, questionId= {}", twitterAccount, questionId);
                final boolean success = tryTweetOnePrepare(title, link, questionId, stackSite, twitterAccount);
                if (!success) {
                    logger.debug("Tried and failed to tweet on twitterAccount= {}, tweet text= {}", twitterAccount, title);
                    continue;
                } else {
                    logger.info("Successfully tweeted on twitterAccount= {}, tweet text= {}", twitterAccount, title);
                    return true;
                }
            }
        }

        return false;
    }

    /**one*/
    final boolean tryTweetOnePrepare(final String textRaw, final String url, final String questionId, final StackSite site, final String twitterAccount) {
        final Map<String, Object> customDetails = Maps.newHashMap();
        customDetails.put("questionId", questionId);
        customDetails.put("site", site);

        final String urlWithNoDoubleQuotes;
        if (url.startsWith("\"") && url.endsWith("\"")) {
            urlWithNoDoubleQuotes = url.substring(1, url.length() - 1);
        } else {
            urlWithNoDoubleQuotes = url;
        }
        final String textRawWithNoDoubleQuotes;
        if (textRaw.startsWith("\"") && textRaw.endsWith("\"")) {
            textRawWithNoDoubleQuotes = textRaw.substring(1, textRaw.length() - 1);
        } else {
            textRawWithNoDoubleQuotes = textRaw;
        }
        return tryTweetOne(textRawWithNoDoubleQuotes, urlWithNoDoubleQuotes, twitterAccount, customDetails);
    }

    /**one*/
    @Override
    protected final boolean tryTweetOne(final String tweetTextRaw, final String url, final String twitterAccount, final Map<String, Object> customDetails) {
        final String questionId = (String) customDetails.get("questionId");
        final StackSite site = (StackSite) customDetails.get("site");

        // is it worth it by itself? - yes

        // is it worth it in the context of all the current list of tweets? - yes

        // pre-process
        final String cleanTweetText = tweetService.processPreValidity(tweetTextRaw);

        // post-process
        final String fullyCleanedTweetText = advancedTweetService.postValidityProcessForTweetTextNoUrl(cleanTweetText, twitterAccount);

        // construct full tweet
        final String shortUrl = bitlyService.shortenUrl(url);
        final String fullTweet = advancedTweetService.constructTweetSimple(fullyCleanedTweetText, shortUrl);

        // is it valid?
        if (!tweetService.isTweetFullValid(fullTweet)) {
            logger.debug("Tweet invalid (size, link count) on twitterAccount= {}, tweet text= {}", twitterAccount, fullTweet);
            return false;
        }

        // is this tweet pointing to something good? - yes

        // is the tweet rejected by some classifier? - no

        // tweet
        final boolean success = twitterWriteLiveService.tweet(twitterAccount, fullTweet);

        // mark
        if (success) {
            markDone(new QuestionTweet(questionId, twitterAccount, site.name(), new Date()));
        }

        // done
        return success;
    }

    // checks

    private final boolean isValidQuestions(final JsonNode siteQuestionsJson, final String twitterAccount) {
        final JsonNode items = siteQuestionsJson.get("items");
        Preconditions.checkNotNull(items, "For twitterAccount = " + twitterAccount + ", there are no items (null) in the questions json = " + siteQuestionsJson);
        Preconditions.checkState(((ArrayNode) siteQuestionsJson.get("items")).size() > 0, "For twitterAccount = " + twitterAccount + ", there are no items (empty) in the questions json = " + siteQuestionsJson);

        return true;
    }

    // template

    @Override
    protected final void markDone(final QuestionTweet entity) {
        getApi().save(entity);
    }

    @Override
    protected final boolean hasThisAlreadyBeenTweetedById(final QuestionTweet question) {
        final QuestionTweet existingTweet = getApi().findByQuestionIdAndTwitterAccount(question.getQuestionId(), question.getTwitterAccount());
        return existingTweet != null;
    }

    @Override
    protected final IQuestionTweetJpaDAO getApi() {
        return questionTweetApi;
    }

}
