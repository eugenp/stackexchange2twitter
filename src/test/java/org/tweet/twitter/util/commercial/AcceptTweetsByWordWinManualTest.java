package org.tweet.twitter.util.commercial;

import static org.junit.Assert.assertFalse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;
import org.classification.data.ClassificationData.Commercial.Accept;
import org.classification.data.GenericClassificationDataUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.tweet.twitter.util.TwitterUtil;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@RunWith(Parameterized.class)
public final class AcceptTweetsByWordWinManualTest {

    private String tweet;

    public AcceptTweetsByWordWinManualTest(final String tweet) {
        super();
        this.tweet = tweet;
    }

    @Parameters
    public static List<String[]> invalidWords() throws IOException {
        final InputStream is = GenericClassificationDataUtil.class.getResourceAsStream(Accept.WIN);
        final List<String> tweets = IOUtils.readLines(new BufferedReader(new InputStreamReader(is)));
        final List<String> tweetsFiltered = Lists.newArrayList(Iterables.filter(tweets, new Predicate<String>() {
            @Override
            public final boolean apply(@Nullable final String input) {
                if (input == null || input.isEmpty() || input.startsWith("//")) {
                    return false;
                }
                return true;
            }
        }));

        final List<String[]> tweetsAsSingeElementArrays = Lists.transform(tweetsFiltered, new Function<String, String[]>() {
            @Override
            public final String[] apply(final String tweet) {
                return new String[] { tweet };
            }

        });

        return tweetsAsSingeElementArrays;
    }

    // tests

    @Test
    public void whenTweetIsAnalyzed_thenRejected() {
        assertFalse(tweet.toLowerCase(), TwitterUtil.isTweetBannedForCommercialAnalysis(tweet.toLowerCase()));
    }

}
