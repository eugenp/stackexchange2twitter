package org.tweet.meta.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TweetMentionService;
import org.tweet.twitter.util.TweetUtil;
import org.tweet.twitter.util.TwitterInteraction;

@Component
@Profile(SpringProfileUtil.LIVE)
public final class InteractionLiveStrategy {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    InteractionLiveService userInteractionLiveService;

    @Autowired
    TweetMentionService tweetMentionService;

    public InteractionLiveStrategy() {
        super();
    }

    // API

    public final TwitterInteraction decideBestInteraction(final Tweet tweet) {
        final TwitterInteraction bestInteractionWithAuthor = userInteractionLiveService.decideBestInteractionWithAuthorLive(tweet.getUser(), tweet.getFromUser());
        final TwitterInteraction bestInteractionWithTweet = userInteractionLiveService.decideBestInteractionWithTweetNotAuthorLive(tweet);
        final String text = TweetUtil.getText(tweet);

        switch (bestInteractionWithAuthor) {
        case None:
            // there is no value in an interaction with the AUTHOR - if the TWEET itself has mention value - the tweet as is; if not, still tweet as is
            logger.info("Should not retweet tweet= {} because it's not worth interacting with the user= {}", text, tweet.getFromUser());
            return TwitterInteraction.None;
        case Mention:
            // we should mention the AUTHOR; if the TWEET itself has mention value as well - see which is more valuable; if not, mention
            if (bestInteractionWithTweet.equals(TwitterInteraction.Mention)) {
                // TODO: determine which is more valuable - mentioning the author or tweeting the tweet with the mentions it has
            }

            logger.info("Should add a mention of the original author= {} and tweet the new tweet= {} user= {}", tweet.getFromUser(), text);
            return TwitterInteraction.Mention;
        case Retweet:
            // we should retweet the AUTHOR; however, if the TWEET itself has mention value, that is more important => tweet as is (with mentions); if not, retweet it
            if (bestInteractionWithTweet.equals(TwitterInteraction.Mention)) {
                return TwitterInteraction.None;
            }

            return TwitterInteraction.Retweet;
            // TODO: is there any way to extract the mentions from the tweet entity?
            // retweet is a catch-all default - TODO: now we need to decide if the tweet itself has more value tweeted than retweeted
        default:
            throw new IllegalStateException();
        }
    }

    public final boolean shouldRetweetOld(final Tweet tweet) {
        final TwitterInteraction bestInteraction = decideBestInteraction(tweet);

        switch (bestInteraction) {
        case None:

            return false;
        case Mention:

            return false;
        case Retweet:

            return true;

        default:
            throw new IllegalStateException();
        }
    }

    // util

}
