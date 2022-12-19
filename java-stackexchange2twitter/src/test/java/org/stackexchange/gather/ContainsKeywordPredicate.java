package org.stackexchange.gather;

import java.util.List;

import org.classification.util.TweetSettings;

import com.google.api.client.util.Preconditions;
import com.google.common.base.CharMatcher;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class ContainsKeywordPredicate implements Predicate<String> {

    private final List<String> keywords;

    public ContainsKeywordPredicate(final List<String> keywords) {
        super();
        this.keywords = Preconditions.checkNotNull(keywords);
    }

    // API

    @Override
    public final boolean apply(final String input) {
        final String inputInternal = input.toLowerCase();
        final List<String> tweetTokens = Lists.newArrayList(Splitter.on(CharMatcher.anyOf(TweetSettings.TWEET_TOKENIZER + "#[]")).split(inputInternal));
        for (final String token : tweetTokens) {
            if (keywords.contains(token)) {
                return true;
            }
        }

        return false;
    }

}
