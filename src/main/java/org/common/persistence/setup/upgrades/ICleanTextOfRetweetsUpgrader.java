package org.common.persistence.setup.upgrades;

public interface ICleanTextOfRetweetsUpgrader {

    void cleanTextOfRetweets();

    boolean cleanTextOfRetweetsOnAccount(String twitterAccount);

}
