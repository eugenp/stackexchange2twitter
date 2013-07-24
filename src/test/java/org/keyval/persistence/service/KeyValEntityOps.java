package org.keyval.persistence.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.common.persistence.IEntityOperations;
import org.keyval.persistence.model.KeyVal;
import org.springframework.stereotype.Component;

@Component
public final class KeyValEntityOps implements IEntityOperations<KeyVal> {

    public KeyValEntityOps() {
        super();
    }

    // API

    // template method

    @Override
    public final KeyVal createNewEntity() {
        return new KeyVal(randomAlphabetic(6), randomAlphabetic(6));
    }

    @Override
    public final void invalidate(final KeyVal entity) {
        entity.setKey(null);
        entity.setValue(null);
    }

    @Override
    public final void change(final KeyVal entity) {
        entity.setKey(randomAlphabetic(6));
    }

}
