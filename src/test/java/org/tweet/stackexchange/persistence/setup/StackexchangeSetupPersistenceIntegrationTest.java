package org.tweet.stackexchange.persistence.setup;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.tweet.spring.PersistenceJPATestConfig;
import org.tweet.stackexchange.persistence.dao.IQuestionTweetJpaDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceJPATestConfig.class })
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
        stackexchangeSetup.recreateTwitterQuestions(new String[] { randomNumeric(3), randomNumeric(3) });
    }

    @Test
    public final void whenRecreatingTheTweetedQuestions_thenQuestionsAreCreated() {
        final String idOfQuestion = randomNumeric(3);
        stackexchangeSetup.recreateTwitterQuestions(new String[] { idOfQuestion, randomNumeric(3) });
        final IQuestionTweetJpaDAO questionTweetJpaDAO = beanFactory.getBean(IQuestionTweetJpaDAO.class);
        // assertThat(actual, matcher)questionTweetJpaDAO.findAll().contains(new QuestionTweet(idOfQuestion, MyTwitterAccounts.SERVERFAULT_BEST));
    }

}
