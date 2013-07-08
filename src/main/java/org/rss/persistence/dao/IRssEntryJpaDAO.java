package org.rss.persistence.dao;

import org.rss.persistence.model.RssEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IRssEntryJpaDAO extends JpaRepository<RssEntry, Long>, JpaSpecificationExecutor<RssEntry> {
    //
}
