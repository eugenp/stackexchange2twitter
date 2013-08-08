package org.tweet.twitter.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.api.client.util.Preconditions;

/**
 * - note: this is under .twitter (and not under .meta) because is core twitter functionality
 */
@Component
public class MinRtRetriever {

    @Autowired
    private Environment env;

    public MinRtRetriever() {
        super();
    }

    // API

    public final int minRt(final String twitterTag) {
        Preconditions.checkNotNull(minRtRaw(twitterTag), "Unable to find min rt value for twitterTag= " + twitterTag);
        return env.getProperty(twitterTag + ".minrt", Integer.class);
    }

    final String minRtRaw(final String twitterTag) {
        return env.getProperty(twitterTag + ".minrt");
    }

}
