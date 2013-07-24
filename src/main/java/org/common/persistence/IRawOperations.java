package org.common.persistence;

import org.springframework.data.repository.CrudRepository;

public interface IRawOperations<T> extends CrudRepository<T, Long> {
    //
}
