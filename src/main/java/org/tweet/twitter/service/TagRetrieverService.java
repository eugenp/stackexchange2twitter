package org.tweet.twitter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.stackexchange.util.GenericUtil;

import com.google.common.base.Preconditions;

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
        return Preconditions.checkNotNull(stackTagsRaw(twitterAccount), "No tags for twitterAccount= " + twitterAccount);
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

    public final String twitterTagsAsString(final String twitterAccount) {
        return Preconditions.checkNotNull(twitterTagsRaw(twitterAccount), "No twitter tags for twitterAccount= " + twitterAccount);
    }

    public final List<String> twitterTags(final String twitterAccount) {
        final String twitterTagsAsString = twitterTagsAsString(twitterAccount);
        return GenericUtil.breakApart(twitterTagsAsString);
    }

    public final String pickTwitterTag(final String twitterAccount) {
        final String twitterTags = twitterTagsAsString(twitterAccount);
        return pickOnTag(twitterTags);
    }

    // util

    private final String pickOnTag(final String allTagsCommaSeparated) {
        final List<String> tags = GenericUtil.breakApart(allTagsCommaSeparated);
        if (tags.isEmpty()) {
            return null;
        }

        return GenericUtil.pickOneGeneric(tags);
    }

}
