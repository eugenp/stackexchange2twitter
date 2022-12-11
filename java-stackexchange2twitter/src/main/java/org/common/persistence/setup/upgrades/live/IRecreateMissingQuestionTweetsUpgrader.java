package org.common.persistence.setup.upgrades.live;

import org.springframework.social.twitter.api.Tweet;

public interface IRecreateMissingQuestionTweetsUpgrader {

    void recreateLocalQuestionTweetsFromLiveTweets();

    void recreateLocalQuestionTweetsOnAccount(final String twitterAccount);

    String extractQuestionIdFromTweet(Tweet tweet);

}
