package org.common.persistence.setup.upgrades.local;

/**
 * Iterates though all the <b>local</b> Retweets <br/>
 * Cleans their text with the latest text cleanup rules (which are frequently updated) <br/>
 * - <b>use</b>: regularly
 */
public interface ICleanTextOfRetweetsUpgrader {

    void cleanTextOfRetweets();

    boolean cleanTextOfRetweetsOnAccount(String twitterAccount);

}
