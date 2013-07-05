package org.rss.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "org.rss" })
public class RssContextConfig {

    public RssContextConfig() {
        super();
    }

}
