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
        final TwitterInteraction bestInteractionWithTweet = userInteractionLiveService.decideBestInteractionWithTweetNotAuthor(tweet);
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
        if (userInteractionLiveService.isTweetToPopular(tweet)) {
            final String tweetUrl = "https://twitter.com/" + tweet.getFromUser() + "/status/" + tweet.getId();
            logger.info("Far to popular tweet= {} - no point in retweeting...rt= {}; link= {}", TweetUtil.getText(tweet), tweet.getRetweetCount(), tweetUrl);
            return false;
        }

        final TwitterInteraction bestInteractionWithAuthor = userInteractionLiveService.decideBestInteractionWithAuthorLive(tweet.getUser(), tweet.getFromUser());
        switch (bestInteractionWithAuthor) {
        case None:
            // either the tweet has no mention - in which case - OK
            // or the tweet has valuable mentions - in which case - still OK (since it will get tweeted as it is)
            final String text = TweetUtil.getText(tweet);
            logger.info("Should not retweet tweet= {} because it's not worth interacting with the user= {}", text, tweet.getFromUser());
            return false;
        case Mention:
            // final String newTextWithMention = tweetMentionService.addMention(tweet.getFromUser(), tweet.getText());
            // if the tweet has no mention itself - OK - TODO: add mention and tweet
            // if the tweet does have valuable mentions - we need to see which is more valuable - adding a mention and tweeting, or tweeting as is
            return true;
        case Retweet:
            // TODO: is there any way to extract the mentions from the tweet entity?
            if (userInteractionLiveService.containsValuableMentions(tweet.getText())) {
                return false; // do not retweet - just tweet
            }
            // retweet is a catch-all default - TODO: now we need to decide if the tweet itself has more value tweeted than retweeted
            return true;
        default:
            throw new IllegalStateException();
        }

        // TODO: OK, so the author may be worth interacting with - but if it contains mentions, then it may also be worth simply tweeting:
        // https://twitter.com/jameschesters/status/50510953187516416
        // https://twitter.com/LispDaily/status/364476542711504896
    }

    // util

}
