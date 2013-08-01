package org.common.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({ "org.common.service" })
@Import(CommonMetricsConfig.class)
public class CommonServiceConfig {

    public CommonServiceConfig() {
        super();
    }

}
