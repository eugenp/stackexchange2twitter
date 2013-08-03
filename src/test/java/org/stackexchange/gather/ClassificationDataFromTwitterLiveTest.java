package org.stackexchange.gather;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;
import org.common.spring.CommonServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.spring.StackexchangeContextConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.live.TweetToStringFunction;
import org.tweet.twitter.service.live.TwitterReadLiveService;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {//@formatter:off
    CommonServiceConfig.class, 
    
    TwitterConfig.class, 
    TwitterLiveConfig.class, 
    
    StackexchangeContextConfig.class 
}) //@formatter:on
@ActiveProfiles(SpringProfileUtil.LIVE)
public class ClassificationDataFromTwitterLiveTest {

    @Autowired
    private TwitterReadLiveService twitterService;

    // tests

    @Test
    public final void whenListingTweets_thenNoExceptions() throws IOException {
        final int fullPageCount = 20;
        int pageIndex = fullPageCount;
        final Function<Tweet, String> composedFunction = Functions.compose(new CleanupStringFunction(), new TweetToStringFunction());

        final String account = "JavaPerformance";

        final Set<String> collector = Sets.newHashSet();
        final TimelineOperations timelineOperations = twitterService.readOnlyTwitterApi().timelineOperations();

        List<Tweet> currentPage = timelineOperations.getUserTimeline(account, 200);
        currentPage = Lists.newArrayList(Collections2.filter(currentPage, new TweetIsValidPredicate()));
        collector.addAll(Lists.transform(currentPage, composedFunction));

        long lastId = currentPage.get(currentPage.size() - 1).getId();
        while (pageIndex > 0) {
            System.out.println("Processing page: " + (fullPageCount - pageIndex));
            currentPage = timelineOperations.getUserTimeline(account, 200, 01, lastId);
            currentPage = Lists.newArrayList(Collections2.filter(currentPage, new TweetIsValidPredicate()));

            collector.addAll(Lists.transform(currentPage, composedFunction));
            lastId = currentPage.get(currentPage.size() - 1).getId();
            pageIndex--;
        }

        final File file = new File("/opt/sandbox/commercial_raw.classif");
        final FileWriter fw = new FileWriter(file); // it creates the file writer and the actual file
        IOUtils.writeLines(collector, "\n", fw);

        System.out.println("Gathered: " + collector.size());
    }

    static class TweetIsValidPredicate implements Predicate<Tweet> {
        @Override
        public final boolean apply(@Nullable final Tweet input) {
            if (input.getText().contains("\n")) {
                return false;
            }
            return true;
        }
    }

}
