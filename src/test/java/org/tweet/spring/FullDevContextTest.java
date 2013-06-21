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
    // org.common.spring
    CommonPersistenceJPAConfig.class,
    
    // org.keyval.spring
    KeyValPersistenceJPAConfig.class,
    
    // org.tweet.spring
    TwitterConfig.class, 

    // org.tweet.meta.spring
    TwitterMetaPersistenceJPAConfig.class, 
    
    // org.stackexchange.spring
    StackexchangeConfig.class, 
    StackexchangeContextConfig.class, 
    StackexchangePersistenceJPAConfig.class, 
    
    // org.gplus.spring
    GplusContextConfig.class
}) // @formatter:on
public class FullDevContextTest {

    static {
        System.setProperty("persistenceTarget", "dev");
    }

    // tests

    @Test
    public final void whenContextIsInitalized_thenNoExceptions() {
        //
    }

}
