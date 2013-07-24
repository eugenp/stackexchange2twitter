package org.tweet.meta.persistence.dao;

import java.util.List;

import org.common.persistence.IOperations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tweet.meta.persistence.model.Retweet;

public interface IRetweetJpaDAO extends JpaRepository<Retweet, Long>, IOperations<Retweet> {

    // find - one

    Retweet findOneByTweetId(final long tweetId);

    Retweet findOneByTweetIdAndTwitterAccount(final long tweetId, final String twitterAccount);

    Retweet findOneByTextAndTwitterAccount(final String text, final String twitterAccount);

    // find - all

    List<Retweet> findAllByTweetId(final long tweetId);

    List<Retweet> findAllByTwitterAccount(final String twitterAccount);

    // count

    @Query("select count(r) from Retweet r where r.twitterAccount = ?1")
    long countAllByTwitterAccount(final String twitterAccount);

}
