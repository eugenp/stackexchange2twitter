package org.tweet.twitter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.common.service.LinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stackexchange.util.GenericUtil;
import org.tweet.twitter.component.MinRtRetriever;
import org.tweet.twitter.component.TwitterHashtagsRetriever;
import org.tweet.twitter.util.TweetUtil;

import com.google.api.client.util.Preconditions;

/**
 * - local
 */
@Service
public class TweetMentionService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterHashtagsRetriever twitterHashtagsRetriever;

    @Autowired
    private MinRtRetriever minRtRetriever;

    @Autowired
    private LinkService linkService;

    public TweetMentionService() {
        super();
    }

    // API

    // checks

    public final List<String> extractMentions(final String tweet) {
        final List<String> result = new ArrayList<String>();

        final Pattern pattern = Pattern.compile("(?<!\\w)@[\\w]+");

        final Matcher matcher = pattern.matcher(tweet);
        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }

    // construct mention

    public final String addMention(final String authorUserToMention, final String textPotentiallyWithoutMentionOfAuthor) {
        Preconditions.checkNotNull(authorUserToMention);
        Preconditions.checkNotNull(textPotentiallyWithoutMentionOfAuthor);

        if (textPotentiallyWithoutMentionOfAuthor.toLowerCase().contains("@" + authorUserToMention.toLowerCase())) {
            return textPotentiallyWithoutMentionOfAuthor;
        }

        final String mentionOption = GenericUtil.pickOneGeneric(TweetUtil.goodSingleMentionVariants);
        String withMention = textPotentiallyWithoutMentionOfAuthor + mentionOption + authorUserToMention;
        if (withMention.length() < 141) {
            return withMention;
        }

        withMention = textPotentiallyWithoutMentionOfAuthor + " (@" + authorUserToMention + ")";
        if (withMention.length() < 141) {
            return withMention;
        }

        return textPotentiallyWithoutMentionOfAuthor;
    }

    // util

}
