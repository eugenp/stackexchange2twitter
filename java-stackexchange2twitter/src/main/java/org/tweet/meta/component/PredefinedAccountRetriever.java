package org.tweet.meta.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.stackexchange.util.GenericUtil;

import com.google.api.client.util.Preconditions;

@Component
public final class PredefinedAccountRetriever {

    @Autowired
    private Environment env;

    public PredefinedAccountRetriever() {
        super();
    }

    // API

    public final List<String> predefinedAccount(final String twitterAccount) {
        final String allPredefinedAccountsRaw = Preconditions.checkNotNull(predefinedAccountRaw(twitterAccount));
        return GenericUtil.breakApart(allPredefinedAccountsRaw);
    }

    final String predefinedAccountRaw(final String twitterAccount) {
        return env.getProperty(twitterAccount + ".predefined");
    }

}
