package org.tweet.stackexchange.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.tweet.stackexchange.persistence.model.QuestionTweet;

public interface IQuestionTweetJpaDAO extends JpaRepository<QuestionTweet, Long>, JpaSpecificationExecutor<QuestionTweet> {
    //
}
