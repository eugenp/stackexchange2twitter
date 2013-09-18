package org.tweet.meta.spring;

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
@ComponentScan({ "org.tweet.meta.persistence" })
@ImportResource("classpath*:metaPersistenceConfig.xml")
@PropertySource({ "classpath:persistence-${persistenceTarget:prod}.properties" })
@Profile(SpringProfileUtil.PERSISTENCE)
public class TwitterMetaPersistenceJPAConfig {

    public TwitterMetaPersistenceJPAConfig() {
        super();
    }

}
