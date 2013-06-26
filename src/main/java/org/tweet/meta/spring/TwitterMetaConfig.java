package org.tweet.meta.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "org.tweet.meta" })
public class TwitterMetaConfig {

    public TwitterMetaConfig() {
        super();
    }

    // API

}