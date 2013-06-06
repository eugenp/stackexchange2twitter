package org.tweet.stackexchange.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan({ "org.tweet.stackexchange" })
public class StackexchangeContextConfig {

    public StackexchangeContextConfig() {
        super();
    }

}
