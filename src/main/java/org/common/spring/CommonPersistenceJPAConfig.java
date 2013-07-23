package org.common.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@Import(PersistenceJPACommonConfig.class)
@ComponentScan({ "org.common.persistence" })
@ImportResource("classpath*:commonPersistenceConfig.xml")
@EnableAsync
public class CommonPersistenceJPAConfig {

    public CommonPersistenceJPAConfig() {
        super();
    }

}
