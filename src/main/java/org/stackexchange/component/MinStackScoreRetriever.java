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

    // by tag and site

    final String minScoreRaw(final String stackTag, final StackSite stackSite) {
        return env.getProperty(stackTag + "." + stackSite.name() + ".minscore");
    }

    public final int minScore(final String stackTag, final StackSite stackSite, final String twitterAccount) {
        Preconditions.checkNotNull(minScoreRaw(stackTag, stackSite), "Unable to find minscore by stackTag= " + stackTag + " and stackSite= " + stackSite + " on twitterAccount= " + twitterAccount);
        return env.getProperty(stackTag + "." + stackSite.name() + ".minscore", Integer.class);
    }

    // by site

    final String minScoreRawBySite(final StackSite stackSite) {
        return env.getProperty(stackSite.name() + ".minscore");
    }

    public final int minScoreBySite(final StackSite stackSite, final String twitterAccount) {
        Preconditions.checkNotNull(minScoreRawBySite(stackSite), "Unable to find minscore by stackSite= " + stackSite + " on twitterAccount= " + twitterAccount);
        return env.getProperty(stackSite.name() + ".minscore", Integer.class);
    }

}
