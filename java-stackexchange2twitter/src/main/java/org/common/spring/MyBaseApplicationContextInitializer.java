package org.common.spring;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;

import com.google.common.base.Preconditions;

public abstract class MyBaseApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    //

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        final ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String envTarget = null;
        try {
            logPersistenceTarget(environment);
            logEnvTarget(environment);

            envTarget = getEnvTarget(environment);
            environment.getPropertySources().addFirst(new ResourcePropertySource("classpath:env-" + envTarget + ".properties"));

            final String activeProfiles = environment.getProperty("spring.profiles.active");
            logger.info("The active profiles are: {}", activeProfiles);

            environment.setActiveProfiles(activeProfiles.split(","));
        } catch (final IOException ioEx) {
            if (envTarget != null) {
                logger.info("Didn't find env-{}.properties in classpath so not loading it in the AppContextInitialized", envTarget);
            }
        }
    }

    //

    private final void logPersistenceTarget(final ConfigurableEnvironment environment) {
        final String persistenceTarget = environment.getProperty(getPersistenceTargetKey());
        if (persistenceTarget == null) {
            logger.warn("Didn't find a value for {} in the current Environment!", getPersistenceTargetKey());
        } else {
            logger.warn("Value for {} in the current Environment is = {}", getPersistenceTargetKey(), persistenceTarget);
        }
    }

    private final void logEnvTarget(final ConfigurableEnvironment environment) {
        final String envTarget = environment.getProperty(getEnvTargetKey());
        if (envTarget == null) {
            logger.warn("Didn't find a value for {} in the current Environment!", getEnvTargetKey());
        } else {
            logger.warn("Value for {} in the current Environment is = {}", getEnvTargetKey(), envTarget);
        }
    }

    private final String getEnvTarget(final ConfigurableEnvironment environment) {
        String envTarget = environment.getProperty(getEnvTargetKey());
        if (envTarget == null) {
            envTarget = getEnvTargetVal();
            logger.warn("Setting {} to default= {}", getEnvTargetKey(), getEnvTargetVal());
        }

        Preconditions.checkState(getValidEnvTargetValues().contains(envTarget));
        return Preconditions.checkNotNull(envTarget);
    }

    // template

    protected abstract String getPersistenceTargetKey();

    protected abstract String getEnvTargetKey();

    protected abstract String getEnvTargetVal();

    protected abstract List<String> getValidEnvTargetValues();

}
