package org.common.persistence.setup.upgrades;

public interface IAddTextToRetweetsUpgrader {

    void addTextToRetweets();

    boolean addTextToRetweetsOnAccount(String twitterAccount);

}
