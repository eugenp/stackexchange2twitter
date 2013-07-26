package org.stackexchange.persistence.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.util.Date;

import org.common.persistence.IEntityOperations;
import org.springframework.stereotype.Component;
import org.stackexchange.persistence.model.QuestionTweet;

@Component
public final class QuestionTweetEntityOps implements IEntityOperations<QuestionTweet> {

    public QuestionTweetEntityOps() {
        super();
    }

    // API

    // template method

    @Override
    public final QuestionTweet createNewEntity() {
        return new QuestionTweet(randomAlphabetic(6), randomAlphabetic(6), randomAlphabetic(6), new Date());
    }

    @Override
    public final void invalidate(final QuestionTweet entity) {
        entity.setQuestionId(null);
        entity.setTwitterAccount(null);
    }

    @Override
    public final void change(final QuestionTweet entity) {
        entity.setQuestionId(randomAlphabetic(6));
    }

}
