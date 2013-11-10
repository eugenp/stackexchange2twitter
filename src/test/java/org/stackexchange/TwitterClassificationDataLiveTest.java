package org.stackexchange;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.classification.data.GenericClassificationDataUtil;
import org.common.spring.CommonServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.spring.StackexchangeContextConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.live.TwitterReadLiveService;
import org.tweet.twitter.util.TweetUtil;
import org.tweet.twitter.util.TwitterUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {//@formatter:off
    CommonServiceConfig.class, 
    
    TwitterConfig.class, 
    TwitterLiveConfig.class, 
    
    StackexchangeContextConfig.class 
}) //@formatter:on
@ActiveProfiles(SpringProfileUtil.LIVE)
public class TwitterClassificationDataLiveTest {

    @Autowired
    private TwitterReadLiveService instance;

    // tests

    // list tweets

    @Test
    public final void givenTweetsListedByCommercialKeyword_whenClassifyingTweets_thenDataIsOnDisk() throws JsonProcessingException, IOException {
        for (final String commercialKeyword : Lists.newArrayList("win", "deal")) {
            final int pagesToConsume = 25;
            final String commercialKeywordWithHash = "#" + commercialKeyword;
            final Set<String> commercialCollector = Sets.newHashSet();
            final Set<String> nonCommercialCollector = Sets.newHashSet();
            final Set<Tweet> tweetsOfHashtag = Sets.newHashSet(instance.listTweetsByHashtagMultiRequestRaw(commercialKeywordWithHash, pagesToConsume));
            final Iterable<Tweet> tweetsOfHashtagWithoutRTsIterable = Iterables.transform(tweetsOfHashtag, new Function<Tweet, Tweet>() {
                @Override
                public final Tweet apply(final Tweet input) {
                    return TweetUtil.getTweet(input);
                }
            });
            final Set<Tweet> tweetsOfHashtagWithoutRTs = Sets.newHashSet(tweetsOfHashtagWithoutRTsIterable);

            for (final Tweet tweet : tweetsOfHashtagWithoutRTs) {
                final String tweetText = TweetUtil.getText(tweet);
                if (tweetText.contains("\n")) { // skip multi-line tweets
                    continue;
                }
                if (TwitterUtil.isTweetBannedForCommercialAnalysis(tweetText)) {
                    commercialCollector.add(tweetText);
                } else {
                    nonCommercialCollector.add(tweetText);
                }
            }

            // remove already existing data
            final InputStream rejectIs = GenericClassificationDataUtil.class.getResourceAsStream("/notes/test/" + commercialKeyword + "-toreject.txt");
            final List<String> rejectExistingData = IOUtils.readLines(new BufferedReader(new InputStreamReader(rejectIs)));
            final InputStream acceptIs = GenericClassificationDataUtil.class.getResourceAsStream("/notes/test/" + commercialKeyword + "-toaccept.txt");
            final List<String> acceptExistingData = IOUtils.readLines(new BufferedReader(new InputStreamReader(acceptIs)));
            nonCommercialCollector.removeAll(acceptExistingData);
            commercialCollector.removeAll(rejectExistingData);

            // write results to disk
            final File commercialFile = new File("/opt/sandbox/" + commercialKeyword + "_commercial_raw.txt");
            Files.write(Joiner.on("\r\n").join(commercialCollector), commercialFile, Charsets.UTF_8);

            final File nonCommercialFile = new File("/opt/sandbox/" + commercialKeyword + "_nonCommercial_raw.txt");
            Files.write(Joiner.on("\r\n").join(nonCommercialCollector), nonCommercialFile, Charsets.UTF_8);
        }
    }

}
