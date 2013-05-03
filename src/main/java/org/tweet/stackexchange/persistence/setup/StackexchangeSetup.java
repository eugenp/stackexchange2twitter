package org.tweet.stackexchange.persistence.setup;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.stackexchange.api.constants.StackSite;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.stackexchange.persistence.dao.IQuestionTweetJpaDAO;
import org.tweet.stackexchange.persistence.model.QuestionTweet;
import org.tweet.stackexchange.util.SimpleTwitterAccount;

import com.google.common.base.Preconditions;

/**
 * SETUP: </br>
 * - step 1 - export the production DB </br>
 * - clear the local (prod) DB and import the new one locally </br>
 * - run {@link SetupBackupIntegrationTest}, update the setup.properties file with the new values </br>
 * - change, in setup.properties - setup.do=false to setup.do=true </br>
 * -- test locally - erase the local DB, restart the server, check that everything gets created correctly
 * - erase the production DB
 * - restart the server (on production)
 */
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

    @Override
    public final void onApplicationEvent(final ContextRefreshedEvent event) {
        if (!setupDone) {
            logger.info("Executing Setup");
            eventPublisher.publishEvent(new BeforeSetupEvent(this));

            if (env.getProperty("setup.do", Boolean.class)) {
                repersistAllQuestionsOnAllTwitterAccounts();
            }

            setupDone = true;
            logger.info("Setup Done");
        }
    }

    // util

    final void repersistAllQuestionsOnAllTwitterAccounts() {
        for (final SimpleTwitterAccount twitterAccount : SimpleTwitterAccount.values()) {
            repersistAllQuestionsOnTwitterAccount(twitterAccount);
        }
    }

    private void repersistAllQuestionsOnTwitterAccount(final SimpleTwitterAccount twitterAccount) {
        final String tweetedQuestions = Preconditions.checkNotNull(env.getProperty(twitterAccount.name() + ".done"));
        final String[] questionIds = tweetedQuestions.split(",");
        recreateTwitterQuestions(questionIds, twitterAccount);
    }

    final void recreateTwitterQuestions(final String[] questionIds, final SimpleTwitterAccount twitterAccount) {
        final List<StackSite> stackSitesForTwitterAccount = TwitterAccountToStackAccount.twitterAccountToStackSites(twitterAccount);
        final StackSite site;
        if (stackSitesForTwitterAccount.size() == 1) {
            site = stackSitesForTwitterAccount.get(0);
        } else {
            site = null;
        }

        for (final String questionId : questionIds) {
            final QuestionTweet questionTweet = new QuestionTweet(questionId, twitterAccount.name(), site.name());
            questionTweetApi.save(questionTweet);
        }
    }

}
