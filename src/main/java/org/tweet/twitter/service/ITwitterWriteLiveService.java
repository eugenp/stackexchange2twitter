package org.tweet.twitter.service;

public interface ITwitterWriteLiveService {

    boolean retweet(String twitterAccount, long tweetId);

    boolean tweet(String twitterAccount, String tweetText);

}
