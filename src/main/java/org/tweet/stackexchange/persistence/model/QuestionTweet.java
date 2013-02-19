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

    private String account;

    public QuestionTweet() {
        super();
    }

    public QuestionTweet(final String questionId, final String account) {
        super();

        this.questionId = questionId;
        this.account = account;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(final String account) {
        this.account = account;
    }

    //

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((account == null) ? 0 : account.hashCode());
        result = prime * result + ((questionId == null) ? 0 : questionId.hashCode());
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
        if (account == null) {
            if (other.account != null)
                return false;
        } else if (!account.equals(other.account))
            return false;
        if (questionId == null) {
            if (other.questionId != null)
                return false;
        } else if (!questionId.equals(other.questionId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("QuestionTweet [questionId=").append(questionId).append(", account=").append(account).append("]");
        return builder.toString();
    }

}
