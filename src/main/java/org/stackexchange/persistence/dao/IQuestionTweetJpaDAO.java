package org.stackexchange.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.stackexchange.persistence.model.QuestionTweet;

public interface IQuestionTweetJpaDAO extends JpaRepository<QuestionTweet, Long>, JpaSpecificationExecutor<QuestionTweet> {

    public QuestionTweet findByQuestionId(final String questionId);

}
