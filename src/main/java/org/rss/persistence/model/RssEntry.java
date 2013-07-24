package org.rss.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.common.persistence.IEntity;

@Entity
@Table(name = "rssentry", uniqueConstraints = @UniqueConstraint(columnNames = { "rssUri", "title" }))
public class RssEntry implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RSS_ENTRY_ID")
    private long id;

    @Column(nullable = false)
    private String twitterAccount;

    @Column(nullable = false)
    private String rssUri;

    @Column(nullable = false)
    private String title;

    public RssEntry() {
        super();
    }

    public RssEntry(final String twitterAccount, final String rssUri, final String title) {
        super();

        this.twitterAccount = twitterAccount;
        this.rssUri = rssUri;
        this.title = title;
    }

    // API

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

    public String getTwitterAccount() {
        return twitterAccount;
    }

    public void setTwitterAccount(final String twitterAccount) {
        this.twitterAccount = twitterAccount;
    }

    public String getRssUri() {
        return rssUri;
    }

    public void setRssUri(final String rssUri) {
        this.rssUri = rssUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    //

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rssUri == null) ? 0 : rssUri.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
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
        final RssEntry other = (RssEntry) obj;
        if (rssUri == null) {
            if (other.rssUri != null)
                return false;
        } else if (!rssUri.equals(other.rssUri))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
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
        final StringBuilder builder = new StringBuilder();
        builder.append("RssEntry [id=").append(id).append(", twitterAccount=").append(twitterAccount).append(", RSS Uri=").append(rssUri).append(", title=").append(title).append("]");
        return builder.toString();
    }

}
