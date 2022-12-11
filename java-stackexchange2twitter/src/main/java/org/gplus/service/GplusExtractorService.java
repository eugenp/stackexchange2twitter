package org.gplus.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.common.service.ContentExtractorLiveService;
import org.common.service.LinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.services.plus.model.Activity;

@Service
public class GplusExtractorService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GplusService gplusService;

    @Autowired
    private ContentExtractorLiveService contentExtractorService;

    @Autowired
    private LinkService linkService;

    public GplusExtractorService() {
        super();
    }

    // API

    public final String getBestTweetCandidate(final String topic) throws IOException {
        logger.info("Retrieving best tweet candidate from G+, on the topic= {}", topic);

        final List<Activity> activities = gplusService.search(topic);
        for (final Activity activity : activities) {
            final Pair<String, String> validTweetCandidate = getUrlOfValidTweetCandidate(activity.getObject().getContent());
            if (validTweetCandidate != null) {
                logger.info("Found tweet candidate from activity= {}", ActivityHelper.toString(activity));
                return validTweetCandidate.getRight() + " - " + validTweetCandidate.getLeft();
            }
        }

        return null;
    }

    /**
     * - note: may return null
     */
    final Pair<String, String> getUrlOfValidTweetCandidate(final String content) {
        logger.debug("Analyzing content of activity as source of tweet candidate; content= {}", content);

        final String mainUrl = linkService.extractUrl(content);
        if (mainUrl == null) {
            return null;
        }

        final String title = contentExtractorService.extractTitle(mainUrl);
        if ((title == null) || (title.length() > 120)) {
            return null;
        }

        // skipping documentation pages
        if (title.toLowerCase().contains("documentation")) {
            return null;
        }

        return new ImmutablePair<String, String>(mainUrl, title);
    }
}
