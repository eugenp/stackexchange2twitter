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

    final String minScoreRawByAccount(final String twitterAccount) {
        return env.getProperty(twitterAccount + ".minscore");
    }

    public final int minScoreByAccount(final String twitterAccount) {
        Preconditions.checkNotNull(minScoreRawByAccount(twitterAccount), "Unable to find minscore by twitterAccount= " + twitterAccount);
        return env.getProperty(twitterAccount + ".minscore", Integer.class);
    }

}
