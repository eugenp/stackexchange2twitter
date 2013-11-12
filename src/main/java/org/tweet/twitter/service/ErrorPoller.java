package org.tweet.twitter.service;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tweet.twitter.util.TwitterUtil;

@Component
public class ErrorPoller {
    final static Logger logger = LoggerFactory.getLogger(ErrorPoller.class);

    @Scheduled(cron = "0 0 */3 * * *")
    public void pollForErrors() {
        logger.info("Starting to poll for errors");

        for (final Map.Entry<String, Set<String>> entry : TwitterUtil.bannedRegExesMaybeErrors.entrySet()) {
            final String expression = entry.getKey();
            final Set<String> errors = entry.getValue();
            if (errors.isEmpty()) {
                continue;
            }

            final StringBuilder errorBatch = new StringBuilder();
            for (final String error : errors) {
                errorBatch.append(error);
                errorBatch.append("\n");
            }
            logger.error("(new-analysis-commercial) - Rejecting by regular expression (maybe)=  " + expression + "; errors= \n" + errorBatch.toString());
            errors.clear();
        }

        logger.info("Done polling for errors");
    }

}
