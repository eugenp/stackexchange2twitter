package org.rss.persistence.dao;

import org.common.persistence.IOperations;
import org.rss.persistence.model.RssEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRssEntryJpaDAO extends JpaRepository<RssEntry, Long>, IOperations<RssEntry> {

    RssEntry findOneByLinkAndTwitterAccount(final String link, final String twitterAccount);

    RssEntry findOneByLink(final String link);

}
