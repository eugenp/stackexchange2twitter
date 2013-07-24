package org.keyval.persistence.service;

import org.common.persistence.AbstractRawServicePersistenceIntegrationTest;
import org.common.persistence.IEntityOperations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.persistence.dao.IKeyValJpaDAO;
import org.keyval.persistence.model.KeyVal;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.TestStackexchangePersistenceJPAConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { KeyValPersistenceJPAConfig.class, TestStackexchangePersistenceJPAConfig.class })
public class KeyValPersistenceIntegrationTest extends AbstractRawServicePersistenceIntegrationTest<KeyVal> {

    @Autowired
    private IKeyValJpaDAO api;

    @Autowired
    private KeyValEntityOps entityOps;

    // tests

    // find - one - by name

    // find - all

    // count - all

    @Test
    /**/public void whenAllResourcesAreCounted_thenNoExceptions() {
        getApi().count();
    }

    // create

    // template method

    @Override
    protected IKeyValJpaDAO getApi() {
        return api;
    }

    @Override
    protected IEntityOperations<KeyVal> getEntityOps() {
        return entityOps;
    }

}
