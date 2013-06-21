package org.keyval.persistence.dao;

import org.keyval.persistence.model.KeyVal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IKeyValJpaDAO extends JpaRepository<KeyVal, Long> {

    KeyVal findByKey(final String key);

}
