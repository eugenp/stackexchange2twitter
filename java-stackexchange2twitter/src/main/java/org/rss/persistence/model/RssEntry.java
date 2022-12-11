package org.rss.persistence.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.common.persistence.IEntity;

@Entity
@Table(name = "rssentry", uniqueConstraints = @UniqueConstraint(columnNames = { "link", "title", "twitterAccount" }))
public class RssEntry implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RSS_ENTRY_ID")
    private long id;

    @Column(nullable = false)
    private String twitterAccount;

    /** The actual link to the article */
    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Date originalPublishDate;

    @Column(nullable = false)
    private Date when;

    public RssEntry() {
        super();
    }

    public RssEntry(final String twitterAccount, final String link, final String title, final Date originalPublishDate, final Date when) {
        super();

        this.twitterAccount = twitterAccount;
        this.link = link;
        this.title = title;
        this.originalPublishDate = originalPublishDate;

        this.when = when;
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

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Date getOriginalPublishDate() {
        return originalPublishDate;
    }

    public void setOriginalPublishDate(final Date originalPublishDate) {
        this.originalPublishDate = originalPublishDate;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(final Date when) {
        this.when = when;
    }

    //

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((link == null) ? 0 : link.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((twitterAccount == null) ? 0 : twitterAccount.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RssEntry other = (RssEntry) obj;
        if (link == null) {
            if (other.link != null) {
                return false;
            }
        } else if (!link.equals(other.link)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        if (twitterAccount == null) {
            if (other.twitterAccount != null) {
                return false;
            }
        } else if (!twitterAccount.equals(other.twitterAccount)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("RssEntry [twitterAccount=").append(twitterAccount).append(", rssUri=").append(link).append(", title=").append(title).append(", originalPublishDate=").append(originalPublishDate).append(", when=").append(when).append("]");
        return builder.toString();
    }

}
