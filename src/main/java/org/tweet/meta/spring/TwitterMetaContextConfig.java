package org.tweet.meta.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan({ "org.tweet.meta" })
public class TwitterMetaContextConfig {

    public TwitterMetaContextConfig() {
        super();
    }

}
