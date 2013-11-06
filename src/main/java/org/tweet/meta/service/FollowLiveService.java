package org.tweet.meta.service;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.live.UserLiveService;
import org.tweet.twitter.util.TwitterInteractionWithValue;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Floats;

@Service
@Profile(SpringProfileUtil.WRITE)
public class FollowLiveService {
    // private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserLiveService userLiveService;

    @Autowired
    private InteractionLiveService interactionLiveService;

    // API

    public final void followBestUser(final String account, final String keyword) {
        final List<TwitterProfile> usersByKeyword = userLiveService.searchForUsers(keyword);

        final List<Pair<String, TwitterInteractionWithValue>> interactionValues = Lists.newArrayList();

        for (final TwitterProfile twitterProfile : usersByKeyword) {
            final TwitterInteractionWithValue interactionWithAuthor = interactionLiveService.determineBestInteractionWithAuthorLive(twitterProfile, twitterProfile.getScreenName(), TwitterAccountEnum.BestScala.name());
            interactionValues.add(new ImmutablePair<String, TwitterInteractionWithValue>(twitterProfile.getScreenName(), interactionWithAuthor));
        }

        class OrderingByValue extends Ordering<Pair<String, TwitterInteractionWithValue>> {
            @Override
            public final int compare(final Pair<String, TwitterInteractionWithValue> v1, final Pair<String, TwitterInteractionWithValue> v2) {
                return Floats.compare(v1.getRight().getVal(), v2.getRight().getVal());
            }
        }
        final Ordering<Pair<String, TwitterInteractionWithValue>> byValue = new OrderingByValue();
        Collections.sort(interactionValues, byValue);

        final String screenNameOfBestValueUser = interactionValues.get(0).getLeft();
        System.out.println(screenNameOfBestValueUser);
        // userLiveService.followUser(screenName);
    }

}
