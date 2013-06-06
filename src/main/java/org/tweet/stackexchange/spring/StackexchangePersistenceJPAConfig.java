package org.tweet.stackexchange.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.tweet.common.spring.PersistenceJPACommonConfig;

@Configuration
@Import(PersistenceJPACommonConfig.class)
@ComponentScan({ "org.tweet.stackexchange.persistence" })
@ImportResource("classpath*:stackPersistenceConfig.xml")
public class StackexchangePersistenceJPAConfig {

    public StackexchangePersistenceJPAConfig() {
        super();
    }

}
