package org.stackexchange.persistence.service;

import org.common.persistence.AbstractRawServicePersistenceIntegrationTest;
import org.common.persistence.IEntityOperations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.persistence.dao.IQuestionTweetJpaDAO;
import org.stackexchange.persistence.model.QuestionTweet;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.TestStackexchangePersistenceJPAConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestStackexchangePersistenceJPAConfig.class })
public class QuestionTweetPersistenceIntegrationTest extends AbstractRawServicePersistenceIntegrationTest<QuestionTweet> {

    @Autowired
    private IQuestionTweetJpaDAO api;

    @Autowired
    private QuestionTweetEntityOps entityOps;

    // tests

    // find - one - by name

    // find - all

    // count - all

    @Test
    /**/public void whenAllResourcesAreCounted_thenNoExceptions() {
        getApi().count();
    }

    @Test
    /**/public void whenAllResourcesAreCountedByTwitterAccount_thenNoExceptions() {
        final long countAllByTwitterAccount = getApi().countAllByTwitterAccount(TwitterAccountEnum.AskUbuntuFact.name());
        System.out.println(countAllByTwitterAccount);
    }

    // create

    // template method

    @Override
    protected IQuestionTweetJpaDAO getApi() {
        return api;
    }

    @Override
    protected IEntityOperations<QuestionTweet> getEntityOps() {
        return entityOps;
    }

}
