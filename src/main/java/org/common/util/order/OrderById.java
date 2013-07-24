package org.common.util.order;

import org.common.persistence.IEntity;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Longs;

public final class OrderById<T extends IEntity> extends Ordering<T> {

    public OrderById() {
        super();
    }

    // API

    @Override
    public final int compare(final T left, final T right) {
        return Longs.compare(left.getId(), right.getId());
    }

}
