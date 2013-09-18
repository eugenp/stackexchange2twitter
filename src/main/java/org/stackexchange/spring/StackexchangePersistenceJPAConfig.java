package org.stackexchange.spring;

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
@ImportResource("classpath*:stackPersistenceConfig.xml")
@PropertySource({ "classpath:persistence-${persistenceTarget:prod}.properties", "classpath:setup.properties" })
@Profile(SpringProfileUtil.PERSISTENCE)
public class StackexchangePersistenceJPAConfig {

    public StackexchangePersistenceJPAConfig() {
        super();
    }

}
