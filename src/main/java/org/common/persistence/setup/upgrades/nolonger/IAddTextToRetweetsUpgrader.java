package org.common.persistence.setup.upgrades.nolonger;

public interface IAddTextToRetweetsUpgrader {

    void addTextOfRetweets();

    boolean addTextOfRetweetsOnAccount(String twitterAccount);

}
