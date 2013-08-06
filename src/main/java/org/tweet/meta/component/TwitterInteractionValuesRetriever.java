package org.tweet.meta.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public final class TwitterInteractionValuesRetriever {

    @Autowired
    private Environment env;

    public TwitterInteractionValuesRetriever() {
        super();
    }

    // API

    /**
     * - twitter.user.valuable.retweets.min <br/>
     * - default = 6
     */
    public final int getMinRetweetsOfValuableUser() {
        return env.getProperty("twitter.user.valuable.retweets.min", Integer.class);
    }

    /**
     * - twitter.user.valuable.retweetsandmentions.min <br/>
     * - default = 20
     */
    public final int getMinRetweetsPlusMentionsOfValuableUser() {
        return env.getProperty("twitter.user.valuable.retweetsandmentions.min", Integer.class);
    }

    /**
     * - twitter.user.valuable.largeaccountretweets.max <br/>
     * - default = 90(%)
     */
    public final int getMaxPercentageOfLargeAccountRetweets() {
        return env.getProperty("twitter.user.valuable.largeaccountretweets.max", Integer.class);
    }

    /**
     * - twitter.user.valuable.followers.min <br/>
     * - default = 300
     */
    public final int getMinFolowersOfValuableUser() {
        return env.getProperty("twitter.user.valuable.followers.min", Integer.class);
    }

    /**
     * - twitter.user.valuable.tweets.min <br/>
     * - default = 300
     */
    public final int getMinTweetsOfValuableUser() {
        return env.getProperty("twitter.user.valuable.tweets.min", Integer.class);
    }

}
