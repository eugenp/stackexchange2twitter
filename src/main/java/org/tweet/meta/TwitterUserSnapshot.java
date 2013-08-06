package org.tweet.meta;

public final class TwitterUserSnapshot {

    private int mentionsOutsideOfRetweetsPercentage;
    private int goodRetweetPercentage;
    private int retweetsOfLargeAccountsOutOfAllGoodRetweets;
    private int retweetsOfSelfMentionsPercentage;

    public TwitterUserSnapshot(final int goodRetweetPercentage, final int retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage, final int retweetsOfSelfMentionsPercentage, final int mentionsOutsideOfRetweetsPercentage) {
        super();

        this.goodRetweetPercentage = goodRetweetPercentage;
        this.retweetsOfLargeAccountsOutOfAllGoodRetweets = retweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage;
        this.retweetsOfSelfMentionsPercentage = retweetsOfSelfMentionsPercentage;

        this.mentionsOutsideOfRetweetsPercentage = mentionsOutsideOfRetweetsPercentage;
    }

    // API

    public final int getGoodRetweetPercentage() {
        return goodRetweetPercentage;
    }

    public final int getRetweetsOfLargeAccountsOutOfAllGoodRetweetsPercentage() {
        return retweetsOfLargeAccountsOutOfAllGoodRetweets;
    }

    public final int getRetweetsOfSelfMentionsPercentage() {
        return retweetsOfSelfMentionsPercentage;
    }

    public final int getMentionsOutsideOfRetweetsPercentage() {
        return mentionsOutsideOfRetweetsPercentage;
    }

    @Override
    public final String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("TwitterUserSnapshot [mentionsOutsideOfRetweetsPercentage=").append(mentionsOutsideOfRetweetsPercentage).append(", goodRetweetPercentage=").append(goodRetweetPercentage).append(", retweetsOfLargeAccountsPercentage=")
                .append(retweetsOfLargeAccountsOutOfAllGoodRetweets).append(", retweetsOfSelfMentionsPercentage=").append(retweetsOfSelfMentionsPercentage).append("]");
        return builder.toString();
    }

}
