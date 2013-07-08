package org.common.service;

import org.common.persistence.IEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.tweet.twitter.service.TweetService;
import org.tweet.twitter.service.TwitterLiveService;

public abstract class BaseTweetFromSourceService<T extends IEntity> {

    @Autowired
    protected TwitterLiveService twitterLiveService;
    @Autowired
    protected TweetService tweetService;

    // API

    protected abstract boolean hasThisAlreadyBeenTweeted(final T entity);

    protected abstract void markDone(final T entity);

    protected abstract JpaRepository<T, Long> getApi();

}
