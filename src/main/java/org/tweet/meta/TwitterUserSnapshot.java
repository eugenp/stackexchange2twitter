package org.tweet.meta;

public final class TwitterUserSnapshot {

    private int mentionsPercentage;
    private int goodRetweetPercentage;
    private int retweetsOfLargeAccountsPercentage;
    private int retweetsOfSelfMentionsPercentage;

    public TwitterUserSnapshot(final int goodRetweetPercentage, final int retweetsOfLargeAccountsPercentage, final int retweetsOfSelfMentionsPercentage, final int mentionsPercentage) {
        super();

        this.goodRetweetPercentage = goodRetweetPercentage;
        this.retweetsOfLargeAccountsPercentage = retweetsOfLargeAccountsPercentage;
        this.retweetsOfSelfMentionsPercentage = retweetsOfSelfMentionsPercentage;

        this.mentionsPercentage = mentionsPercentage;
    }

    // API

    public final int getGoodRetweetPercentage() {
        return goodRetweetPercentage;
    }

    public final int getRetweetsOfLargeAccountsPercentage() {
        return retweetsOfLargeAccountsPercentage;
    }

    public final int getRetweetsOfSelfMentionsPercentage() {
        return retweetsOfSelfMentionsPercentage;
    }

    public final int getMentionsPercentage() {
        return mentionsPercentage;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("TwitterUserSnapshot [mentionsPercentage=").append(mentionsPercentage).append(", goodRetweetPercentage=").append(goodRetweetPercentage).append(", retweetsOfLargeAccountsPercentage=").append(retweetsOfLargeAccountsPercentage)
                .append(", retweetsOfSelfMentionsPercentage=").append(retweetsOfSelfMentionsPercentage).append("]");
        return builder.toString();
    }

}
