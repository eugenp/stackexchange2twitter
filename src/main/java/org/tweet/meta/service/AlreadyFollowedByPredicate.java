package org.tweet.meta.service;

import java.util.Set;

import javax.annotation.Nullable;

import org.springframework.social.twitter.api.TwitterProfile;

import com.google.api.client.util.Preconditions;
import com.google.common.base.Predicate;

public class AlreadyFollowedByPredicate implements Predicate<TwitterProfile> {

    private final Set<Long> alreadyFollowedAccounts;

    public AlreadyFollowedByPredicate(final Set<Long> alreadyFollowedAccounts) {
        super();

        this.alreadyFollowedAccounts = Preconditions.checkNotNull(alreadyFollowedAccounts);
    }

    // API

    @Override
    public final boolean apply(@Nullable final TwitterProfile input) {
        final boolean alreadyFollows = alreadyFollowedAccounts.contains(input.getId());
        if (alreadyFollows) {
            return true;
        }

        return false;
    }

}
