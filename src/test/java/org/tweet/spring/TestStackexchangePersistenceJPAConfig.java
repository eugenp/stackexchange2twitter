package org.tweet.spring;

import org.common.spring.PersistenceJPACommonConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(PersistenceJPACommonConfig.class)
@ComponentScan({ "org.stackexchange.persistence" })
@ImportResource("classpath*:stackPersistenceConfig.xml")
@PropertySource({ "classpath:persistence-${persistenceTarget:test}.properties" })
public class TestStackexchangePersistenceJPAConfig {

    public TestStackexchangePersistenceJPAConfig() {
        super();
    }

}
