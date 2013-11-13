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

    @Scheduled(cron = "0 0 */6 * * *")
    public void pollForErrors() {
        logger.info("Starting to poll for errors");

        logBannedRegExesMaybeErrors();
        logBannedCommercialContainsMaybeErrors();
        logBannedContainsMaybeErrors();

        logger.info("Done polling for errors");
    }

    private final void logBannedRegExesMaybeErrors() {
        for (final Map.Entry<String, Set<String>> entry : TwitterUtil.bannedRegExesMaybeErrors.entrySet()) {
            final Set<String> errors = entry.getValue();
            if (errors.isEmpty()) {
                continue;
            }
            final String finalErrorsAsString = asString(errors);

            final String expression = entry.getKey();
            logger.error("(new-analysis-commercial-1) - Rejecting by regular expression (maybe)=  " + expression + "; errors= \n" + finalErrorsAsString);
            errors.clear();
        }
    }

    private final void logBannedCommercialContainsMaybeErrors() {
        for (final Map.Entry<String, Set<String>> entry : TwitterUtil.bannedCommercialContainsMaybeErrors.entrySet()) {
            final Set<String> errors = entry.getValue();
            if (errors.isEmpty()) {
                continue;
            }
            final String finalErrorsAsString = asString(errors);

            final String token = entry.getKey();
            logger.error("(new-analysis-commercial-2) - Rejecting by contains (maybe)= " + token + "; errors= \n" + finalErrorsAsString);

            errors.clear();
        }
    }

    private final void logBannedContainsMaybeErrors() {
        for (final Map.Entry<String, Set<String>> entry : TwitterUtil.bannedContainsMaybeErrors.entrySet()) {
            final Set<String> errors = entry.getValue();
            if (errors.isEmpty()) {
                continue;
            }
            final String finalErrorsAsString = asString(errors);

            final String token = entry.getKey();
            logger.error("(new-analysis-2) - Rejecting by contains (maybe)= " + token + "; errors= \n" + finalErrorsAsString);

            errors.clear();
        }
    }

    private final String asString(final Set<String> errors) {
        if (errors.isEmpty()) {
            return null;
        }

        final StringBuilder errorBatch = new StringBuilder();
        for (final String error : errors) {
            errorBatch.append(error);
            errorBatch.append("\n");
        }
        final String finalErrorsAsString = errorBatch.toString();
        return finalErrorsAsString;
    }

}
