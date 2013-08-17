package org.common.service.live;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.common.spring.CommonServiceConfig;
import org.common.util.LinkUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.live.TwitterReadLiveService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonServiceConfig.class, TwitterConfig.class, TwitterLiveConfig.class })
@ActiveProfiles(SpringProfileUtil.LIVE)
public class LinkLiveServiceLiveTest {

    @Autowired
    private LinkLiveService linkLiveService;

    @Autowired
    private TwitterReadLiveService twitterReadLiveService;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoException() {
        //
    }

    @Test
    public final void whenLinksToSeDomainsAreCounted_thenNoExceptions() {
        linkLiveService.countLinksToAnyDomainRaw("@alexbowe No problem - 300GB to something that can be held in RAM is worth tweeting about :)", LinkUtil.seDomains);
    }

    @Test
    public final void whenCountingLinksToDomainInTweet1_thenFound() {
        final Tweet tweet = twitterReadLiveService.findOne(368703813277847552l);
        final int found = linkLiveService.countLinksToAnyDomain(tweet, LinkUtil.seDomains);
        assertThat(found, greaterThan(0));
    }

    @Test
    public final void whenCountingLinksToDomainInTweet2_thenFound() {
        final boolean found = linkLiveService.hasLinksToAnyDomain("When should one use the following: #Amazon #EC2, Google App Engine, Microsoft Azure and http://t.co/44zGWFUyUd? - http://t.co/8o3C12Fruc", LinkUtil.seDomains);
        assertThat(found, is(true));
    }

}
