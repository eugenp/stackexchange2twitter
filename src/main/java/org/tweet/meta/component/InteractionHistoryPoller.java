package org.tweet.meta.component;

import java.util.List;

import org.common.service.live.LinkLiveService;
import org.keyval.persistence.dao.IKeyValJpaDAO;
import org.keyval.persistence.model.KeyVal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.social.twitter.api.MentionEntity;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.util.TweetNotLinksToSePredicate;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.live.TwitterAnalysisLiveService;
import org.tweet.twitter.service.live.TwitterReadLiveService;

import com.google.api.client.util.Preconditions;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Component
@Profile(SpringProfileUtil.DEPLOYED_POLLER)
public class InteractionHistoryPoller {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterReadLiveService twitterReadLiveService;

    @Autowired
    private TwitterAnalysisLiveService twitterAnalysisLiveService;

    @Autowired
    private LinkLiveService linkLiveService;

    @Autowired
    private IKeyValJpaDAO keyValueApi;

    public InteractionHistoryPoller() {
        super();
    }

    // API

    @Scheduled(cron = "0 30 6 * * *")
    public void checkAndUpdateHistory() throws InterruptedException {
        logger.info("Starting to check account history");

        for (final TwitterAccountEnum twitterAccount : TwitterAccountEnum.values()) {
            if (twitterAccount.isRt()) {
                logger.info("Performing check and update of history for twitterAccount= " + twitterAccount.name());
                checkAndUpdateHistoryOnAccount(twitterAccount.name());
                logger.info("Done performing check and update of history for twitterAccount= " + twitterAccount.name() + "; sleeping for 5 secs...");
                Thread.sleep(1000 * 5 * 1); // 5 sec
            }
        }

        logger.info("Finished checking account history");
    }

    // util

    /**
     * - the current implementation still works on the assumption that the account has less than 200 tweets
     */
    final void checkAndUpdateHistoryOnAccount(final String twitterAccount) {
        final List<Tweet> tweetsToAnalyze = getTweetsToAnalyize(twitterAccount);
        if (tweetsToAnalyze.isEmpty()) { // nothing new to analyze yet
            return;
        }
        final Iterable<Tweet> metaRetweets = Iterables.filter(tweetsToAnalyze, new TweetNotLinksToSePredicate(linkLiveService));
        for (final Tweet tweet : metaRetweets) {
            processTweet(tweet, twitterAccount);
        }

        createOrUpdateLastId(twitterAccount, tweetsToAnalyze);
    }

    private final void processTweet(final Tweet tweet, final String twitterAccount) {
        final List<MentionEntity> mentions = tweet.getEntities().getMentions();
        if (mentions.isEmpty()) {
            return;
        }
        final List<Tweet> retweetsOfThisTweet = twitterReadLiveService.readOnlyTwitterApi().timelineOperations().getRetweets(tweet.getId());
        final List<String> usersWhoRetweeted = Lists.transform(retweetsOfThisTweet, new Function<Tweet, String>() {
            @Override
            public final String apply(final Tweet input) {
                return input.getFromUser();
            }
        });
        for (final MentionEntity mention : mentions) {
            processMention(twitterAccount, usersWhoRetweeted, mention);
        }
    }

    private void processMention(final String twitterAccount, final List<String> usersWhoRetweeted, final MentionEntity mention) {
        final String key = "interaction." + twitterAccount + "." + mention.getScreenName();

        final KeyVal interactionStatus = getOrCreateCurrentInteractionStatus(key);

        if (usersWhoRetweeted.contains(mention.getScreenName())) {
            processPositiveInteraction(twitterAccount, mention, interactionStatus);
        } else {
            processNegativeInteraction(twitterAccount, mention, interactionStatus);
        }
    }

    private final KeyVal getOrCreateCurrentInteractionStatus(final String key) {
        KeyVal interactionStatus = keyValueApi.findByKey(key);
        if (interactionStatus == null) {
            interactionStatus = keyValueApi.save(new KeyVal(key, "0"));
        }
        return interactionStatus;
    }

    private final void processPositiveInteraction(final String twitterAccount, final MentionEntity mention, final KeyVal interactionStatus) {
        Integer currentInteractionValue = Integer.valueOf(interactionStatus.getValue());
        if (currentInteractionValue <= 0) {
            currentInteractionValue = 1;
        } else {
            currentInteractionValue += 1;
        }
        interactionStatus.setValue(currentInteractionValue.toString());

        keyValueApi.save(interactionStatus);

        logger.info("Good interaction found on twitterAccount= {}, with= {}", twitterAccount, mention.getScreenName());
    }

    private final void processNegativeInteraction(final String twitterAccount, final MentionEntity mention, final KeyVal interactionStatus) {
        Integer currentInteractionValue = Integer.valueOf(interactionStatus.getValue());
        currentInteractionValue -= 1;
        interactionStatus.setValue(currentInteractionValue.toString());

        keyValueApi.save(interactionStatus);

        logger.debug("No interaction found on twitterAccount= {}, with= {}", twitterAccount, mention.getScreenName());
    }

    private List<Tweet> getTweetsToAnalyize(final String twitterAccount) {
        final String lastIdKey = "interaction." + twitterAccount + ".lastId";
        final KeyVal lastIdKeyVal = keyValueApi.findByKey(lastIdKey);
        if (lastIdKeyVal == null) {
            return twitterReadLiveService.listTweetsOfInternalAccountRaw(twitterAccount, 200);
        } else {
            final Long sinceId = Long.valueOf(lastIdKeyVal.getValue());
            final List<Tweet> listTweetsOfInternalAccountRaw = twitterReadLiveService.listTweetsOfInternalAccountRaw(twitterAccount, 1);
            Preconditions.checkState(!listTweetsOfInternalAccountRaw.isEmpty(), "No tweets to analyze for account= " + twitterAccount);

            final long lastTweetId = listTweetsOfInternalAccountRaw.get(0).getId();

            return twitterReadLiveService.readOnlyTwitterApi(twitterAccount).timelineOperations().getUserTimeline(200, sinceId, lastTweetId);
        }
    }

    private final void createOrUpdateLastId(final String twitterAccount, final List<Tweet> tweetsToAnalyze) {
        final String lastIdKey = "interaction." + twitterAccount + ".lastId";
        final Long lastIdRetrieved = tweetsToAnalyze.get(0).getId();
        final KeyVal lastIdKeyVal = keyValueApi.findByKey(lastIdKey);
        if (lastIdKeyVal == null) {
            keyValueApi.save(new KeyVal(lastIdKey, lastIdRetrieved.toString()));
        } else {
            lastIdKeyVal.setValue(lastIdRetrieved.toString());
            keyValueApi.save(lastIdKeyVal);
        }
    }

}
