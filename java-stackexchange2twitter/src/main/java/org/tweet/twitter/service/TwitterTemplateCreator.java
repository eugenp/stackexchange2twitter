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

    public synchronized final Twitter createTwitterTemplate(final String twitterAccount) {
        final String consumerKey = Preconditions.checkNotNull(env.getProperty(twitterAccount + ".consumerKey"), "consumerKey not found for " + twitterAccount);
        final String consumerSecret = Preconditions.checkNotNull(env.getProperty(twitterAccount + ".consumerSecret"), "consumerSecret not found for " + twitterAccount);
        final String accessToken = Preconditions.checkNotNull(env.getProperty(twitterAccount + ".accessToken"), "accessToken not found for " + twitterAccount);
        final String accessTokenSecret = Preconditions.checkNotNull(env.getProperty(twitterAccount + ".accessTokenSecret"), "accessTokenSecret not found for " + twitterAccount);

        final TwitterTemplate twitterTemplate = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
        return twitterTemplate;
    }

}
