package org.common.persistence.setup.upgrades.live;

public interface IRecreateMissingRetweetsUpgrader {

    void recreateLocalRetweetsFromLiveTweets();

    void recreateLocalRetweetsFromLiveTweetsOnAccount(final String twitterAccount);

}
