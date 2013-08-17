package org.common.persistence.setup.upgrades.live;

public interface IRecreateMissingRetweetsUpgrader {

    void recreateLocalRetweetsFromLiveTweets();

    void processAllLiveTweetsOnAccount(final String twitterAccount);

}
