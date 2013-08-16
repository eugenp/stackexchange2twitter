package org.common.persistence.setup.upgrades;

public interface IRecreateMissingRetweetsUpgrader {

    void recreateLocalRetweetsFromLiveTweets();

    void processAllLiveTweetsOnAccount(final String twitterAccount);

}
