package org.classification.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "org.classification.service" })
public class ClassificationConfig {

    public ClassificationConfig() {
        super();
    }

}
