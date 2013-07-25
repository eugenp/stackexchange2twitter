package org.common.service;

import java.util.Map;

import org.common.persistence.IEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.tweet.twitter.service.TweetLiveService;
import org.tweet.twitter.service.TweetService;
import org.tweet.twitter.service.live.ITwitterWriteLiveService;
import org.tweet.twitter.service.live.TwitterReadLiveService;

public abstract class BaseTweetFromSourceLiveService<T extends IEntity> {

    @Autowired
    protected TwitterReadLiveService twitterReadLiveService;
    @Autowired
    protected ITwitterWriteLiveService twitterWriteLiveService;

    @Autowired
    protected TweetService tweetService;
    @Autowired
    protected TweetLiveService tweetLiveService;

    // API

    protected abstract boolean tryTweetOne(final String text, final String url, final String twitterAccount, final Map<String, Object> customDetails);

    /**
     * - this should decide based also on the twitterAccount - currently, the implementations of T should actually contain the twitterAccount information
     */
    protected abstract boolean hasThisAlreadyBeenTweetedById(final T entity);

    protected abstract void markDone(final T entity);

    protected abstract JpaRepository<T, Long> getApi();

}
