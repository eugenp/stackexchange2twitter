package org.tweet.meta.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;
import org.tweet.meta.component.TwitterInteractionValuesRetriever;
import org.tweet.spring.util.SpringProfileUtil;
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

    public InteractionLiveStrategy() {
        super();
    }

    // API

    public final boolean shouldRetweet(final Tweet tweet) {
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
            // if the tweet has no mention itself - OK - TODO: add mention and tweet
            // if the tweet does have valuable mentions - we need to see which is more valuable - adding a mention and tweeting, or tweeting as is
            return true;
        case Retweet:
            return true;
        default:
            throw new IllegalStateException();
        }

        // TODO: OK, so the author may be worth interacting with - but if it contains mentions, then it may also be worth simply tweeting:
        // https://twitter.com/jameschesters/status/50510953187516416
        // https://twitter.com/LispDaily/status/364476542711504896
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
