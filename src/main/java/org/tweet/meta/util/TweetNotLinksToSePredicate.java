package org.tweet.meta.util;

import org.common.service.live.LinkLiveService;
import org.common.util.LinkUtil;
import org.springframework.social.twitter.api.Tweet;

import com.google.api.client.util.Preconditions;
import com.google.common.base.Predicate;

public final class TweetNotLinksToSePredicate implements Predicate<Tweet> {

    private LinkLiveService linkLiveService;

    public TweetNotLinksToSePredicate(final LinkLiveService linkLiveService) {
        this.linkLiveService = Preconditions.checkNotNull(linkLiveService);
    }

    @Override
    public final boolean apply(final Tweet tweet) {
        return linkLiveService.countLinksToAnyDomain(tweet, LinkUtil.seDomains) == 0;
    }

}
