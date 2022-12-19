package org.tweet.twitter.service.live;

import org.springframework.social.twitter.api.Tweet;

public interface ITwitterWriteLiveService {

    boolean retweet(String twitterAccount, long tweetId);

    boolean tweet(String twitterAccount, String textToTweet);

    boolean tweet(String twitterAccount, String textToTweet, Tweet originalTweet);

}
