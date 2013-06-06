package org.tweet.meta.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.tweet.meta.model.Retweet;

public interface IRetweetJpaDAO extends JpaRepository<Retweet, Long>, JpaSpecificationExecutor<Retweet> {

    public Retweet findByTweetId(final long tweetId);

}
