package org.common.persistence;

public interface IEntityOperations<T extends IEntity> {

    T createNewEntity();

    void invalidate(final T entity);

    void change(final T resource);

}
