package org.tweet.meta;

public final class TwitterUserSnapshot {

    private final int mentionsOutsideOfRetweetsPercentage;
    private final int goodRetweetPercentage;
    private final int retweetsOfLargeAccountsOutOfAllGoodRetweets;
    private final int retweetsOfSelfMentionsPercentage;
    private final int retweetsOfNonFollowedUsersPercentage;

    public TwitterUserSnapshot(final int goodRetweetPercentage, final int retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage, final int retweetsOfSelfMentionsPercentage, final int mentionsOutsideOfRetweetsPercentage,
            final int retweetsOfNonFollowedUsersPercentage) {
        super();

        this.goodRetweetPercentage = goodRetweetPercentage;
        this.retweetsOfLargeAccountsOutOfAllGoodRetweets = retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage;
        this.retweetsOfSelfMentionsPercentage = retweetsOfSelfMentionsPercentage;

        this.mentionsOutsideOfRetweetsPercentage = mentionsOutsideOfRetweetsPercentage;
        this.retweetsOfNonFollowedUsersPercentage = retweetsOfNonFollowedUsersPercentage;
    }

    // API

    public final int getGoodRetweetPercentage() {
        return goodRetweetPercentage;
    }

    /**
     * - note that this relative to the total number of good retweets
     */
    public final int getRetweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage() {
        return retweetsOfLargeAccountsOutOfAllGoodRetweets;
    }

    public final int getRetweetsOfSelfMentionsPercentage() {
        return retweetsOfSelfMentionsPercentage;
    }

    public final int getMentionsOutsideOfRetweetsPercentage() {
        return mentionsOutsideOfRetweetsPercentage;
    }

    /**
     * - NEW
     */
    public final int getRetweetsOfNonFollowedUsersPercentage() {
        return retweetsOfNonFollowedUsersPercentage;
    }

    //

    @Override
    public final String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("TwitterUserSnapshot [mentionsOutsideOfRetweetsPercentage=").append(mentionsOutsideOfRetweetsPercentage).append(", goodRetweetPercentage=").append(goodRetweetPercentage).append(", retweetsOfLargeAccountsOutOfAllGoodRetweets=")
                .append(retweetsOfLargeAccountsOutOfAllGoodRetweets).append(", retweetsOfSelfMentionsPercentage=").append(retweetsOfSelfMentionsPercentage).append(", retweetsOfNonFollowedUsersPercentage=").append(retweetsOfNonFollowedUsersPercentage)
                .append("]");
        return builder.toString();
    }

}
