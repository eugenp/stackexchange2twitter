package org.tweet.spring;

import static org.junit.Assert.assertNotNull;
import static org.tweet.spring.util.SpringProfileUtil.DEPLOYED;
import static org.tweet.spring.util.SpringProfileUtil.LIVE;
import static org.tweet.spring.util.SpringProfileUtil.PRODUCTION;
import static org.tweet.spring.util.SpringProfileUtil.WRITE;

import org.classification.spring.ClassificationConfig;
import org.common.spring.CommonContextConfig;
import org.common.spring.CommonPersistenceJPAConfig;
import org.common.spring.MyApplicationContextInitializerProv;
import org.gplus.spring.GplusContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.persistence.dao.IKeyValJpaDAO;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.rss.spring.RssContextConfig;
import org.rss.spring.RssPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.persistence.dao.IQuestionTweetJpaDAO;
import org.stackexchange.spring.StackexchangeConfig;
import org.stackexchange.spring.StackexchangeContextConfig;
import org.stackexchange.spring.StackexchangePersistenceJPAConfig;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.spring.TwitterMetaConfig;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {// @formatter:off 
    // org.common.spring
    CommonPersistenceJPAConfig.class,
    // PersistenceJPACommonConfig.class, // imported
    CommonContextConfig.class, 
    
    // org.classification
    ClassificationConfig.class,

    // org.rss
    RssContextConfig.class,
    RssPersistenceJPAConfig.class,
    
    // org.keyval.spring
    KeyValPersistenceJPAConfig.class,
    
    // org.tweet.spring
    TwitterConfig.class, 
    TwitterLiveConfig.class, 
    // SetupPersistenceTestConfig.class, 
    
    // org.tweet.meta.spring
    TwitterMetaPersistenceJPAConfig.class, 
    TwitterMetaConfig.class, 
    
    // org.stackexchange.spring
    StackexchangeConfig.class, 
    StackexchangeContextConfig.class, 
    StackexchangePersistenceJPAConfig.class, 
    
    // org.gplus.spring
    GplusContextConfig.class
}) // @formatter:on
@ActiveProfiles({ DEPLOYED, LIVE, PRODUCTION, WRITE })
public class DevFullContextManualTest {

    static {
        System.setProperty(MyApplicationContextInitializerProv.PERSISTENCE_TARGET_KEY, "dev");
    }

    @Autowired
    private ApplicationContext appContext;

    // tests

    @Test
    public final void whenContextIsInitalized_thenNoExceptions() {
        assertNotNull(appContext.getBean(IQuestionTweetJpaDAO.class));
        assertNotNull(appContext.getBean(IRetweetJpaDAO.class));
        assertNotNull(appContext.getBean(IKeyValJpaDAO.class));
    }

}
