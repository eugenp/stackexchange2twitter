package org.tweet.stackexchange.persistence.setup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.PersistenceJPATestConfig;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.stackexchange.persistence.dao.IQuestionTweetJpaDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceJPATestConfig.class })
@ActiveProfiles(SpringProfileUtil.DEPLOYED)
public class StackexchangeSetupPersistenceIntegrationTest {

    @Autowired
    private IQuestionTweetJpaDAO questionTweetApi;

    @Autowired
    private StackexchangeSetup stackexchangeSetup;

    // tests

    @Test
    public final void when_then() {
        //
    }

}
