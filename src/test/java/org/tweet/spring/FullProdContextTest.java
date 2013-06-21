package org.tweet.spring;

import org.common.spring.CommonPersistenceJPAConfig;
import org.gplus.spring.GplusContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.spring.StackexchangeConfig;
import org.stackexchange.spring.StackexchangeContextConfig;
import org.stackexchange.spring.StackexchangePersistenceJPAConfig;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {// @formatter:off 
    CommonPersistenceJPAConfig.class,
        
    KeyValPersistenceJPAConfig.class,
    
    TwitterConfig.class, 

    TwitterMetaPersistenceJPAConfig.class, 
    
    StackexchangeConfig.class, 
    StackexchangeContextConfig.class, 
    StackexchangePersistenceJPAConfig.class, 
    
    GplusContextConfig.class
}) // @formatter:on
public class FullProdContextTest {

    static {
        System.setProperty("persistenceTarget", "prod");
    }

    // tests

    @Test
    public final void whenContextIsInitalized_thenNoExceptions() {
        //
    }

}
