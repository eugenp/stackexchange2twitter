package org.tweet.twitter.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.api.client.util.Preconditions;

/**
 * - note: this is under .twitter (and not under .meta) because is core twitter functionality
 */
@Component
public class MaxRtRetriever {

    @Autowired
    private Environment env;

    public MaxRtRetriever() {
        super();
    }

    // API

    final String maxrtRaw(final String twitterTag) {
        return env.getProperty(twitterTag + ".maxrt");
    }

    public final int maxRt(final String twitterTag) {
        Preconditions.checkNotNull(maxrtRaw(twitterTag), "Unable to find max rt value for twitterTag= " + twitterTag);
        return env.getProperty(twitterTag + ".maxrt", Integer.class);
    }

}