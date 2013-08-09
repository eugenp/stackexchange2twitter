package org.common.service.live;

import org.common.service.LinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.tweet.spring.util.SpringProfileUtil;

@Service
@Profile(SpringProfileUtil.LIVE)
public class LinkLiveService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpLiveService httpLiveService;

    @Autowired
    private LinkService linkService;

    public LinkLiveService() {
        super();
    }

    // API

    // count links

    /**
     * - <b>live</b><br/>
     */
    public final boolean containsLinkToDomain(final String tweet, final String domain) {
        final String mainUrl = linkService.extractUrl(tweet);
        final String mainUrlExpanded = httpLiveService.expand(mainUrl);
        if (mainUrlExpanded == null) {
            return false;
        }
        if (!mainUrlExpanded.contains(domain)) {
            return false;
        }

        return true;
    }

    /**
     * - <b>live</b><br/>
     */
    public final int countLinksToDomain(final Iterable<String> tweets, final String domain) {
        int count = 0;
        for (final String tweet : tweets) {
            final String mainUrl = linkService.extractUrl(tweet);
            final String mainUrlExpanded = httpLiveService.expand(mainUrl);
            if (mainUrlExpanded == null) {
                continue;
            }
            if (mainUrlExpanded.contains(domain)) {
                count++;
            }
        }

        return count;
    }

    /**
     * - <b>live</b><br/>
     */
    public final int countLinksToAnyDomain(final Iterable<String> tweets, final Iterable<String> domains) {
        int count = 0;
        for (final String tweet : tweets) {
            final String mainUrl = linkService.extractUrl(tweet);
            final String mainUrlExpanded = httpLiveService.expand(mainUrl);
            if (mainUrlExpanded == null) {
                // temporary error - go to debug when I'm done
                // it does happen and it may be worth investigating why - for example:
                logger.error("Unable to expand link= {} \nfrom tweet: {}", mainUrl, tweet);
                continue;
            }
            for (final String domain : domains) {
                if (mainUrlExpanded.contains(domain)) {
                    count++;
                    continue;
                }
            }
        }

        return count;
    }

    // util

}
