package org.rss.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.common.persistence.IEntity;

@Entity
public class RssEntry implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RSS_ENTRY_ID")
    private long id;

    @Column(nullable = false)
    private String twitterAccount;

    @Column(nullable = false)
    private String blog;

    @Column(nullable = false)
    private String title;

    public RssEntry() {
        super();
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

    public String getBlog() {
        return blog;
    }

    public void setBlog(final String blog) {
        this.blog = blog;
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
        result = prime * result + ((blog == null) ? 0 : blog.hashCode());
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
        if (blog == null) {
            if (other.blog != null)
                return false;
        } else if (!blog.equals(other.blog))
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
        builder.append("RssEntry [id=").append(id).append(", twitterAccount=").append(twitterAccount).append(", blog=").append(blog).append(", title=").append(title).append("]");
        return builder.toString();
    }

}
