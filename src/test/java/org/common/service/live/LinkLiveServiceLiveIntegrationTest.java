package org.common.service.live;

import org.common.spring.CommonServiceConfig;
import org.common.util.LinkUtil;
import org.gplus.spring.GplusContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.util.SpringProfileUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonServiceConfig.class, GplusContextConfig.class })
@ActiveProfiles(SpringProfileUtil.LIVE)
public class LinkLiveServiceLiveIntegrationTest {

    @Autowired
    private LinkLiveService linkLiveService;

    // tests

    @Test
    public final void whenLinksToSeDomainsAreCounted_thenNoExceptions() {
        linkLiveService.countLinksToAnyDomain("@alexbowe No problem - 300GB to something that can be held in RAM is worth tweeting about :)", LinkUtil.seDomains);
    }

}
