package org.tweet.meta.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.api.client.util.Preconditions;
import com.google.common.collect.Lists;

@Component
public final class PredefinedAccountRetriever {

    @Autowired
    private Environment env;

    public PredefinedAccountRetriever() {
        super();
    }

    // API

    public final String predefinedAccountRaw(final String twitterAccount) {
        return env.getProperty(twitterAccount + ".predefined");
    }

    public final List<String> predefinedAccount(final String twitterAccount) {
        final String allPredefinedAccountsRaw = predefinedAccountRaw(twitterAccount);
        Preconditions.checkNotNull(allPredefinedAccountsRaw);

        final String[] questionIds = allPredefinedAccountsRaw.split(",");
        return Lists.newArrayList(questionIds);
    }

}
