package org.tweet.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({ "file:///opt/stack/twitter.properties" })
public class TwitterLiveConfig {

    public TwitterLiveConfig() {
        super();
    }

    // API

}