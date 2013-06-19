package org.gplus.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan({ "org.gplus" })
public class GplusContextConfig {

    public GplusContextConfig() {
        super();
    }

}
