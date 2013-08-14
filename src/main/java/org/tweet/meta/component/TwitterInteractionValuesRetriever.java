package org.tweet.meta.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.api.client.util.Preconditions;

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
     * - default = 4%
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

    /**
     * - twitter.value.pagestoanalyze <br/>
     * - default = 1
     */
    public int getPagesToAnalyze() {
        final Integer value = env.getProperty("twitter.value.pagestoanalyze", Integer.class);
        Preconditions.checkState(value > 0);
        return value;
    }

    /**
     * - twitter.value.largeaccount <br/>
     * - default = 3000
     */
    public int getLargeAccountDefinition() {
        return env.getProperty("twitter.value.largeaccount", Integer.class);
    }

    /**
     * When calculating <b>the value of an RETWEET interaction with a USER</b>, the number of followers of that account will add something to the score <i>Math.log(followerCount)</i><br/>
     * This percentage will be applied to that base score - the reason is simple - the value of a retweet is smaller than the value of a mention, so the actual value of the follower count is also a bit lower
     * - <b>twitter.retweet.score.followers.percentage</b> <br/>
     * - default = 50
     */
    public int getRetweetScoreFollowersPercentage() {
        return env.getProperty("twitter.retweet.score.followers.percentage", Integer.class);
    }

}
