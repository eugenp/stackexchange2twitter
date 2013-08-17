package org.common.persistence.setup.upgrades.live;

public interface IFillInDatesOfRetweetsUpgrader {

    boolean fillInDatesOfRetweetsOfOneAccount(String twitterAccount);

}
