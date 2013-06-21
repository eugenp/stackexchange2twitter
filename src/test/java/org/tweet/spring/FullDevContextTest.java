package org.tweet.spring;

import org.gplus.spring.GplusContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.spring.StackexchangeConfig;
import org.stackexchange.spring.StackexchangeContextConfig;
import org.stackexchange.spring.StackexchangePersistenceJPAConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {// @formatter:off 
    TwitterConfig.class, 
    
    StackexchangeConfig.class, 
    StackexchangeContextConfig.class, 
    StackexchangePersistenceJPAConfig.class, 
    
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
