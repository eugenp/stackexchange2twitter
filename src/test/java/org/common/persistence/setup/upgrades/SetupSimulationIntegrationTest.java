package org.common.persistence.setup.upgrades;

import org.common.spring.CommonContextConfig;
import org.common.spring.CommonPersistenceJPAConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class, CommonPersistenceJPAConfig.class, KeyValPersistenceJPAConfig.class, TwitterConfig.class, TwitterLiveConfig.class, TwitterMetaPersistenceJPAConfig.class })
@ActiveProfiles({ SpringProfileUtil.DEPLOYED, SpringProfileUtil.LIVE })
public class SetupSimulationIntegrationTest {

    static {
        System.setProperty("persistenceTarget", "prod");
    }

    @Autowired
    private AddTextToRetweetsUpgrader addTextToRetweetsUpgrader;

    // fixtures

    // tests

    @Test
    public final void whenRecreatingTheTweetedQuestions_thenNoExceptions() {
        addTextToRetweetsUpgrader.addTextToRetweets();
    }

}
