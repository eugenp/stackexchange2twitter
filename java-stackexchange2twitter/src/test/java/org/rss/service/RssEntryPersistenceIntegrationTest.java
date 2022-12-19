package org.rss.service;

import org.common.persistence.AbstractRawServicePersistenceIntegrationTest;
import org.common.persistence.IEntityOperations;
import org.common.spring.MyApplicationContextInitializerProv;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rss.persistence.dao.IRssEntryJpaDAO;
import org.rss.persistence.model.RssEntry;
import org.rss.persistence.service.RssEntryEntityOps;
import org.rss.spring.RssPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RssPersistenceJPAConfig.class })
public class RssEntryPersistenceIntegrationTest extends AbstractRawServicePersistenceIntegrationTest<RssEntry> {

    static {
        System.setProperty(MyApplicationContextInitializerProv.PERSISTENCE_TARGET_KEY, "test");
    }

    @Autowired
    private IRssEntryJpaDAO api;

    @Autowired
    private RssEntryEntityOps entityOps;

    // tests

    // find - one - by name

    // find - all

    // count - all

    @Test
    /**/public void whenAllResourcesAreCounted_thenNoExceptions() {
        getApi().count();
    }

    // create

    // update

    // template method

    @Override
    protected IRssEntryJpaDAO getApi() {
        return api;
    }

    @Override
    protected final IEntityOperations<RssEntry> getEntityOps() {
        return entityOps;
    }

}
