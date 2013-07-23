package org.common.persistence.setup.upgrades;

import org.common.persistence.setup.AfterSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public final class AddTextToRetweetsUpgrader implements ApplicationListener<AfterSetupEvent> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    public AddTextToRetweetsUpgrader() {
        super();
    }

    @Override
    public final void onApplicationEvent(final AfterSetupEvent event) {
        if (env.getProperty("setup.do", Boolean.class)) {
            logger.info("Starting to execute the AddTextToRetweets Upgrader");
            addTextToRetweets();
            logger.info("Finished executing the AddTextToRetweets Upgrader");
        }
    }

    // util

    private final void addTextToRetweets() {
        //
    }

}
