package org.tweet.meta.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.live.UserLiveService;
import org.tweet.twitter.util.TwitterInteractionWithValue;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Floats;

@Service
@Profile(SpringProfileUtil.WRITE)
public class FollowLiveService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserLiveService userLiveService;

    @Autowired
    private InteractionLiveService interactionLiveService;

    // API

    public final void followBestUser(final String myAccount, final String keyword) {
        final List<TwitterProfile> usersByKeyword = userLiveService.searchForUsers(keyword);
        final Set<Long> alreadyFollowedAccounts = userLiveService.getIdsOfAccountsFollowedByMyAccount(myAccount);
        final Iterable<TwitterProfile> newAccountsToFollow = Iterables.filter(usersByKeyword, Predicates.not(new AlreadyFollowedByPredicate(alreadyFollowedAccounts)));
        final Iterable<TwitterProfile> newAccountsToFollowWithoutMyself = Iterables.filter(newAccountsToFollow, Predicates.not(new IsMyselfPredicate(myAccount)));

        final List<Pair<String, TwitterInteractionWithValue>> interactionValues = Lists.newArrayList();

        for (final TwitterProfile twitterProfile : newAccountsToFollowWithoutMyself) {
            final TwitterInteractionWithValue interactionWithAuthor = interactionLiveService.determineBestInteractionWithAuthorLive(twitterProfile, twitterProfile.getScreenName(), TwitterAccountEnum.ScalaFact.name());
            interactionValues.add(new ImmutablePair<String, TwitterInteractionWithValue>(twitterProfile.getScreenName(), interactionWithAuthor));
        }

        if (interactionValues.isEmpty()) {
            logger.warn("Found no user to follow for account= {} and keyword= {}", myAccount, keyword);
            return;
        }

        class OrderingByValue extends Ordering<Pair<String, TwitterInteractionWithValue>> {
            @Override
            public final int compare(final Pair<String, TwitterInteractionWithValue> v1, final Pair<String, TwitterInteractionWithValue> v2) {
                return Floats.compare(v1.getRight().getVal(), v2.getRight().getVal());
            }
        }
        final Ordering<Pair<String, TwitterInteractionWithValue>> byValue = new OrderingByValue().reverse();
        Collections.sort(interactionValues, byValue);

        final String screenNameOfBestValueUser = interactionValues.get(0).getLeft();
        userLiveService.followUser(myAccount, screenNameOfBestValueUser);
    }

}
