package org.common.persistence.setup.upgrades.live;

public interface IRecreateMissingQuestionTweetsUpgrader {

    void recreateLocalQuestionTweetsFromLiveTweets();

    void recreateLocalQuestionTweetsOnAccount(final String twitterAccount);

}
