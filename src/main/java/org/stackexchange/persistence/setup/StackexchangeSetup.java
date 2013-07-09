package org.stackexchange.persistence.setup;

import java.util.List;

import org.common.persistence.setup.BeforeSetupEvent;
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
import org.stackexchange.persistence.dao.IQuestionTweetJpaDAO;
import org.stackexchange.persistence.model.QuestionTweet;
import org.stackexchange.util.SimpleTwitterAccount;
import org.tweet.spring.util.SpringProfileUtil;

import com.google.common.base.Preconditions;

/**
 * <b>SETUP</b>: </p></p>
 * - export the production DB </p>
 * - drop the local (prod) DB and import the new one locally </p>
 * - run {@link SetupBackupIntegrationTest}, update the setup.properties file with the new values </p>
 * - change, in setup.properties - setup.do=false to setup.do=true </p>
 * -- test locally - erase the local DB, restart the server, check that everything gets created correctly </p>
 * - erase the production DB </p>
 * - restart the server (on production) </p>
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
            logger.info("Before Setup");
            eventPublisher.publishEvent(new BeforeSetupEvent(this));

            if (env.getProperty("setup.do", Boolean.class)) {
                logger.info("Setup Active - Executing");
                repersistAllQuestionsOnAllTwitterAccounts();
            }

            setupDone = true;
            logger.info("After Setup");
        }
    }

    // util

    final void repersistAllQuestionsOnAllTwitterAccounts() {
        for (final SimpleTwitterAccount twitterAccount : SimpleTwitterAccount.values()) {
            recreateAllQuestionsOnTwitterAccount(twitterAccount);
        }
    }

    private void recreateAllQuestionsOnTwitterAccount(final SimpleTwitterAccount twitterAccount) {
        logger.info("Before Setup for twitterAccount= {}", twitterAccount);

        final String tweetedQuestions = Preconditions.checkNotNull(env.getProperty(twitterAccount.name()), "No Questions in setup.properties: for twitterAccount" + twitterAccount);
        final String[] questionIds = tweetedQuestions.split(",");
        recreateQuestions(questionIds, twitterAccount);
    }

    final void recreateQuestions(final String[] questionIds, final SimpleTwitterAccount twitterAccount) {
        final List<StackSite> stackSitesForTwitterAccount = TwitterAccountToStackAccount.twitterAccountToStackSites(twitterAccount);
        final StackSite site;
        if (stackSitesForTwitterAccount.size() == 1) {
            site = stackSitesForTwitterAccount.get(0);
        } else {
            site = null;
        }

        for (final String questionId : questionIds) {
            final QuestionTweet questionTweet = new QuestionTweet(questionId, twitterAccount, site);
            questionTweetApi.save(questionTweet);
        }
    }

}
