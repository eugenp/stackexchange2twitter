package org.tweet.twitter.service.live;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.TwitterTemplateCreator;

import com.google.common.collect.Maps;

@Service
@Profile(SpringProfileUtil.LIVE)
@SuppressWarnings("unused")
public class TwitterAnalysisLiveService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterReadLiveService twitterLiveService;

    @Autowired
    private TwitterTemplateCreator twitterCreator;

    public TwitterAnalysisLiveService() {
        super();
    }

    // API

    public final void analyzeAccount(final String twitterAccount) {
        final Map<String, Integer> retweetsCollector = Maps.newHashMap();
        final Map<String, Integer> favsCollector = Maps.newHashMap();

        final Twitter twitterTemplate = twitterCreator.getTwitterTemplate(twitterAccount);
        final TimelineOperations timelineOperations = twitterTemplate.timelineOperations();
        final List<Tweet> retweetsOfMe = timelineOperations.getRetweetsOfMe(1, 100);
        for (final Tweet myTweet : retweetsOfMe) {
            final List<Tweet> retweetsOfCurrentTweet = timelineOperations.getRetweets(myTweet.getId());
            for (final Tweet retweetOfMe : retweetsOfCurrentTweet) {
                analyzeRetweetOfMe(retweetOfMe, retweetsCollector);
            }
        }
        final List<Tweet> mentions = timelineOperations.getMentions(200);
        for (final Tweet mention : mentions) {
            analyzeMention(mention);
        }

        final List<Tweet> tweetsOfAccount = twitterLiveService.listTweetsOfInternalAccountRaw(twitterAccount, 200);
        for (final Tweet tweet : tweetsOfAccount) {
            analyzeGenericTweet(tweet);
        }

        System.out.println("Retweets: " + retweetsCollector);
    }

    private void analyzeRetweetOfMe(final Tweet tweet, final Map<String, Integer> retweetsCollector) {
        final Integer resultsForThatUser = retweetsCollector.get(tweet.getFromUser());
        if (resultsForThatUser == null) {
            retweetsCollector.put(tweet.getFromUser(), 1);
        } else {
            retweetsCollector.put(tweet.getFromUser(), resultsForThatUser + 1);
        }
    }

    private final void analyzeMention(final Tweet tweet) {
        // not yet defined
    }

    private final void analyzeGenericTweet(final Tweet tweet) {
        if (tweet.isFavorited()) {
            System.out.println();
        }
        // if (tweet.getEntities() != null) {
        // final Entities entities = tweet.getEntities();
        // System.out.println();
        // }
    }

}
