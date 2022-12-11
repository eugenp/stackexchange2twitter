package org.tweet.meta.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;

import com.google.api.client.util.Preconditions;
import com.google.common.base.Predicate;

public final class TweetContainsWordPredicate implements Predicate<Tweet> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String word;

    public TweetContainsWordPredicate(final String word) {
        super();

        this.word = Preconditions.checkNotNull(word);
    }

    // API

    @Override
    public final boolean apply(final Tweet input) {
        final String tweetTextLowerCase = input.getText().toLowerCase();
        if (containsWord(tweetTextLowerCase)) {
            return true;
        }
        return false;
    }

    final boolean containsWord(final String tweetTextLowerCase) {
        final boolean contains = tweetTextLowerCase.matches(".*\\b" + word + "\\b.*");
        if (!contains) {
            // logger.debug("Following tweet does not contain the word={}: \n{}", this.word, tweetTextLowerCase);
        }
        return contains;
    }

}
