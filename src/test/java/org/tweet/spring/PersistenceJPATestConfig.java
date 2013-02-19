package org.tweet.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.tweet.common.spring.PersistenceJPACommonConfig;

@Configuration
@Import(PersistenceJPACommonConfig.class)
@PropertySource({ "classpath:persistence-${persistenceTarget:test}.properties", "classpath:setup.properties" })
public class PersistenceJPATestConfig {

    public PersistenceJPATestConfig() {
        super();
    }

}
