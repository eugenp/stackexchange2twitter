package org.tweet.stackexchange.persistence.setup;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.tweet.spring.TestStackexchangePersistenceJPAConfig;
import org.tweet.stackexchange.persistence.dao.IQuestionTweetJpaDAO;
import org.tweet.stackexchange.persistence.model.QuestionTweet;
import org.tweet.stackexchange.util.SimpleTwitterAccount;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestStackexchangePersistenceJPAConfig.class })
public class StackexchangeSetupPersistenceIntegrationTest {

    @Autowired
    private IQuestionTweetJpaDAO questionTweetApi;

    @Autowired
    private BeanFactory beanFactory;

    private StackexchangeSetup stackexchangeSetup;

    // fixtures

    @Before
    public final void before() {
        stackexchangeSetup = new StackexchangeSetup();
        final IQuestionTweetJpaDAO questionTweetJpaDAO = beanFactory.getBean(IQuestionTweetJpaDAO.class);
        ReflectionTestUtils.setField(stackexchangeSetup, "questionTweetApi", questionTweetJpaDAO);
    }

    // tests

    @Test
    public final void whenRecreatingTheTweetedQuestions_thenNoExceptions() {
        stackexchangeSetup.recreateQuestions(new String[] { randomNumeric(3), randomNumeric(3) }, SimpleTwitterAccount.ServerFaultBest);
    }

    @Test
    public final void whenRecreatingTheTweetedQuestions_thenQuestionsAreCreated() {
        final String idOfQuestion = randomNumeric(3);
        stackexchangeSetup.recreateQuestions(new String[] { idOfQuestion, randomNumeric(3) }, SimpleTwitterAccount.ServerFaultBest);
        final IQuestionTweetJpaDAO questionTweetJpaDAO = beanFactory.getBean(IQuestionTweetJpaDAO.class);

        final QuestionTweet questionTweet = new QuestionTweet(idOfQuestion, SimpleTwitterAccount.ServerFaultBest.name(), TwitterAccountToStackAccount.twitterAccountToStackSite(SimpleTwitterAccount.ServerFaultBest).name());
        assertThat(questionTweetJpaDAO.findAll(), hasItem(questionTweet));
    }

}
