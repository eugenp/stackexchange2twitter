package org.tweet.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan({ "org.tweet.twitter" })
@PropertySource({ "classpath:twitterInternal.properties", "classpath:interaction.properties" })
public class TwitterConfig {

    public TwitterConfig() {
        super();
    }

    // API

}