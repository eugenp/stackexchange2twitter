package org.tweet.twitter.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

@Component
public final class TwitterHashtagsRetriever {

    @Autowired
    private Environment env;

    public TwitterHashtagsRetriever() {
        super();
    }

    // API

    final String hashtagsRaw(final String twitterAccount) {
        return env.getProperty(twitterAccount + ".hash");
    }

    public final String hashtags(final String twitterAccount) {
        return Preconditions.checkNotNull(hashtagsRaw(twitterAccount));
    }

}
