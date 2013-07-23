package org.tweet.meta.persistence.setup;

import org.common.persistence.setup.BeforeSetupEvent;
import org.keyval.persistence.dao.IKeyValJpaDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TwitterMetaSetupListener implements ApplicationListener<BeforeSetupEvent> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationContext eventPublisher;

    @Autowired
    private IKeyValJpaDAO keyValApi;

    public TwitterMetaSetupListener() {
        super();
    }

    //

    @Override
    public final void onApplicationEvent(final BeforeSetupEvent event) {
        setupRetweetThresholds();
    }

    // util

    private void setupRetweetThresholds() {
        // keyValApi.save(new KeyVal(Tag.jquery.name(), 5d));
    }

}
