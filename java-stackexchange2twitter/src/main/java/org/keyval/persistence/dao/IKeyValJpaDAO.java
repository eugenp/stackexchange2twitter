package org.keyval.persistence.dao;

import org.common.persistence.IOperations;
import org.keyval.persistence.model.KeyVal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IKeyValJpaDAO extends JpaRepository<KeyVal, Long>, IOperations<KeyVal> {

    KeyVal findByKey(final String key);

}
