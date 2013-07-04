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
    public final String pickStackTagForAccount(final String accountName) {
        final String tagsAccount = Preconditions.checkNotNull(env.getProperty(accountName + ".stack.tags"), "No tags for account: " + accountName);
        return pickOnTag(tagsAccount);
    }

    /**
     * - note: may return null
     */
    public final String pickTwitterTagForAccount(final String accountName) {
        final String twitterTags = env.getProperty(accountName + ".twitter.tags");
        final String twitterTagsForAccount = Preconditions.checkNotNull(twitterTags, "No twitter tags for account: " + accountName);
        return pickOnTag(twitterTagsForAccount);
    }

    // util

    private final String pickOnTag(final String allTagsCommaSeparated) {
        final Iterable<String> split = Splitter.on(',').split(allTagsCommaSeparated);
        final List<String> tags = Lists.newArrayList(split);
        if (tags.isEmpty()) {
            return null;
        }

        return GenericUtil.pickOneGeneric(tags);
    }

}
