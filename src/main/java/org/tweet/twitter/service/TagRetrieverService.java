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
public class TagRetrieverService {

    @Autowired
    private Environment env;

    public TagRetrieverService() {
        super();
    }

    // API

    // stack tags

    /** - note: may return null */
    final String stackTagsRaw(final String twitterAccount) {
        return env.getProperty(twitterAccount + ".stack.tags");
    }

    public final String stackTags(final String twitterAccount) {
        return Preconditions.checkNotNull(stackTagsRaw(twitterAccount), "No tags for account: " + twitterAccount);
    }

    public final String pickStackTag(final String twitterAccount) {
        final String stackTagsOfAccount = stackTags(twitterAccount);
        return pickOnTag(stackTagsOfAccount);
    }

    // twitter tags

    /** - note: may return null */
    final String twitterTagsRaw(final String twitterAccount) {
        return env.getProperty(twitterAccount + ".twitter.tags");
    }

    public final String twitterTags(final String twitterAccount) {
        return Preconditions.checkNotNull(twitterTagsRaw(twitterAccount), "No twitter tags for account: " + twitterAccount);
    }

    public final String pickTwitterTag(final String twitterAccount) {
        final String twitterTags = twitterTags(twitterAccount);
        return pickOnTag(twitterTags);
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
