package org.stackexchange.persistence.setup;

import org.common.persistence.setup.AfterSetupEvent;
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
import org.stackexchange.persistence.dao.IQuestionTweetJpaDAO;
import org.tweet.spring.util.SpringProfileUtil;

/**
 * <b>SETUP</b>: </p></p>
 * - export the production DB </p>
 * - drop the local (prod) DB and import the new one locally </p>
 * - run {@link SetupBackupManualTest}, update the setup.properties file with the new values </p>
 * - change, in setup.properties - setup.do=false to setup.do=true </p>
 * -- test locally - erase the local DB, restart the server, check that everything gets created correctly </p>
 * - erase the production DB </p>
 * - restart the server (on production) </p>
 */
@Component
@Profile(SpringProfileUtil.DEPLOYED)
public class MainSetup implements ApplicationListener<ContextRefreshedEvent> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private boolean setupDone;

    @Autowired
    private ApplicationContext eventPublisher;

    @Autowired
    private Environment env;

    @Autowired
    private IQuestionTweetJpaDAO questionTweetApi;

    public MainSetup() {
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
                // do something
            }

            setupDone = true;
            eventPublisher.publishEvent(new AfterSetupEvent(this));
            logger.info("After Setup");
        }
    }

    // util

}
