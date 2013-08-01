package org.common.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "org.common.metrics" })
public class CommonMetricsConfig {

    public CommonMetricsConfig() {
        super();
    }

}
