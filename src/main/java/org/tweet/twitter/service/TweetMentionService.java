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
import org.tweet.twitter.component.MinRtRetriever;
import org.tweet.twitter.component.TwitterHashtagsRetriever;

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

    /**
     * - example: <i>RT @someuser: Original Tweet Stuff</i>
     */
    public final boolean isRetweetMention(final String potentialTweet) {
        final boolean startsWith = potentialTweet.startsWith("RT @");
        if (startsWith) {
            return true;
        }
        final boolean contains = potentialTweet.contains("RT @");
        if (contains) {
            return true;
        }

        final boolean containsRtInfo = potentialTweet.matches(".*RT.{0,3}@.*");
        if (containsRtInfo) {
            return true;
        }

        return false;
    }

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

    public final String addMention(final String authorUserToMention, final String textWithoutMentionOfAuthor) {

        return null;
    }

    // util

}
