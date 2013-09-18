package org.tweet.spring;

import org.common.spring.PersistenceJPACommonConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.tweet.spring.util.SpringProfileUtil;

@Configuration
@Import(PersistenceJPACommonConfig.class)
@ComponentScan({ "org.stackexchange.persistence" })
@ImportResource({ "classpath*:stackPersistenceConfig.xml" })
@PropertySource({ "classpath:persistence-${persistenceTarget:test}.properties", "classpath:setup-test.properties" })
@Profile(SpringProfileUtil.PERSISTENCE)
public class TestStackexchangePersistenceJPAConfig {

    public TestStackexchangePersistenceJPAConfig() {
        super();
    }

}
