package org.tweet.twitter.evaluator;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tweet.twitter.util.ErrorUtil;

import com.google.api.client.util.Preconditions;

public abstract class AbstractEvaluator implements IEvaluator {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final List<String> acceptedRegExes;
    private final List<String> bannedRegExesMaybe;
    private final List<String> bannedRegExes;

    private final String tag;

    private final Map<String, Set<String>> bannedRegExesMaybeErrors;

    public AbstractEvaluator(final List<String> acceptedRegExes, final List<String> bannedRegExesMaybe, final List<String> bannedRegExes, final String tag, final Map<String, Set<String>> bannedRegExesMaybeErrors) {
        super();

        this.acceptedRegExes = Preconditions.checkNotNull(acceptedRegExes);
        this.bannedRegExesMaybe = Preconditions.checkNotNull(bannedRegExesMaybe);
        this.bannedRegExes = Preconditions.checkNotNull(bannedRegExes);
        this.tag = Preconditions.checkNotNull(tag);

        this.bannedRegExesMaybeErrors = Preconditions.checkNotNull(bannedRegExesMaybeErrors);
    }

    // API

    @Override
    public boolean isRejectedByBannedRegexExpressions(final String tweet) {
        final String textLowerCase = tweet.toLowerCase();

        for (final String hardAcceptedRegEx : acceptedRegExes) {
            if (textLowerCase.matches(hardAcceptedRegEx)) {
                // was error - is OK now - moving down - move back up when something is added into the accept list
                logger.info("(" + tag + ") - Hard Accept by regular expression (maybe)=  " + hardAcceptedRegEx + "; text= \n" + tweet);
                return false;
            }
        }

        for (final String bannedRegExMaybe : bannedRegExesMaybe) {
            if (textLowerCase.matches(bannedRegExMaybe)) {
                ErrorUtil.registerError(bannedRegExesMaybeErrors, bannedRegExMaybe, tweet);
                return true;
            }
        }

        for (final String bannedRegEx : bannedRegExes) {
            if (textLowerCase.matches(bannedRegEx)) {
                return true;
            }
        }

        return false;
    }

}
