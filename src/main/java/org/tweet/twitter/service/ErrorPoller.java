package org.tweet.twitter.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tweet.twitter.util.ErrorUtil;

import com.google.api.client.util.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Component
public final class ErrorPoller {
    final static Logger logger = LoggerFactory.getLogger(ErrorPoller.class);

    class EntryByExpressionPredicate implements Predicate<Entry<String, Set<String>>> {

        private String expression;

        public EntryByExpressionPredicate(final String expression) {
            super();

            this.expression = Preconditions.checkNotNull(expression);
        }

        @Override
        public final boolean apply(final Entry<String, Set<String>> input) {
            final String expressionInInput = input.getKey();
            return expressionInInput.contains(expression);
        }
    }

    public ErrorPoller() {
        super();
    }

    // API

    @Scheduled(cron = "0 0 */12 * * *")
    public void pollForErrors() {
        logger.info("Starting to poll for errors");

        logBannedRegExesMaybeErrors();
        logBannedCommercialRegExesMaybeErrors();

        logBannedCommercialContainsMaybeErrors();

        logBannedContainsMaybeForAnalysisErrors();

        logBannedContainsMaybeForTweetingErrors();

        logRejectedByClassifierJobErrors();

        logger.info("Done polling for errors");
    }

    // UTIL

    final void logBannedRegExesMaybeErrors() {
        final Set<Entry<String, Set<String>>> temporaryEntrySet = Sets.newHashSet(ErrorUtil.bannedRegExesMaybeErrors.entrySet());

        final List<Entry<String, Set<String>>> errorsForWin = Lists.newArrayList(Iterables.filter(temporaryEntrySet, new EntryByExpressionPredicate("win")));
        final List<Entry<String, Set<String>>> errorsForDeal = Lists.newArrayList(Iterables.filter(temporaryEntrySet, new EntryByExpressionPredicate("deal")));
        temporaryEntrySet.removeAll(errorsForWin);
        temporaryEntrySet.removeAll(errorsForDeal);

        logAll(errorsForWin, "(new-analysis-1) - Rejecting by regular expression (maybe)=  ; errors= \n");
        logAll(errorsForDeal, "(new-analysis-1) - Rejecting by regular expression (maybe)=  ; errors= \n");
        logEach(temporaryEntrySet, "(new-analysis-1) - Rejecting by regular expression (maybe)=  ");
    }

    final void logBannedCommercialRegExesMaybeErrors() {
        final Set<Entry<String, Set<String>>> temporaryEntrySet = Sets.newHashSet(ErrorUtil.bannedCommercialRegExesMaybeErrors.entrySet());

        logEach(temporaryEntrySet, "(new-analysis-commercial-1) - Rejecting by regular expression (maybe)=  ");
    }

    final void logBannedCommercialContainsMaybeErrors() {
        final Set<Entry<String, Set<String>>> temporaryEntrySet = Sets.newHashSet(ErrorUtil.bannedCommercialContainsMaybeErrors.entrySet());

        logEach(temporaryEntrySet, "(new-analysis-commercial-2) - Rejecting by contains (maybe)= ");
    }

    final void logBannedContainsMaybeForAnalysisErrors() {
        final Set<Entry<String, Set<String>>> temporaryEntrySet = Sets.newHashSet(ErrorUtil.bannedContainsMaybeErrorsForAnalysis.entrySet());

        logEach(temporaryEntrySet, "(new-analysis-2) - Rejecting by contains for analysis (maybe)= ");
    }

    final void logBannedContainsMaybeForTweetingErrors() {
        final Set<Entry<String, Set<String>>> temporaryEntrySet = Sets.newHashSet(ErrorUtil.bannedContainsMaybeErrorsForTweeting.entrySet());

        logEach(temporaryEntrySet, "(new-analysis-3) - Rejecting by contains for tweeting (maybe)= ");
    }

    //

    private final void logAll(final List<Entry<String, Set<String>>> errorsToCompound, final String errorLogPrefix) {
        final StringBuilder collector = new StringBuilder();
        for (final Map.Entry<String, Set<String>> entry : errorsToCompound) {
            final Set<String> errors = entry.getValue();
            if (errors.isEmpty()) {
                continue;
            }
            final String finalErrorsAsString = asString(errors);
            collector.append(finalErrorsAsString);
            collector.append("\n");
            errors.clear();
        }
        logger.error(errorLogPrefix + collector.toString());
    }

    private final void logEach(final Set<Entry<String, Set<String>>> temporaryEntrySet, final String errorLogPrefix) {
        for (final Map.Entry<String, Set<String>> entry : temporaryEntrySet) {
            final Set<String> errors = entry.getValue();
            if (errors.isEmpty()) {
                continue;
            }
            final String finalErrorsAsString = asString(errors);

            final String token = entry.getKey();
            logger.error(errorLogPrefix + token + "; errors= \n" + finalErrorsAsString);

            errors.clear();
        }
    }

    private final void logRejectedByClassifierJobErrors() {
        final Set<String> errors = ErrorUtil.rejectedByClassifierJob;
        if (errors.isEmpty()) {
            return;
        }
        final String finalErrorsAsString = asString(errors);

        logger.error("(new-classif) - Rejected by the classifier - jobs; errors= \n" + finalErrorsAsString);

        errors.clear();
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
        errorBatch.append("\n");
        final String finalErrorsAsString = errorBatch.toString();

        return finalErrorsAsString;
    }

}
