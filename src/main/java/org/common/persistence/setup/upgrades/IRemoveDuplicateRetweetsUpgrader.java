package org.common.persistence.setup.upgrades;

public interface IRemoveDuplicateRetweetsUpgrader {

    void removeDuplicateLocalRetweets();

    boolean removeDuplicateLocalRetweetsOnAccount(final String twitterAccount);

}
