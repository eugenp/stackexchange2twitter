package org.common.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan({ "org.common.service" })
@Import(CommonMetricsConfig.class)
@PropertySource({ "classpath:config-${envTarget:dev}.properties" })
public class CommonServiceConfig {

    public CommonServiceConfig() {
        super();
    }

}
