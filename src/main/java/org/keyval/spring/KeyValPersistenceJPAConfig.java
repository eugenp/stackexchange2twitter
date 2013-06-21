package org.keyval.spring;

import org.common.spring.PersistenceJPACommonConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@Import(PersistenceJPACommonConfig.class)
@ComponentScan({ "org.keyval.persistence" })
@ImportResource("classpath*:keyValPersistenceConfig.xml")
public class KeyValPersistenceJPAConfig {

    public KeyValPersistenceJPAConfig() {
        super();
    }

}
