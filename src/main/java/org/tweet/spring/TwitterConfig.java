package org.tweet.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Spring Social Configuration.
 */
@Configuration
@ComponentScan({ "org.tweet.twitter" })
@PropertySource({ "classpath:twitter.properties" })
public class TwitterConfig {

    public TwitterConfig() {
        super();
    }

    // API

}