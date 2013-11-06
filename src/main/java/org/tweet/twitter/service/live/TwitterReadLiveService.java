package org.tweet.twitter.service.live;

import java.util.List;

import org.common.metrics.MetricsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.twitter.api.SearchOperations;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchParameters.ResultType;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;
import org.stackexchange.util.GenericUtil;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TwitterTemplateCreator;

import com.codahale.metrics.MetricRegistry;
import com.google.api.client.util.Preconditions;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Service
@Profile(SpringProfileUtil.LIVE)
public class TwitterReadLiveService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterTemplateCreator twitterCreator;

    @Autowired
    private MetricRegistry metrics;

    public TwitterReadLiveService() {
        super();
    }

    // API

    // tweets - from internal accounts

    /**
     * - note: will NOT return null
     */
    public List<String> listTweetsOfInternalAccount(final String twitterAccount) {
        try {
            return listTweetsOfInternalAccountInternal(twitterAccount);
        } catch (final RuntimeException ex) {
            metrics.counter(MetricsUtil.Meta.TWITTER_READ_ERR).inc();
            logger.error("1 - Unable to list tweets on twitterAccount= " + twitterAccount, ex);
            return Lists.newArrayList();
        }
    }

    /**
     * - note: will NOT return null
     */
    private final List<String> listTweetsOfInternalAccountInternal(final String twitterAccount) {
        return listTweetsOfInternalAccount(twitterAccount, 20);
    }

    /**
     * - note: will NOT return null
     */
    public List<String> listTweetsOfInternalAccount(final String twitterAccount, final int howmany) {
        try {
            return listTweetsOfInternalAccountInternal(twitterAccount, howmany);
        } catch (final RuntimeException ex) {
            metrics.counter(MetricsUtil.Meta.TWITTER_READ_ERR).inc();
            logger.error("2 - Unable to list tweets on twitterAccount= " + twitterAccount, ex);
            return Lists.newArrayList();
        }
    }

    /**
     * - note: will NOT return null
     */
    private final List<String> listTweetsOfInternalAccountInternal(final String twitterAccount, final int howmany) {
        final List<Tweet> userTimeline = listTweetsOfInternalAccountRaw(twitterAccount, howmany);
        return Lists.transform(userTimeline, new TweetToStringFunction());
    }

    public List<Tweet> listTweetsOfInternalAccountRaw(final String twitterAccount, final int howmany) {
        try {
            return listTweetsOfInternalAccountRawInternal(twitterAccount, howmany);
        } catch (final RuntimeException ex) {
            metrics.counter(MetricsUtil.Meta.TWITTER_READ_ERR).inc();
            logger.error("3 - Unable to list tweets on twitterAccount= " + twitterAccount, ex);
            return Lists.newArrayList();
        }
    }

    private final List<Tweet> listTweetsOfInternalAccountRawInternal(final String twitterAccount, final int howmany) {
        final Twitter twitterTemplate = twitterCreator.createTwitterTemplate(twitterAccount);

        final List<Tweet> userTimeline = twitterTemplate.timelineOperations().getUserTimeline(howmany);
        metrics.counter(MetricsUtil.Meta.TWITTER_READ_OK).inc();

        return userTimeline;
    }

    // tweets - from external accounts

    public List<String> listTweetsOfAccount(final String twitterAccount, final int howmany) {
        try {
            return listTweetsOfAccountInternal(twitterAccount, howmany);
        } catch (final RuntimeException ex) {
            metrics.counter(MetricsUtil.Meta.TWITTER_READ_ERR).inc();
            logger.error("4 - Unable to list tweets on twitterAccount= " + twitterAccount, ex);
            return Lists.newArrayList();
        }
    }

    public List<Tweet> listTweetsOfAccountRaw(final String twitterAccount, final int howmany) {
        try {
            return listTweetsOfAccountRawInternal(twitterAccount, howmany);
        } catch (final RuntimeException ex) {
            metrics.counter(MetricsUtil.Meta.TWITTER_READ_ERR).inc();
            logger.error("5 - Unable to list tweets on twitterAccount= " + twitterAccount, ex);
            return Lists.newArrayList();
        }
    }

    private final List<String> listTweetsOfAccountInternal(final String twitterAccount, final int howmany) {
        final List<Tweet> rawTweets = listTweetsOfAccountRawInternal(twitterAccount, howmany);
        final Function<Tweet, String> tweetToStringFunction = new TweetToStringFunction();
        return Lists.transform(rawTweets, tweetToStringFunction);
    }

    private final List<Tweet> listTweetsOfAccountRawInternal(final String twitterAccount, final int howmany) {
        final String randomAccount = GenericUtil.pickOneGeneric(TwitterAccountEnum.values()).name();
        final Twitter readOnlyTwitterTemplate = twitterCreator.createTwitterTemplate(randomAccount);

        final List<Tweet> userTimeline = readOnlyTwitterTemplate.timelineOperations().getUserTimeline(twitterAccount, howmany);
        metrics.counter(MetricsUtil.Meta.TWITTER_READ_OK).inc();

        return userTimeline;
    }

    // tweets - multi-request

    public List<Tweet> listTweetsOfAccountMultiRequestRaw(final String twitterAccount, final int howManyPages) {
        try {
            return listTweetsOfAccountMultiRequestRawInternal(twitterAccount, howManyPages);
        } catch (final RuntimeException ex) {
            metrics.counter(MetricsUtil.Meta.TWITTER_READ_ERR).inc();
            if (ex instanceof NotAuthorizedException) {
                // keep at warn or below - no need to know when this happens all the time
                logger.warn("2 - Known reason - Unable to retrieve profile of user: " + twitterAccount, ex);
                return Lists.newArrayList();
            }
            logger.error("6 - Unable to list tweets on twitterAccount= " + twitterAccount, ex);
            return Lists.newArrayList();
        }
    }

    private final List<Tweet> listTweetsOfAccountMultiRequestRawInternal(final String twitterAccount, final int howManyPages) {
        if (howManyPages <= 1) {
            return listTweetsOfAccountRawInternal(twitterAccount, 200);
        }
        if (howManyPages > 20) {
            throw new IllegalStateException();
        }

        final int reqCount = howManyPages;
        int pageIndex = reqCount;

        final String randomAccount = GenericUtil.pickOneGeneric(TwitterAccountEnum.values()).name();
        final Twitter readOnlyTwitterTemplate = twitterCreator.createTwitterTemplate(randomAccount);
        final TimelineOperations timelineOperations = readOnlyTwitterTemplate.timelineOperations();

        final List<Tweet> collector = Lists.newArrayList();
        List<Tweet> currentPage = timelineOperations.getUserTimeline(twitterAccount, 200);
        metrics.counter(MetricsUtil.Meta.TWITTER_READ_OK).inc();

        collector.addAll(currentPage);
        if (collector.size() < 200) {
            // done - under 200 tweets (1 page)
            return collector;
        }
        long lastId = currentPage.get(currentPage.size() - 1).getId();
        while (pageIndex > 1) {
            currentPage = timelineOperations.getUserTimeline(twitterAccount, 200, 01, lastId);
            metrics.counter(MetricsUtil.Meta.TWITTER_READ_OK).inc();

            collector.addAll(currentPage);
            if (currentPage.isEmpty()) {
                logger.error("This should not happen but weirdly does when retrieving {} pages for twitterAccount= {}", howManyPages, twitterAccount);
                return collector;
            }
            lastId = currentPage.get(currentPage.size() - 1).getId();
            pageIndex--;
        }

        return collector;
    }

    public List<Tweet> listTweetsByHashtagMultiRequestRaw(final String hashtag, final int howManyPages) {
        try {
            return listTweetsByHashtagMultiRequestRawInternal(hashtag, howManyPages);
        } catch (final RuntimeException ex) {
            metrics.counter(MetricsUtil.Meta.TWITTER_READ_ERR).inc();
            logger.error("7 - Unable to list tweets by hashtag= " + hashtag, ex);
            return Lists.newArrayList();
        }
    }

    private final List<Tweet> listTweetsByHashtagMultiRequestRawInternal(final String hashtag, final int howManyPages) {
        Preconditions.checkState(howManyPages > 0);
        if (howManyPages == 1) {
            return listTweetsByHashtag(hashtag);
        }
        if (howManyPages > 20) {
            throw new IllegalStateException();
        }

        final int reqCount = howManyPages;
        int pageIndex = reqCount;

        final String randomAccount = GenericUtil.pickOneGeneric(TwitterAccountEnum.values()).name();
        final Twitter readOnlyTwitterTemplate = twitterCreator.createTwitterTemplate(randomAccount);
        final SearchOperations searchOperations = readOnlyTwitterTemplate.searchOperations();

        final List<Tweet> collector = Lists.newArrayList();

        SearchParameters searchParameters = new SearchParameters(hashtag.trim()).lang("en").count(100).includeEntities(true).resultType(ResultType.MIXED);
        List<Tweet> currentPage = searchOperations.search(searchParameters).getTweets();
        metrics.counter(MetricsUtil.Meta.TWITTER_READ_OK).inc();

        collector.addAll(currentPage);
        if (collector.size() < 100) {
            // done - under 100 tweets (1 page)
            return collector;
        }
        long lastId = currentPage.get(currentPage.size() - 1).getId();
        while (pageIndex > 1) {
            searchParameters = new SearchParameters(hashtag.trim()).lang("en").count(100).maxId(lastId).includeEntities(true).resultType(ResultType.MIXED);
            currentPage = searchOperations.search(searchParameters).getTweets(); // timelineOperations.getUserTimeline(twitterAccount, 200, 01, lastId);
            metrics.counter(MetricsUtil.Meta.TWITTER_READ_OK).inc();

            collector.addAll(currentPage);
            if (currentPage.isEmpty()) {
                logger.error("This should not happen but weirdly does when retrieving {} pages for hashtag= {}", howManyPages, hashtag);
                return collector;
            }
            lastId = currentPage.get(currentPage.size() - 1).getId();
            pageIndex--;
        }

        return collector;
    }

    // tweets - by hashtag

    public List<Tweet> listTweetsByHashtag(final String hashtag) {
        final String randomAccount = GenericUtil.pickOneGeneric(TwitterAccountEnum.values()).name();
        return listTweetsByHashtag(randomAccount, hashtag);
    }

    public List<Tweet> listTweetsByHashtag(final String readOnlyAccountName, final String hashtag) {
        final Twitter twitterTemplate = twitterCreator.createTwitterTemplate(readOnlyAccountName);

        // ruby_rails
        final StringBuilder param = new StringBuilder();
        if (hashtag.contains("_")) {
            final String[] hashtags = hashtag.split("_");
            for (final String hashtagIndividual : hashtags) {
                param.append("#").append(hashtagIndividual).append(" ");
            }
        } else {
            param.append("#").append(hashtag);
        }

        final SearchParameters searchParameters = new SearchParameters(param.toString().trim()).lang("en").count(100).includeEntities(true).resultType(ResultType.MIXED);
        final SearchResults search = twitterTemplate.searchOperations().search(searchParameters);
        metrics.counter(MetricsUtil.Meta.TWITTER_READ_OK).inc();

        return search.getTweets();
    }

    public List<Tweet> listTweetsByWord(final String readOnlyAccountName, final String word) {
        final Twitter twitterTemplate = twitterCreator.createTwitterTemplate(readOnlyAccountName);

        // ruby_rails
        final StringBuilder param = new StringBuilder();
        if (word.contains("_")) {
            final String[] words = word.split("_");
            for (final String wordIndividual : words) {
                param.append(wordIndividual).append(" ");
            }
        } else {
            param.append(word);
        }

        final SearchParameters searchParameters = new SearchParameters(param.toString().trim()).lang("en").count(100).includeEntities(true).resultType(ResultType.MIXED);
        final SearchResults search = twitterTemplate.searchOperations().search(searchParameters);
        metrics.counter(MetricsUtil.Meta.TWITTER_READ_OK).inc();

        return search.getTweets();
    }

    // tweets - single one

    public Tweet findOne(final long id) {
        return readOnlyTwitterApi().timelineOperations().getStatus(id);
    }

    // internal API

    public final Twitter readOnlyTwitterApi() {
        final String randomAccount = GenericUtil.pickOneGeneric(TwitterAccountEnum.values()).name();
        final Twitter readOnlyTwitterTemplate = twitterCreator.createTwitterTemplate(randomAccount);
        return readOnlyTwitterTemplate;
    }

    public final Twitter readOnlyTwitterApi(final String twitterAccount) {
        final Twitter readOnlyTwitterTemplate = twitterCreator.createTwitterTemplate(twitterAccount);
        return readOnlyTwitterTemplate;
    }

}
