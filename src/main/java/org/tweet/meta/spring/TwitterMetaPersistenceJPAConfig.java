package org.tweet.meta.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.tweet.common.spring.PersistenceJPACommonConfig;

@Configuration
@Import(PersistenceJPACommonConfig.class)
@ComponentScan({ "org.tweet.meta.persistence" })
@ImportResource("classpath*:metaPersistenceConfig.xml")
@PropertySource({ "classpath:persistence-${persistenceTarget:prod}.properties", "classpath:setup.properties" })
public class TwitterMetaPersistenceJPAConfig {

    public TwitterMetaPersistenceJPAConfig() {
        super();
    }

}
