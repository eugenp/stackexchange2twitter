package org.tweet.twitter.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.OperationNotPermittedException;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.SearchParameters;
import org.springframework.social.twitter.api.impl.SearchParameters.ResultType;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Service
public class TwitterService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterTemplateCreator twitterCreator;

    public TwitterService() {
        super();
    }

    // API

    public void tweet(final String accountName, final String tweetText) {
        final Twitter twitterTemplate = twitterCreator.getTwitterTemplate(accountName);

        try {
            twitterTemplate.timelineOperations().updateStatus(tweetText);
        } catch (final OperationNotPermittedException notPermitted) {
            // likely tracked by https://github.com/eugenp/stackexchange2twitter/issues/11
            logger.error("[TEMP] Unable to tweet on account: " + accountName + "; tweet: " + tweetText, notPermitted);

            // another possible cause: OperationNotPermittedException: Status is a duplicate
        } catch (final RuntimeException ex) {
            logger.error("Unable to tweet on account: " + accountName + "; tweet: " + tweetText, ex);
        }
    }

    // read

    public List<String> listTweetsOfAccount(final String accountName) {
        final Twitter twitterTemplate = twitterCreator.getTwitterTemplate(accountName);
        final List<Tweet> userTimeline = twitterTemplate.timelineOperations().getUserTimeline();
        final Function<Tweet, String> tweetToStringFunction = new Function<Tweet, String>() {
            @Override
            public final String apply(final Tweet input) {
                return input.getText();
            }
        };
        return Lists.transform(userTimeline, tweetToStringFunction);
    }

    /**
     * TODO: include the sinceId as a parameter
     */
    public List<Tweet> listTweetsOfHashtag(final String readOnlyAccountName, final String hashtag) {
        final Twitter twitterTemplate = twitterCreator.getTwitterTemplate(readOnlyAccountName);

        // final SearchParameters searchParameters = new SearchParameters("#" + hashtag).lang("en").count(100).includeEntities(false).resultType(ResultType.POPULAR);
        final SearchParameters searchParameters = new SearchParameters("#" + hashtag).lang("en").count(100).includeEntities(false).resultType(ResultType.MIXED);
        // final SearchParameters searchParameters = new SearchParameters("#" + hashtag).lang("en").count(100).includeEntities(false).resultType(ResultType.RECENT);
        final SearchResults search = twitterTemplate.searchOperations().search(searchParameters);
        return search.getTweets();
    }

}
