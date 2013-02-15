package org.tweet.spring;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

/**
 * Spring Social Configuration.
 * https://github.com/SpringSource/spring-social/wiki/Quick-Start
 * http://static.springsource.org/spring-social/docs/1.0.x/reference/html/connecting.html
 */
@Configuration
@ComponentScan({ "org.tweet.twitter" })
public class SocialConfig {

    public static final String CONSUMER_KEY = "2jXUgs9QmrNzdzRzglDBTg";
    public static final String CONSUMER_SECRET = "XipjDslY6bqhulUW0IHbAEIqHgkk0IWOfwg4OljU";
    public static final String ACCESS_TOKEN = "1169459593-tWD1l9Ni7Ocy3EdcHkM0WF7SGfbpXsbX9NAAi3f";
    public static final String ACCESS_TOKEN_SECRET = "2zjY7iPCm8PDcPfrQvKjdU5No3SXgkuluncqYgBBc";

    @Autowired
    private Environment environment;

    @Autowired
    private DataSource dataSource;

    public SocialConfig() {
        super();
    }

    // API

    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        final ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new TwitterConnectionFactory(CONSUMER_KEY, CONSUMER_SECRET));
        return registry;
    }

    @Bean
    public UsersConnectionRepository usersConnectionRepository() {
        return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator(), Encryptors.noOpText());
    }

    @Bean
    public ConnectionRepository connectionRepository() {
        return usersConnectionRepository().createConnectionRepository("ServerFaultBest");
    }

    @Bean
    public ConnectController connectController() {
        return new ConnectController(connectionFactoryLocator(), connectionRepository());
    }

    @Bean
    public Twitter twitter() {
        return connectionRepository().getPrimaryConnection(Twitter.class).getApi();
    }

    // http://stackoverflow.com/questions/7968641/spring-social-twitter-oauth

}