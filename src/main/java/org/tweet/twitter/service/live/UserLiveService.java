package org.tweet.twitter.service.live;

import java.util.List;
import java.util.Set;

import org.common.metrics.MetricsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.InternalServerErrorException;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.social.SocialException;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.FriendOperations;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.UserOperations;
import org.springframework.stereotype.Service;
import org.stackexchange.util.GenericUtil;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TwitterTemplateCreator;

import com.codahale.metrics.MetricRegistry;
import com.google.api.client.util.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Service
@Profile(SpringProfileUtil.LIVE)
public class UserLiveService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterTemplateCreator twitterCreator;

    @Autowired
    private MetricRegistry metrics;

    // API

    // follow

    public final void followUser(final String myAccount, final String screenName) {
        final Twitter readOnlyTwitterTemplate = twitterCreator.createTwitterTemplate(myAccount);

        readOnlyTwitterTemplate.friendOperations().follow(screenName);

        logger.info("My account: {} just followed account: {}", myAccount, screenName);
    }

    // user profiles - single

    /**
     * - note: will NOT return null
     */
    public TwitterProfile getProfileOfUser(final String userHandle) {
        try {
            return getProfileOfUserInternal(userHandle);
        } catch (final SocialException socialEx) {
            metrics.counter(MetricsUtil.Meta.TWITTER_READ_ERR).inc();

            final Throwable cause = socialEx.getCause();
            if (cause != null && cause instanceof InternalServerErrorException) {
                // keep at warn or below - no need to know when this happens all the time
                logger.warn("1 - Known reason - Unable to retrieve profile of user: " + userHandle, socialEx);
                return null;
            }
            if (socialEx instanceof ResourceNotFoundException) {
                // keep at warn or below - no need to know when this happens all the time
                logger.warn("Known reason - User no longer exists: " + userHandle, socialEx);
                return null;
            }

            logger.error("Unable to retrieve profile of user: " + userHandle, socialEx);
            return null;
        } catch (final RuntimeException ex) {
            metrics.counter(MetricsUtil.Meta.TWITTER_READ_ERR).inc();
            logger.error("Unable to retrieve profile of user: " + userHandle, ex);
            return null;
        }
    }

    private final TwitterProfile getProfileOfUserInternal(final String userHandle) {
        final String randomAccount = GenericUtil.pickOneGeneric(TwitterAccountEnum.values()).name();
        final Twitter readOnlyTwitterTemplate = twitterCreator.createTwitterTemplate(randomAccount);

        final TwitterProfile userProfile = readOnlyTwitterTemplate.userOperations().getUserProfile(userHandle);
        metrics.counter(MetricsUtil.Meta.TWITTER_READ_OK).inc();

        return Preconditions.checkNotNull(userProfile);
    }

    // user profiles - multi-request - search

    public final List<TwitterProfile> searchForUsers(final String keyword, final int howManyPages) {
        if (howManyPages <= 1) {
            return searchForUsers(keyword);
        }
        if (howManyPages > 20) {
            throw new IllegalStateException();
        }

        final String randomAccount = GenericUtil.pickOneGeneric(TwitterAccountEnum.values()).name();
        final Twitter readOnlyTwitterTemplate = twitterCreator.createTwitterTemplate(randomAccount);
        final UserOperations userOperations = readOnlyTwitterTemplate.userOperations();

        final List<TwitterProfile> collector = Lists.newArrayList();
        int currentPageIndex = 0;
        List<TwitterProfile> currentPage = null;
        boolean keepGoing = true;

        while (keepGoing) {
            currentPage = userOperations.searchForUsers(keyword, currentPageIndex, 20);
            collector.addAll(currentPage);
            currentPageIndex++;
            if (collector.size() < 20) {
                // done - under 20 user profiles (1 page)
                keepGoing = false;
            }
            if (currentPageIndex == howManyPages) {
                keepGoing = false;
            }
        }

        return collector;
    }

    // user profiles - multiple - search

    public final List<TwitterProfile> searchForUsers(final String keyword) {
        final String randomAccount = GenericUtil.pickOneGeneric(TwitterAccountEnum.values()).name();
        final Twitter readOnlyTwitterTemplate = twitterCreator.createTwitterTemplate(randomAccount);

        final List<TwitterProfile> usersByKeyword = readOnlyTwitterTemplate.userOperations().searchForUsers(keyword);
        return usersByKeyword;
    }

    // friends

    public Set<Long> getFriendIds(final TwitterProfile account, final int maxPages) {
        final Set<Long> fullListOfFriends = Sets.newHashSet();
        final FriendOperations friendOperations = readOnlyTwitterApi().friendOperations();
        final String screenName = account.getScreenName();

        CursoredList<Long> currentPage = friendOperations.getFriendIds(screenName);
        fullListOfFriends.addAll(currentPage);

        final int maxNecessaryPages = (account.getFriendsCount() / 5000) + 1;
        final int maxActualPages = Math.min(maxNecessaryPages, maxPages) - 1;
        for (int i = 0; i < maxActualPages; i++) {
            final long nextCursor = currentPage.getNextCursor();
            currentPage = friendOperations.getFriendIdsInCursor(screenName, nextCursor);
            fullListOfFriends.addAll(currentPage);
        }

        return fullListOfFriends;
    }

    // followers

    public final Set<Long> getFollowerIdsOfMyAccount(final String myAccount) {
        final Twitter readOnlyTwitterTemplate = twitterCreator.createTwitterTemplate(myAccount);
        final FriendOperations friendOperations = readOnlyTwitterTemplate.friendOperations();

        final CursoredList<Long> followerIds = friendOperations.getFollowerIds();
        return Sets.newHashSet(followerIds);
    }

    // internal API

    private final Twitter readOnlyTwitterApi() {
        final String randomAccount = GenericUtil.pickOneGeneric(TwitterAccountEnum.values()).name();
        final Twitter readOnlyTwitterTemplate = twitterCreator.createTwitterTemplate(randomAccount);
        return readOnlyTwitterTemplate;
    }

}
