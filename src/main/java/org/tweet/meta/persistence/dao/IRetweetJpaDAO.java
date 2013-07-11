package org.tweet.meta.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.tweet.meta.persistence.model.Retweet;

public interface IRetweetJpaDAO extends JpaRepository<Retweet, Long>, JpaSpecificationExecutor<Retweet> {

    public Retweet findByTweetId(final long tweetId);

    public Retweet findByTweetIdAndTwitterAccount(final long tweetId, final String twitterAccount);

}
