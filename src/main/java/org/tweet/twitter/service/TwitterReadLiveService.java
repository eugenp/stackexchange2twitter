package org.tweet.twitter.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.SearchParameters;
import org.springframework.social.twitter.api.impl.SearchParameters.ResultType;
import org.springframework.stereotype.Service;
import org.stackexchange.util.GenericUtil;
import org.stackexchange.util.TwitterAccountEnum;

import com.google.api.client.util.Preconditions;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Service
public class TwitterReadLiveService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterTemplateCreator twitterCreator;

    public TwitterReadLiveService() {
        super();
    }

    // API

    // user profiles

    /**
     * - note: will NOT return null
     */
    public TwitterProfile getProfileOfUser(final String userHandle) {
        final String randomAccount = GenericUtil.pickOneGeneric(TwitterAccountEnum.values()).name();
        final Twitter readOnlyTwitterTemplate = twitterCreator.getTwitterTemplate(randomAccount);
        final TwitterProfile userProfile = readOnlyTwitterTemplate.userOperations().getUserProfile(userHandle);
        return Preconditions.checkNotNull(userProfile);
    }

    // by internal accounts

    /**
     * - note: will NOT return null
     */
    public List<String> listTweetsOfInternalAccount(final String twitterAccount) {
        try {
            return listTweetsOfInternalAccountInternal(twitterAccount);
        } catch (final RuntimeException ex) {
            logger.error("Unable to list tweets on twitterAccount= " + twitterAccount, ex);
            return Lists.newArrayList();
        }
    }

    /**
     * - note: will NOT return null
     */
    public List<String> listTweetsOfInternalAccountInternal(final String twitterAccount) {
        return listTweetsOfInternalAccount(twitterAccount, 20);
    }

    /**
     * - note: will NOT return null
     */
    public List<String> listTweetsOfInternalAccount(final String twitterAccount, final int howmany) {
        try {
            return listTweetsOfInternalAccountInternal(twitterAccount, howmany);
        } catch (final RuntimeException ex) {
            logger.error("Unable to list tweets on twitterAccount= " + twitterAccount, ex);
            return Lists.newArrayList();
        }
    }

    /**
     * - note: will NOT return null
     */
    public List<String> listTweetsOfInternalAccountInternal(final String twitterAccount, final int howmany) {
        final List<Tweet> userTimeline = listTweetsOfInternalAccountRaw(twitterAccount, howmany);
        final Function<Tweet, String> tweetToStringFunction = new Function<Tweet, String>() {
            @Override
            public final String apply(final Tweet input) {
                return input.getText();
            }
        };
        return Lists.transform(userTimeline, tweetToStringFunction);
    }

    public List<Tweet> listTweetsOfInternalAccountRaw(final String twitterAccount, final int howmany) {
        try {
            return listTweetsOfInternalAccountRawInternal(twitterAccount, howmany);
        } catch (final RuntimeException ex) {
            logger.error("Unable to list tweets on twitterAccount= " + twitterAccount, ex);
            return Lists.newArrayList();
        }
    }

    public List<Tweet> listTweetsOfInternalAccountRawInternal(final String twitterAccount, final int howmany) {
        final Twitter twitterTemplate = twitterCreator.getTwitterTemplate(twitterAccount);
        final List<Tweet> userTimeline = twitterTemplate.timelineOperations().getUserTimeline(howmany);
        return userTimeline;
    }

    // by external accounts

    public List<Tweet> listTweetsOfAccount(final String twitterAccount, final int howmany) {
        try {
            return listTweetsOfAccountInternal(twitterAccount, howmany);
        } catch (final RuntimeException ex) {
            logger.error("Unable to list tweets on twitterAccount= " + twitterAccount, ex);
            return Lists.newArrayList();
        }
    }

    public List<Tweet> listTweetsOfAccountInternal(final String twitterAccount, final int howmany) {
        final String randomAccount = GenericUtil.pickOneGeneric(TwitterAccountEnum.values()).name();
        final Twitter readOnlyTwitterTemplate = twitterCreator.getTwitterTemplate(randomAccount);
        final List<Tweet> userTimeline = readOnlyTwitterTemplate.timelineOperations().getUserTimeline(twitterAccount, howmany);
        return userTimeline;
    }

    // by hashtag

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

    // internal API

    public final Twitter readOnlyTwitterApi() {
        final String randomAccount = GenericUtil.pickOneGeneric(TwitterAccountEnum.values()).name();
        final Twitter readOnlyTwitterTemplate = twitterCreator.getTwitterTemplate(randomAccount);
        return readOnlyTwitterTemplate;
    }

}
