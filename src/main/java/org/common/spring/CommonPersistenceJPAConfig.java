package org.common.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.tweet.common.spring.PersistenceJPACommonConfig;

@Configuration
@Import(PersistenceJPACommonConfig.class)
@ComponentScan({ "org.common.persistence" })
@ImportResource("classpath*:commonPersistenceConfig.xml")
public class CommonPersistenceJPAConfig {

    public CommonPersistenceJPAConfig() {
        super();
    }

}
