package org.common.persistence.setup.upgrades;

public interface IAddTextToRetweetsUpgrader {

    void addTextOfRetweets();

    boolean addTextOfRetweetsOnAccount(String twitterAccount);

}
