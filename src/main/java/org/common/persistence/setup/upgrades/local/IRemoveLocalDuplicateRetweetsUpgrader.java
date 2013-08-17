package org.common.persistence.setup.upgrades.local;

/**
 * Iterates though all the <b>local</b> Retweets <br/>
 * Removes exact duplicates (by text and account) for each of these <br/>
 * - <b>use</b>: maybe - but shouldn't really do anything
 */
public interface IRemoveLocalDuplicateRetweetsUpgrader {

    void removeLocalDuplicateRetweets();

    boolean removeLocalDuplicateRetweetsOnAccount(final String twitterAccount);

}
