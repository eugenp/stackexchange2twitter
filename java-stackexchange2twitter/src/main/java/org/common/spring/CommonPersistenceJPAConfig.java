package org.common.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.tweet.spring.util.SpringProfileUtil;

@Configuration
@Import(PersistenceJPACommonConfig.class)
@ComponentScan({ "org.common.persistence" })
@ImportResource("classpath*:commonPersistenceConfig.xml")
@EnableAsync
// (proxyTargetClass = true)
@Profile(SpringProfileUtil.PERSISTENCE)
public class CommonPersistenceJPAConfig {

    public CommonPersistenceJPAConfig() {
        super();
    }

}
