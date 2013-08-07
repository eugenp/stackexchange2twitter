package org.tweet.meta.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;
import org.tweet.meta.component.TwitterInteractionValuesRetriever;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TweetMentionService;
import org.tweet.twitter.util.TweetUtil;
import org.tweet.twitter.util.TwitterAccountInteraction;

@Component
@Profile(SpringProfileUtil.LIVE)
public final class InteractionLiveStrategy {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserInteractionLiveService userInteractionLiveService;

    @Autowired
    TwitterInteractionValuesRetriever twitterInteraction;

    @Autowired
    TweetMentionService tweetMentionService;

    public InteractionLiveStrategy() {
        super();
    }

    // API

    public final TwitterAccountInteraction decideBestInteraction(final Tweet tweet) {
        final TwitterAccountInteraction bestInteractionWithAuthor = userInteractionLiveService.decideBestInteractionWithAuthorLive(tweet.getUser(), tweet.getFromUser());

        if (isTweetToPopular(tweet)) {
            final String tweetUrl = "https://twitter.com/" + tweet.getFromUser() + "/status/" + tweet.getId();
            logger.info("Far to popular tweet= {} - no point in retweeting...rt= {}; link= {}", TweetUtil.getText(tweet), tweet.getRetweetCount(), tweetUrl);
            if (bestInteractionWithAuthor.equals(TwitterAccountInteraction.Mention)) {
                return TwitterAccountInteraction.Mention;
            }
            return TwitterAccountInteraction.None;
        }

        return bestInteractionWithAuthor;
    }

    public final boolean shouldRetweetOld(final Tweet tweet) {
        if (isTweetToPopular(tweet)) {
            final String tweetUrl = "https://twitter.com/" + tweet.getFromUser() + "/status/" + tweet.getId();
            logger.info("Far to popular tweet= {} - no point in retweeting...rt= {}; link= {}", TweetUtil.getText(tweet), tweet.getRetweetCount(), tweetUrl);
            return false;
        }

        final TwitterAccountInteraction bestInteractionWithAuthor = userInteractionLiveService.decideBestInteractionWithAuthorLive(tweet.getUser(), tweet.getFromUser());
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
            if (containsValuableMentions(tweet.getText())) {
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

    private final boolean containsValuableMentions(final String text) {
        final List<String> mentions = tweetMentionService.extractMentions(text);
        for (final String mentionedUser : mentions) {
            if (userInteractionLiveService.decideBestInteractionWithAuthorLive(mentionedUser).equals(TwitterAccountInteraction.Mention)) {
                logger.error("(temp-error)More value in tweeting as is - the tweet has valuable mentions: {}", text);
                return true;
            }
        }

        // retweet is a catch-all default - TODO: now we need to decide if the tweet itself has more value tweeted than retweeted
        return false;
    }

    // util

    /**
     * - local
     */
    private final boolean isTweetToPopular(final Tweet tweet) {
        final boolean hasLessRtsThanTheTooPopularThreshold = tweet.getRetweetCount() < twitterInteraction.getMaxRetweetsForTweet();
        return !hasLessRtsThanTheTooPopularThreshold;
    }

}
