package org.tweet.meta.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TwitterInteractionValuesRetriever {

    @Autowired
    private Environment env;

    public TwitterInteractionValuesRetriever() {
        super();
    }

    // API

    /**
     * - twitter.value.user.retweets.percentage.min <br/>
     * - default = 3%
     */
    public int getMinRetweetsPercentageOfValuableUser() {
        return env.getProperty("twitter.value.user.retweets.percentage.min", Integer.class);
    }

    /**
     * - twitter.value.user.retweetsandmentions.min <br/>
     * - default = 10%
     */
    public int getMinRetweetsPlusMentionsOfValuableUser() {
        return env.getProperty("twitter.value.user.retweetsandmentions.min", Integer.class);
    }

    /**
     * - twitter.value.user.largeaccountretweets.percentage.max <br/>
     * - default = 90%
     */
    public int getMaxLargeAccountRetweetsPercentage() {
        return env.getProperty("twitter.value.user.largeaccountretweets.percentage.max", Integer.class);
    }

    /**
     * - twitter.value.user.followers.min <br/>
     * - default = 300
     */
    public int getMinFolowersOfValuableUser() {
        return env.getProperty("twitter.value.user.followers.min", Integer.class);
    }

    /**
     * - twitter.value.user.tweets.min <br/>
     * - default = 300
     */
    public int getMinTweetsOfValuableUser() {
        return env.getProperty("twitter.value.user.tweets.min", Integer.class);
    }

    /**
     * - twitter.value.tweet.retweets.max <br/>
     * - default = 15
     */
    public int getMaxRetweetsForTweet() {
        return env.getProperty("twitter.value.tweet.retweets.max", Integer.class);
    }

}
