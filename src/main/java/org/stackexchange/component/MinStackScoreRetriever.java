package org.stackexchange.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.stackexchange.api.constants.StackSite;

import com.google.common.base.Preconditions;

@Component
public final class MinStackScoreRetriever {

    @Autowired
    private Environment env;

    public MinStackScoreRetriever() {
        super();
    }

    // API

    final String minScoreRaw(final String stackTag, final StackSite stackSite) {
        return env.getProperty(stackTag + "." + stackSite.name() + ".minscore");
    }

    public final int minScore(final String stackTag, final StackSite stackSite, final String twitterAccount) {
        Preconditions.checkNotNull(minScoreRaw(stackTag, stackSite), "Unable to find minscore for stackTag= " + stackTag + " on twitter account= " + twitterAccount);
        return env.getProperty(stackTag + "." + stackSite.name() + ".minscore", Integer.class);
    }

}
