package org.keyval.spring;

import org.common.spring.PersistenceJPACommonConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.tweet.spring.util.SpringProfileUtil;

@Configuration
@Import(PersistenceJPACommonConfig.class)
@ComponentScan({ "org.keyval.persistence" })
@ImportResource("classpath*:keyValPersistenceConfig.xml")
@Profile(SpringProfileUtil.PERSISTENCE)
public class KeyValPersistenceJPAConfig {

    public KeyValPersistenceJPAConfig() {
        super();
    }

}
