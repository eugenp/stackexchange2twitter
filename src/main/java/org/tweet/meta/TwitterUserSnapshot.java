package org.tweet.meta;

public final class TwitterUserSnapshot {

    private final int mentionsOutsideOfRetweetsPercentage;
    private final int goodRetweetPercentage;
    private final int retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage;
    private final int retweetsOfSelfMentionsPercentage;
    private final int retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage;

    public TwitterUserSnapshot(final int goodRetweetPercentage, final int retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage, final int retweetsOfSelfMentionsPercentage, final int mentionsOutsideOfRetweetsPercentage,
            final int retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage) {
        super();

        this.goodRetweetPercentage = goodRetweetPercentage;
        this.retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage = retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage;
        this.retweetsOfSelfMentionsPercentage = retweetsOfSelfMentionsPercentage;

        this.mentionsOutsideOfRetweetsPercentage = mentionsOutsideOfRetweetsPercentage;
        this.retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage = retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage;
    }

    // API

    public final int getGoodRetweetPercentage() {
        return goodRetweetPercentage;
    }

    /**
     * - note that this relative to the total number of good retweets
     */
    public final int getRetweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage() {
        return retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage;
    }

    public final int getRetweetsOfSelfMentionsPercentage() {
        return retweetsOfSelfMentionsPercentage;
    }

    public final int getMentionsOutsideOfRetweetsPercentage() {
        return mentionsOutsideOfRetweetsPercentage;
    }

    public final int getRetweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage() {
        return retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage;
    }

    //

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("TwitterUserSnapshot [mentionsOutsideOfRetweetsPercentage=").append(mentionsOutsideOfRetweetsPercentage).append(", goodRetweetPercentage=").append(goodRetweetPercentage).append(", retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage=")
                .append(retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage).append(", retweetsOfSelfMentionsPercentage=").append(retweetsOfSelfMentionsPercentage).append(", retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage=")
                .append(retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage).append("]");
        return builder.toString();
    }

}
