package org.tweet.meta.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.tweet.common.persistence.IEntity;
import org.tweet.stackexchange.util.SimpleTwitterAccount;

@Entity
public class Retweet implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "QT_ID")
    private long id;

    @Column(nullable = false, unique = true)
    private long tweetId;

    @Column(nullable = false)
    private String twitterAccount;

    public Retweet() {
        super();
    }

    public Retweet(final long tweetId, final SimpleTwitterAccount twitterAccount) {
        super();

        this.tweetId = tweetId;
        this.twitterAccount = twitterAccount.name();
    }

    public Retweet(final long tweetId, final String twitterAccount) {
        super();

        this.tweetId = tweetId;
        this.twitterAccount = twitterAccount;
    }

    // API

    @Override
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getTwitterAccount() {
        return twitterAccount;
    }

    public void setTwitterAccount(final String twitterAccount) {
        this.twitterAccount = twitterAccount;
    }

    public long getTweetId() {
        return tweetId;
    }

    public void setTweetId(final long tweetId) {
        this.tweetId = tweetId;
    }

    //

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (tweetId ^ (tweetId >>> 32));
        result = prime * result + ((twitterAccount == null) ? 0 : twitterAccount.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Retweet other = (Retweet) obj;
        if (tweetId != other.tweetId)
            return false;
        if (twitterAccount == null) {
            if (other.twitterAccount != null)
                return false;
        } else if (!twitterAccount.equals(other.twitterAccount))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Retweet [tweetId=" + tweetId + ", twitterAccount=" + twitterAccount + "]";
    }

}
