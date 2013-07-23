package org.tweet.meta.persistence.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.tweet.meta.persistence.model.Retweet;

public interface IRetweetJpaDAO extends JpaRepository<Retweet, Long>, JpaSpecificationExecutor<Retweet> {

    Retweet findOneByTweetId(final long tweetId);

    List<Retweet> findAllByTweetId(final long tweetId);

    List<Retweet> findAllByTwitterAccount(final String twitterAccount);

    Retweet findOneByTweetIdAndTwitterAccount(final long tweetId, final String twitterAccount);

    Retweet findOneByTextAndTwitterAccount(final String text, final String twitterAccount);

}
