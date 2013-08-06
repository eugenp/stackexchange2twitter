package org.tweet.twitter.util;

public enum TwitterAccountInteraction {

    /**
     * No value in interacting with the user
     */
    None,
    /**
     * Best way to interact with this user is <b>to mention it</b>
     */
    Mention,
    /**
     * Best way to interact with this user is <b>to retweet it</b><p/>
     * - <b>note</b>: this is the fallback - if there is value in interacting with the user, the default is going to be the Retweet <br/>
     * that doesn't necessarily mean that the tweet itself should be retweeted if there's more value in tweeting it (may contain good mentions)
     */
    Retweet

}
