package org.tweet.twitter.service.live;

public interface ITwitterWriteLiveService {

    boolean retweet(String twitterAccount, long tweetId);

    boolean tweet(String twitterAccount, String tweetText);

}
