package org.common.persistence.setup.upgrades;

public interface IRecreateMissingRetweetsUpgrader {

    void recreateLocalRetweetsFromLiveTweets();

    boolean processAllLiveTweetsOnAccount(final String twitterAccount);

}
