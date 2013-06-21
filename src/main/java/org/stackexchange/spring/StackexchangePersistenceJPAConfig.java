package org.stackexchange.spring;

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
@PropertySource({ "classpath:persistence-${persistenceTarget:prod}.properties", "classpath:setup.properties" })
public class StackexchangePersistenceJPAConfig {

    public StackexchangePersistenceJPAConfig() {
        super();
    }

}
