package org.common.persistence.setup.upgrades;

public interface IRemoveLocalDuplicateRetweetsUpgrader {

    void removeLocalDuplicateRetweets();

    boolean removeLocalDuplicateRetweetsOnAccount(final String twitterAccount);

}
