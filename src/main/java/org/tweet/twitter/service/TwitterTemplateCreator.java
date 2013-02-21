package org.tweet.twitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

@Component
public class TwitterTemplateCreator {

    @Autowired
    private Environment env;

    public TwitterTemplateCreator() {
        super();
    }

    // API

    public final Twitter getTwitterTemplate(final String accountName) {
        final String consumerKey = Preconditions.checkNotNull(env.getProperty(accountName + ".consumerKey"));
        final String consumerSecret = Preconditions.checkNotNull(env.getProperty(accountName + ".consumerSecret"));
        final String accessToken = Preconditions.checkNotNull(env.getProperty(accountName + ".accessToken"));
        final String accessTokenSecret = Preconditions.checkNotNull(env.getProperty(accountName + ".accessTokenSecret"));

        final TwitterTemplate twitterTemplate = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
        return twitterTemplate;
    }

}
