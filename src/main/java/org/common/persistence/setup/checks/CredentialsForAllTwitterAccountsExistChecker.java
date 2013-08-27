package org.common.persistence.setup.checks;

import org.common.persistence.setup.AfterSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TwitterTemplateCreator;

@Component
@Profile(SpringProfileUtil.DEPLOYED)
class CredentialsForAllTwitterAccountsExistChecker implements ApplicationListener<AfterSetupEvent> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterTemplateCreator twitterTemplateCreator;

    @Autowired
    private Environment env;

    public CredentialsForAllTwitterAccountsExistChecker() {
        super();
    }

    // API

    @Override
    @Async
    public void onApplicationEvent(final AfterSetupEvent event) {
        logger.info("Starting to execute the CredentialsForAllTwitterAccountsExistChecker Upgrader");

        for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
            twitterTemplateCreator.createTwitterTemplate(twitterAccount.name());
        }

        logger.info("Finished executing the CredentialsForAllTwitterAccountsExistChecker Upgrader");
    }

}
