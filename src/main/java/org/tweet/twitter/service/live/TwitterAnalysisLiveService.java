package org.tweet.twitter.service.live;

import java.util.List;
import java.util.Map;

import org.common.service.live.LinkLiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.spring.util.SpringProfileUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
@Profile(SpringProfileUtil.Live.ANALYSIS)
public class TwitterAnalysisLiveService {
    // private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterReadLiveService twitterReadLiveService;

    @Autowired
    private IRetweetJpaDAO retweetLocalApi;

    public TwitterAnalysisLiveService() {
        super();
    }

    @Autowired
    private LinkLiveService linkLiveService;

    // API

    /**
     * Analyzes the account and compiles a simple report of: <br/>
     * - who retweeted you <br/>
     * - who favorited you (not yet) <br/>
     * - who mentioned you <br/>
     */
    public final void calculateLiveStatisticsForAccount(final String twitterAccount) {
        final Map<String, Integer> retweetsCollector = Maps.newHashMap();

        final Twitter twitterTemplate = twitterReadLiveService.readOnlyTwitterApi(twitterAccount);
        final TimelineOperations timelineOperations = twitterTemplate.timelineOperations();
        final List<Tweet> myRetweetedTweets = timelineOperations.getRetweetsOfMe(1, 100);
        for (final Tweet myRetweetedTweet : myRetweetedTweets) {
            final List<Tweet> retweetsOfCurrentTweet = timelineOperations.getRetweets(myRetweetedTweet.getId());
            for (final Tweet retweetOfMe : retweetsOfCurrentTweet) {
                analyzeRetweetOfMe(retweetOfMe, retweetsCollector);
            }
        }
        final List<Tweet> mentions = timelineOperations.getMentions(200);
        for (final Tweet mention : mentions) {
            analyzeMention(mention);
        }

        final List<Tweet> tweetsOfAccount = twitterReadLiveService.listTweetsOfInternalAccountRaw(twitterAccount, 200);
        for (final Tweet tweet : tweetsOfAccount) {
            analyzeGenericTweet(tweet);
        }

        System.out.println("Retweets: " + retweetsCollector);
    }

    public final int calculateAbsDifferenceBetweenLocalAndLiveRetweetsOnAccount(final String twitterAccount) {
        final List<String> tweetsOnAccount = twitterReadLiveService.listTweetsOfInternalAccount(twitterAccount, 400);
        final List<String> relevantDomains = Lists.newArrayList("http://stackoverflow.com/", "http://askubuntu.com/", "http://superuser.com/");
        final int linkingToSo = linkLiveService.countLinksToAnyDomain(tweetsOnAccount, relevantDomains);
        final int liveRetweetsOnAccount = tweetsOnAccount.size() - linkingToSo;
        final int localRetweetsOnAccount = (int) retweetLocalApi.countAllByTwitterAccount(twitterAccount);

        final int absDifference = Math.abs(liveRetweetsOnAccount - localRetweetsOnAccount);
        return absDifference;
    }

    // util

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
