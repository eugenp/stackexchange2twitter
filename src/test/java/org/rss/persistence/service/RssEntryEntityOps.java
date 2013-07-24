package org.rss.persistence.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.common.persistence.IEntityOperations;
import org.rss.persistence.model.RssEntry;
import org.springframework.stereotype.Component;

@Component
public final class RssEntryEntityOps implements IEntityOperations<RssEntry> {

    public RssEntryEntityOps() {
        super();
    }

    // API

    // template method

    @Override
    public final RssEntry createNewEntity() {
        return new RssEntry(randomAlphabetic(6), randomAlphabetic(6), randomAlphabetic(6));
    }

    @Override
    public final void invalidate(final RssEntry entity) {
        entity.setTitle(null);
        entity.setTwitterAccount(null);
    }

    @Override
    public final void change(final RssEntry entity) {
        entity.setTitle(randomAlphabetic(6));
    }

}
