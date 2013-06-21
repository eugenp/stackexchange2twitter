package org.tweet.meta.persistence.setup;

import org.common.persistence.dao.IKeyValJpaDAO;
import org.common.persistence.setup.BeforeSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TwitterMetaSetup implements ApplicationListener<ContextRefreshedEvent> {
    private final Logger logger = LoggerFactory.getLogger(TwitterMetaSetup.class);

    private boolean setupDone;

    @Autowired
    private ApplicationContext eventPublisher;

    @Autowired
    private Environment env;

    @Autowired
    private IKeyValJpaDAO keyValApi;

    public TwitterMetaSetup() {
        super();
    }

    //

    @Override
    public final void onApplicationEvent(final ContextRefreshedEvent event) {
        if (!setupDone) {
            logger.info("Before Setup");
            eventPublisher.publishEvent(new BeforeSetupEvent(this));

            setupRetweetThresholds();

            setupDone = true;
            logger.info("After Setup");
        }
    }

    // util

    private void setupRetweetThresholds() {
        // keyValApi.save(new KeyVal(Tag.jquery.name(), 5d));
    }

}
