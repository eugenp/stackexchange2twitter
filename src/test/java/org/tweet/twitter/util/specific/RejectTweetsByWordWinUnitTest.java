package org.tweet.twitter.util.specific;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.classification.util.GenericClassificationDataUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.tweet.twitter.util.TwitterUtil;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@RunWith(Parameterized.class)
public final class RejectTweetsByWordWinUnitTest {

    private String tweet;

    public RejectTweetsByWordWinUnitTest(final String tweet) {
        super();
        this.tweet = tweet;
    }

    @Parameters
    public static List<String[]> invalidWords() throws IOException {
        final InputStream is = GenericClassificationDataUtil.class.getResourceAsStream("/notes/test/win-toreject.txt");
        final List<String> tweets = IOUtils.readLines(new BufferedReader(new InputStreamReader(is)));
        final List<String[]> tweetsAsSingeElementArrays = Lists.transform(tweets, new Function<String, String[]>() {
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
        assertTrue(TwitterUtil.isTweetBannedForAnalysis(tweet.toLowerCase()));
    }

}
