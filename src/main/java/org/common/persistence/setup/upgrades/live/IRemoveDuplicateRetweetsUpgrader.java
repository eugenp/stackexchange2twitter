package org.common.persistence.setup.upgrades.live;

public interface IRemoveDuplicateRetweetsUpgrader {

    void removeDuplicateLocalRetweets();

    boolean removeDuplicateRetweetsOnAccount(final String twitterAccount);

}
