package org.stackexchange.persistence.dao;

import java.util.List;

import org.common.persistence.IOperations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.stackexchange.persistence.model.QuestionTweet;

public interface IQuestionTweetJpaDAO extends JpaRepository<QuestionTweet, Long>, IOperations<QuestionTweet> {

    QuestionTweet findByQuestionId(final String questionId);

    QuestionTweet findByQuestionIdAndTwitterAccount(final String questionId, final String twitterAccount);

    List<QuestionTweet> findAllByTwitterAccount(final String twitterAccount);

    @Query("select count(q) from QuestionTweet q where q.twitterAccount = ?1")
    long countAllByTwitterAccount(final String twitterAccount);

}
