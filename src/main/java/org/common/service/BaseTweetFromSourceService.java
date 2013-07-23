package org.common.service;

import java.util.Map;

import org.common.persistence.IEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.tweet.twitter.service.ITwitterWriteLiveService;
import org.tweet.twitter.service.TweetService;
import org.tweet.twitter.service.TwitterReadLiveService;

public abstract class BaseTweetFromSourceService<T extends IEntity> {

    @Autowired
    protected TwitterReadLiveService twitterReadLiveService;
    @Autowired
    protected ITwitterWriteLiveService twitterWriteLiveService;

    @Autowired
    protected TweetService tweetService;

    // API

    protected abstract boolean tryTweetOne(final String text, final String url, final String twitterAccount, final Map<String, Object> customDetails);

    /**
     * - this should decide based also on the twitterAccount - currently, the implementations of T should actually contain the twitterAccount information
     */
    protected abstract boolean hasThisAlreadyBeenTweeted(final T entity);

    protected abstract void markDone(final T entity);

    protected abstract JpaRepository<T, Long> getApi();

}
