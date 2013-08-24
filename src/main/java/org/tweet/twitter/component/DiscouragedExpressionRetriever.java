package org.tweet.twitter.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.stackexchange.util.GenericUtil;

import com.google.common.collect.Lists;

/**
 * - note: this is under .twitter (and not under .meta) because is core twitter functionality
 */
@Component
public class DiscouragedExpressionRetriever {

    @Autowired
    private Environment env;

    public DiscouragedExpressionRetriever() {
        super();
    }

    // API

    /**
     * return - not null
     */
    public final List<String> discouraged(final String twitterAccount) {
        final String discouragedRaw = discouragedRaw(twitterAccount);
        if (discouragedRaw == null) {
            return Lists.newArrayList();
        }
        return GenericUtil.breakApart(discouragedRaw);
    }

    final String discouragedRaw(final String twitterAccount) {
        return env.getProperty(twitterAccount + ".discouraged");
    }

}
