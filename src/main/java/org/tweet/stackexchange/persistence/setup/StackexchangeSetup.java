package org.tweet.stackexchange.persistence.setup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.stackexchange.persistence.dao.IQuestionTweetJpaDAO;
import org.tweet.stackexchange.persistence.model.QuestionTweet;
import org.tweet.stackexchange.util.MyTwitterAccounts;

import com.google.common.base.Preconditions;

@Component
@Profile(SpringProfileUtil.DEPLOYED)
public class StackexchangeSetup implements ApplicationListener<ContextRefreshedEvent> {
    private final Logger logger = LoggerFactory.getLogger(StackexchangeSetup.class);

    private boolean setupDone;

    @Autowired
    private ApplicationContext eventPublisher;

    @Autowired
    private Environment env;

    @Autowired
    private IQuestionTweetJpaDAO questionTweetApi;

    public StackexchangeSetup() {
        super();
    }

    //

    /**
     * - note that this is a compromise - the flag makes this bean statefull which can (and will) be avoided in the future by a more advanced mechanism <br>
     * - the reason for this is that the context is refreshed more than once throughout the lifecycle of the deployable <br>
     * - alternatives: proper persisted versioning
     */
    @Override
    public final void onApplicationEvent(final ContextRefreshedEvent event) {
        if (!setupDone) {
            logger.info("Executing Setup");
            eventPublisher.publishEvent(new BeforeSetupEvent(this));

            if (env.getProperty("setup.active", Boolean.class)) {
                recreateTwittedQuestions();
            }

            setupDone = true;
            logger.info("Setup Done");
        }
    }

    // util

    private void recreateTwittedQuestions() {
        final String tweetedQuestions = Preconditions.checkNotNull(env.getProperty("ServerFaultBest.done"));
        final String[] questionIds = tweetedQuestions.split(",");
        for (final String questionId : questionIds) {
            final QuestionTweet questionTweet = new QuestionTweet(questionId, MyTwitterAccounts.SERVERFAULT_BEST);
            questionTweetApi.save(questionTweet);
        }
    }

    //

}
