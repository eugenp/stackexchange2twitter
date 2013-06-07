package org.common.persistence.dao;

import org.common.persistence.model.KeyVal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IKeyValJpaDAO extends JpaRepository<KeyVal, Long> {

    KeyVal findByKey(final String key);

}
