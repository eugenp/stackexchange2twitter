package org.tweet.meta;

public final class TwitterUserSnapshot {

    private final double mentionsOutsideOfRetweetsPercentage;
    private final double goodRetweetPercentage;
    private final double retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage;
    private final double retweetsOfSelfMentionsPercentage;
    private final double retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage;

    public TwitterUserSnapshot(final double goodRetweetPercentage, final double retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage, final double retweetsOfSelfMentionsPercentage, final double mentionsOutsideOfRetweetsPercentage,
            final double retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage) {
        super();

        this.goodRetweetPercentage = goodRetweetPercentage;
        this.retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage = retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage;
        this.retweetsOfSelfMentionsPercentage = retweetsOfSelfMentionsPercentage;

        this.mentionsOutsideOfRetweetsPercentage = mentionsOutsideOfRetweetsPercentage;
        this.retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage = retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage;
    }

    // API

    public final double getGoodRetweetPercentage() {
        return goodRetweetPercentage;
    }

    /**
     * - note that this relative to the total number of good retweets
     */
    public final double getRetweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage() {
        return retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage;
    }

    public final double getRetweetsOfSelfMentionsPercentage() {
        return retweetsOfSelfMentionsPercentage;
    }

    public final double getMentionsOutsideOfRetweetsPercentage() {
        return mentionsOutsideOfRetweetsPercentage;
    }

    public final double getRetweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage() {
        return retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage;
    }

    //

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("TwitterUserSnapshot [mentionsOutsideOfRetweetsPercentage=").append(mentionsOutsideOfRetweetsPercentage).append(", goodRetweetPercentage=").append(goodRetweetPercentage).append(", retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage=")
                .append(retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage).append(", retweetsOfSelfMentionsPercentage=").append(retweetsOfSelfMentionsPercentage).append(", retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage=")
                .append(retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage).append("]");
        return builder.toString();
    }

}
