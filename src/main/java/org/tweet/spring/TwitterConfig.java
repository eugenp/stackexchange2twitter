package org.tweet.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

/**
 * Spring Social Configuration.
 * https://github.com/SpringSource/spring-social/wiki/Quick-Start
 * http://static.springsource.org/spring-social/docs/1.0.x/reference/html/connecting.html
 */
@Configuration
@ComponentScan({ "org.tweet.twitter" })
@PropertySource({ "classpath:twitter.properties" })
public class TwitterConfig {

    @Autowired
    private Environment env;

    public TwitterConfig() {
        super();
    }

    // API

    @Bean
    public Twitter twitter() {
        final TwitterTemplate twitterTemplate = new TwitterTemplate(env.getProperty("ServerFaultBest.consumerKey"), env.getProperty("ServerFaultBest.consumerSecret"), env.getProperty("ServerFaultBest.accessToken"),
                env.getProperty("ServerFaultBest.accessTokenSecret"));
        return twitterTemplate;
    }
    // http://stackoverflow.com/questions/7968641/spring-social-twitter-oauth

}