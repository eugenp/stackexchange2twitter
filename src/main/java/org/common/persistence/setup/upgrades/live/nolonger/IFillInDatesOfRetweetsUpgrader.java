package org.common.persistence.setup.upgrades.live.nolonger;

/**
 * Iterates though all the <b>live</b> Tweets <br/>
 * Finds their corresponding <b>local</b> Retweet and adds the date to it (if it doesn't already have it) <br/>
 * - <b>use</b>: no longer - most Retweets now have dates - and the ones that do not, are orphans and need to be removed anyways
 */
public interface IFillInDatesOfRetweetsUpgrader {

    void fillInDatesOfRetweetsOfAllAccounts();

    boolean fillInDatesOfRetweetsOfOneAccount(final String twitterAccount);

}
