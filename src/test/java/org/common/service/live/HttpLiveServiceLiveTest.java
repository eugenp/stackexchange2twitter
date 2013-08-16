package org.common.service.live;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.common.service.LinkService;
import org.common.spring.CommonServiceConfig;
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
public class HttpLiveServiceLiveTest {

    @Autowired
    private HttpLiveService httpService;

    @Autowired
    private LinkService linkService;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoException() {
        //
    }

    @Test
    public final void whenRssUriIsExpandedScenario1_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String unshortenedUrl = httpService.expandSingleLevel("http://feedproxy.google.com/~r/Baeldung/~3/WK4JN2S5KCU/spring-nosuchbeandefinitionexception").getRight();
        System.out.println(unshortenedUrl);
        assertNotNull(unshortenedUrl);
        assertThat(unshortenedUrl, not(containsString("feedproxy")));
        assertThat(unshortenedUrl, containsString("baeldung"));
    }

    @Test
    public final void whenShortenedUriIsUnshortednedBySingleLevelScenario1_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String unshortenedUrl = httpService.expandSingleLevel("http://t.co/wCD5WnAFGi").getRight();
        System.out.println(unshortenedUrl);
        assertNotNull(unshortenedUrl);
        assertThat(unshortenedUrl, not(containsString("t.co")));
    }

    @Test
    public final void whenStandardUriIsUnshortednedBySingleLevelScenario1_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String url = "http://www.yahoo.com";
        final String unshortenedUrl = httpService.expandSingleLevel(url).getRight();
        System.out.println(unshortenedUrl);
        assertNotNull(unshortenedUrl);
        assertThat(unshortenedUrl, equalTo(url));
    }

    @Test
    public final void givenUrlIsInvalid_whenExpanding_thenNoExceptions() throws IOException {
        httpService.expandInternal("http://www.marketwatch.com/enf/rss.asp?guid={3B615536-E289-11E2-ACAD-002128040CF6}");
    }

    @Test
    public final void whenShortenedUriIsUnshortednedScenario1_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String unshortenedUrl = httpService.expandInternal("http://t.co/wCD5WnAFGi");
        System.out.println(unshortenedUrl);
        assertNotNull(unshortenedUrl);
        assertFalse(linkService.isKnownShortenedUrl(unshortenedUrl));
    }

    @Test
    public final void whenShortenedUriIsUnshortednedScenario2_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String unshortenedUrl = httpService.expandInternal("http://t.co/qefnsZ0ZoF");
        System.out.println(unshortenedUrl);
        assertNotNull(unshortenedUrl);
        assertFalse(linkService.isKnownShortenedUrl(unshortenedUrl));
    }

    @Test
    public final void whenShortenedUriIsUnshortednedScenario3_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String unshortenedUrl = httpService.expandInternal("http://t.co/0ibisJsGBS");
        System.out.println(unshortenedUrl);
        assertNotNull(unshortenedUrl);
        assertFalse(linkService.isKnownShortenedUrl(unshortenedUrl));
    }

    @Test
    public final void whenShortenedUriIsUnshortednedScenario4_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String unshortenedUrl = httpService.expandInternal("https://www.digitalocean.com/community/articles/securing-mysql-and-mariadb-databases-in-a-linux-vps");
        System.out.println(unshortenedUrl);
        assertNotNull(unshortenedUrl);
        assertFalse(linkService.isKnownShortenedUrl(unshortenedUrl));
    }

    @Test
    public final void whenShortenedUriIsUnshortednedScenario5_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String unshortenedUrl = httpService.expandInternal("http://t.co/8Hcmxg1tyt");
        System.out.println(unshortenedUrl);
        assertNotNull(unshortenedUrl);
        assertFalse(linkService.isKnownShortenedUrl(unshortenedUrl));
    }

    @Test
    public final void whenShortenedUriIsUnshortednedScenario6_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String unshortenedUrl = httpService.expandInternal("http://t.co/pNtzBZ5NAo");
        System.out.println(unshortenedUrl);
        assertNotNull(unshortenedUrl);
        assertFalse(linkService.isKnownShortenedUrl(unshortenedUrl));
    }

    @Test
    public final void whenShortenedUriIsUnshortednedScenario7_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String unshortenedUrl = httpService.expandInternal("http://bit.ly/13jEoS1");
        System.out.println(unshortenedUrl);
        assertNotNull(unshortenedUrl);
        assertFalse(linkService.isKnownShortenedUrl(unshortenedUrl));
    }

    @Test
    public final void whenShortenedUriIsUnshortednedScenario8_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String unshortenedUrl = httpService.expandInternal("http://t.co/y6yabbABq0");
        System.out.println(unshortenedUrl);
        assertNotNull(unshortenedUrl);
        assertFalse(linkService.isKnownShortenedUrl(unshortenedUrl));
    }

    @Test
    public final void whenShortenedUriIsUnshortednedScenario9_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String unshortenedUrl = httpService.expandInternal("http://www.marketwatch.com/enf/rss.asp?guid={3B615536-E289-11E2-ACAD-002128040CF6}");
        System.out.println(unshortenedUrl);
        assertNotNull(unshortenedUrl);
        assertFalse(linkService.isKnownShortenedUrl(unshortenedUrl));
    }

    // invalid urls

    @Test
    public final void givenInvalidUrl1_whenUnshortening_thenException() throws ClientProtocolException, IOException {
        final String unshortenedUrl = httpService.expand("http://t.co/nzFMCOOdEw");
        assertNull(unshortenedUrl);
    }

    @Test
    public final void givenInvalidUrl2_whenUnshortening_thenNotOK() throws ClientProtocolException, IOException {
        final String unshortenedUrl = httpService.expand("http://web.blackberry.com/business/software/bes-10.html");
        assertNull(unshortenedUrl);
    }

    // is homepage url

    @Test
    public final void givenUrlUnshortened_whenVerifyingIfUrlIsHomepage_thenResultIsCorrect() throws ClientProtocolException, IOException {
        final String candidateUrl = httpService.expandInternal("http://bit.ly/N7vAX");
        System.out.println(candidateUrl);
        assertTrue(linkService.isHomepageUrl(candidateUrl));
    }

    // util

}
