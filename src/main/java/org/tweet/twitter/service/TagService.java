package org.tweet.twitter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.stackexchange.util.GenericUtil;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

@Service
public class TagService {

    @Autowired
    private Environment env;

    public TagService() {
        super();
    }

    // API

    /**
     * - note: may return null
     */
    public final String pickTagForAccount(final String accountName) {
        final String tagsAccount = Preconditions.checkNotNull(env.getProperty(accountName + ".tags"), "No tags for account: " + accountName);
        final Iterable<String> split = Splitter.on(',').split(tagsAccount);
        final List<String> tags = Lists.newArrayList(split);
        if (tags.isEmpty()) {
            return null;
        }

        return GenericUtil.pickOneGeneric(tags);
    }

}
