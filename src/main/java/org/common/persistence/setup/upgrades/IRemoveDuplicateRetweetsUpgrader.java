package org.common.persistence.setup.upgrades;

public interface IRemoveDuplicateRetweetsUpgrader {

    void removeDuplicateLocalRetweets();

    boolean removeDuplicateRetweetsOnAccount(final String twitterAccount);

}
