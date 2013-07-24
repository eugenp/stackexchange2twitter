package org.rss.persistence.dao;

import org.common.persistence.IOperations;
import org.rss.persistence.model.RssEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRssEntryJpaDAO extends JpaRepository<RssEntry, Long>, IOperations<RssEntry> {

    RssEntry findOneByRssUriAndTwitterAccount(final String rssUri, final String twitterAccount);

    RssEntry findOneByRssUri(final String rssUri);

}
