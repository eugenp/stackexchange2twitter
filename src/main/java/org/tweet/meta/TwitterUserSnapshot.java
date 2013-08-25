package org.tweet.meta;

public final class TwitterUserSnapshot {

    private final float mentionsOutsideOfRetweetsPercentage;
    private final float goodRetweetPercentage;
    private final float retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage;
    private final float retweetsOfSelfMentionsPercentage;
    private final float retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage;

    public TwitterUserSnapshot(final float goodRetweetPercentage, final float retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage, final float retweetsOfSelfMentionsPercentage, final float mentionsOutsideOfRetweetsPercentage,
            final float retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage) {
        super();

        this.goodRetweetPercentage = goodRetweetPercentage;
        this.retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage = retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage;
        this.retweetsOfSelfMentionsPercentage = retweetsOfSelfMentionsPercentage;

        this.mentionsOutsideOfRetweetsPercentage = mentionsOutsideOfRetweetsPercentage;
        this.retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage = retweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage;
    }

    // API

    public final float getGoodRetweetPercentage() {
        return goodRetweetPercentage;
    }

    /**
     * - note that this relative to the total number of good retweets
     */
    public final float getRetweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage() {
        return retweetsOfSmallAccountsOutOfAllGoodRetweetsPercentage;
    }

    public final float getRetweetsOfSelfMentionsPercentage() {
        return retweetsOfSelfMentionsPercentage;
    }

    public final float getMentionsOutsideOfRetweetsPercentage() {
        return mentionsOutsideOfRetweetsPercentage;
    }

    public final float getRetweetsOfNonFollowedUsersOutOfGoodRetweetsPercentage() {
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
