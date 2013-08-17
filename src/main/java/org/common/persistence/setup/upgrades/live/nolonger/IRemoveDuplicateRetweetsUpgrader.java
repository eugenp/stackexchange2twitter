package org.common.persistence.setup.upgrades.live.nolonger;

/**
 * Iterates though all the <b>live</b> Tweets <br/>
 * If any of these have more than one corresponding <b>local</b> Retweet - it removes the duplicates <br/>
 * - <b>use</b>: very unlikely - last pass it found a single occurrence, and that should be found by @IRemoveLocalDuplicateRetweetsUpgrader
 */
public interface IRemoveDuplicateRetweetsUpgrader {

    void removeDuplicateLocalRetweets();

    boolean removeDuplicateRetweetsOnAccount(final String twitterAccount);

}
