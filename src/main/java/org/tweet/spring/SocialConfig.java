package org.tweet.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
public class SocialConfig {

    private static final String CONSUMER_KEY = "2jXUgs9QmrNzdzRzglDBTg";
    private static final String CONSUMER_SECRET = "XipjDslY6bqhulUW0IHbAEIqHgkk0IWOfwg4OljU";
    private static final String ACCESS_TOKEN = "1169459593-tWD1l9Ni7Ocy3EdcHkM0WF7SGfbpXsbX9NAAi3f";
    private static final String ACCESS_TOKEN_SECRET = "2zjY7iPCm8PDcPfrQvKjdU5No3SXgkuluncqYgBBc";

    @Autowired
    private Environment environment;

    public SocialConfig() {
        super();
    }

    // API

    @Bean
    public Twitter twitter() {
        final TwitterTemplate twitterTemplate = new TwitterTemplate(SocialConfig.CONSUMER_KEY, SocialConfig.CONSUMER_SECRET, SocialConfig.ACCESS_TOKEN, SocialConfig.ACCESS_TOKEN_SECRET);
        return twitterTemplate;
    }

    // http://stackoverflow.com/questions/7968641/spring-social-twitter-oauth

}