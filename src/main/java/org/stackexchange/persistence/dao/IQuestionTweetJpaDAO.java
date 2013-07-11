package org.stackexchange.persistence.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.stackexchange.persistence.model.QuestionTweet;

public interface IQuestionTweetJpaDAO extends JpaRepository<QuestionTweet, Long>, JpaSpecificationExecutor<QuestionTweet> {

    public QuestionTweet findByQuestionId(final String questionId);

    public QuestionTweet findByQuestionIdAndTwitterAccount(final String questionId, final String twitterAccount);

    public List<QuestionTweet> findAllByTwitterAccount(final String twitterAccount);

    @Query("select count(q) from QuestionTweet q where q.twitterAccount = ?1")
    public long countAllByTwitterAccount(final String twitterAccount);

}
