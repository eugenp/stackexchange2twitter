package org.tweet.meta.persistence.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.common.persistence.IEntityOperations;
import org.springframework.stereotype.Component;
import org.stackexchange.util.IDUtil;
import org.tweet.meta.persistence.model.Retweet;

@Component
public final class RetweetEntityOps implements IEntityOperations<Retweet> {

    public RetweetEntityOps() {
        super();
    }

    // API

    // template method

    @Override
    public final Retweet createNewEntity() {
        return new Retweet(IDUtil.randomPositiveLong(), randomAlphabetic(6), randomAlphabetic(6));
    }

    @Override
    public final void invalidate(final Retweet entity) {
        entity.setText(null);
        entity.setTwitterAccount(null);
    }

    @Override
    public final void change(final Retweet entity) {
        entity.setText(randomAlphabetic(6));
    }

}
