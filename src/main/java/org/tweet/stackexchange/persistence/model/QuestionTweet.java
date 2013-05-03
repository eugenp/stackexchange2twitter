package org.tweet.stackexchange.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.tweet.common.persistence.IEntity;

@Entity
public class QuestionTweet implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "QT_ID")
    private long id;

    @Column(nullable = false, unique = true)
    private String questionId;

    @Column(nullable = false)
    private String twitterAccount;

    @Column(nullable = true)
    private String soAccount;

    public QuestionTweet() {
        super();
    }

    public QuestionTweet(final String questionId, final String twitterAccount, final String soAccount) {
        super();

        this.questionId = questionId;
        this.twitterAccount = twitterAccount;
        this.soAccount = soAccount;
    }

    // API

    @Override
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(final String questionId) {
        this.questionId = questionId;
    }

    public String getTwitterAccount() {
        return twitterAccount;
    }

    public void setTwitterAccount(final String twitterAccount) {
        this.twitterAccount = twitterAccount;
    }

    public String getSoAccount() {
        return soAccount;
    }

    public void setSoAccount(final String soAccount) {
        this.soAccount = soAccount;
    }

    //

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((questionId == null) ? 0 : questionId.hashCode());
        result = prime * result + ((soAccount == null) ? 0 : soAccount.hashCode());
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
        final QuestionTweet other = (QuestionTweet) obj;
        if (questionId == null) {
            if (other.questionId != null)
                return false;
        } else if (!questionId.equals(other.questionId))
            return false;
        if (soAccount == null) {
            if (other.soAccount != null)
                return false;
        } else if (!soAccount.equals(other.soAccount))
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
        return "QuestionTweet [questionId=" + questionId + ", twitterAccount=" + twitterAccount + ", soAccount=" + soAccount + "]";
    }

}
