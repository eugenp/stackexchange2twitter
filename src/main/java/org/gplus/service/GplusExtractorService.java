package org.gplus.service;

import java.io.IOException;
import java.util.List;

import org.common.service.ContentExtractorService;
import org.common.text.TextUtils;
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
    private ContentExtractorService contentExtractorService;

    public GplusExtractorService() {
        super();
    }

    // API

    public final String getBestTweetCandidate(final String topic) throws IOException {
        logger.info("Retrieving best tweet candidate from G+, on the topic = {}", topic);

        final List<Activity> activities = gplusService.search(topic);
        for (final Activity activity : activities) {
            if (isActivityTweetCandidate(activity.getObject().getContent())) {
                return activity.getObject().getContent();
            }
        }

        return null;
    }

    final boolean isActivityTweetCandidate(final String content) {
        logger.debug("Analyzing content of activity as source of tweet candidate; content = {}", content);

        final List<String> extractedUrls = TextUtils.extractUrls(content);
        final String mainUrl = TextUtils.determineMainUrl(extractedUrls);
        if (mainUrl == null) {
            return false;
        }

        final String title = contentExtractorService.extractTitle(mainUrl);
        return (title != null) && (title.length() < 120);
    }

}
