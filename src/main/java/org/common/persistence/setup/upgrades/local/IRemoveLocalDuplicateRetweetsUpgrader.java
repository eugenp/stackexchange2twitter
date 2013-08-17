package org.common.persistence.setup.upgrades.local;

public interface IRemoveLocalDuplicateRetweetsUpgrader {

    void removeLocalDuplicateRetweets();

    boolean removeLocalDuplicateRetweetsOnAccount(final String twitterAccount);

}
