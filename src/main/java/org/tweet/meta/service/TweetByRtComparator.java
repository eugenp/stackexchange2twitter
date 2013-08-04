package org.tweet.meta.service;

import java.util.Comparator;

import org.springframework.social.twitter.api.Tweet;

public final class TweetByRtComparator implements Comparator<Tweet> {

    @Override
    public final int compare(final Tweet t1, final Tweet t2) {
        return t2.getRetweetCount().compareTo(t1.getRetweetCount());
    }

}
