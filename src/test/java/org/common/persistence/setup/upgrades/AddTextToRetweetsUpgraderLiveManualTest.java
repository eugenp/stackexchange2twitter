package org.common.persistence.setup.upgrades;

import org.common.persistence.setup.upgrades.live.nolonger.IAddTextToRetweetsUpgrader;
import org.common.spring.CommonPersistenceJPAConfig;
import org.common.spring.CommonServiceConfig;
import org.common.spring.MyApplicationContextInitializerProv;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonServiceConfig.class, CommonPersistenceJPAConfig.class, KeyValPersistenceJPAConfig.class, TwitterConfig.class, TwitterLiveConfig.class, TwitterMetaPersistenceJPAConfig.class })
@ActiveProfiles({ SpringProfileUtil.DEPLOYED, SpringProfileUtil.LIVE })
public class AddTextToRetweetsUpgraderLiveManualTest {

    static {
        System.setProperty(MyApplicationContextInitializerProv.PERSISTENCE_TARGET_KEY, "prod");
    }

    @Autowired
    private IAddTextToRetweetsUpgrader addTextToRetweetsUpgrader;

    // fixtures

    // tests

    @Test
    public final void whenRecreatingTheTweetedQuestions_thenNoExceptions() {
        addTextToRetweetsUpgrader.addTextOfRetweets();
    }

    @Test
    public final void whenRecreatingTheTweetedQuestionsOnSpecificAccount_thenNoExceptions() {
        addTextToRetweetsUpgrader.addTextOfRetweetsOnAccount(TwitterAccountEnum.BestScala.name());
    }

}
