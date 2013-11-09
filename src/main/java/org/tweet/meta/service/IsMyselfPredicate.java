package org.tweet.meta.service;

import javax.annotation.Nullable;

import org.springframework.social.twitter.api.TwitterProfile;

import com.google.api.client.util.Preconditions;
import com.google.common.base.Predicate;

public class IsMyselfPredicate implements Predicate<TwitterProfile> {

    private final String myAccount;

    public IsMyselfPredicate(final String myAccount) {
        super();
        this.myAccount = Preconditions.checkNotNull(myAccount);
    }

    // API

    @Override
    public final boolean apply(@Nullable final TwitterProfile input) {
        if (input.getScreenName().equals(myAccount)) {
            return true;
        }
        return false;
    }

}
