package org.common.persistence.setup.upgrades;

public interface IFillInDatesOfRetweetsUpgrader {

    boolean fillInDatesOfRetweetsOfOneAccount(String twitterAccount);

}
