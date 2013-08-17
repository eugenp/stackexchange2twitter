package org.common.persistence.setup.upgrades.live;

public interface IRemoveOrphanedRetweetsUpgrader {

    void removeOrphanedRetweets();

    void removeOrphanedRetweetsOnAccount(final String twitterAccount);

}
