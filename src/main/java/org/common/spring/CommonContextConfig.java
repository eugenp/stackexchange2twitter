package org.common.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "org.common.service" })
public class CommonContextConfig {

    public CommonContextConfig() {
        super();
    }

}
