package org.tweet.twitter.util;

public final class TwitterInteractionWithValue {
    private final int val;
    private final TwitterInteraction twitterInteraction;

    public TwitterInteractionWithValue(final TwitterInteraction twitterInteraction, final int val) {
        super();

        this.twitterInteraction = twitterInteraction;
        this.val = val;
    }

    // API

    public final int getVal() {
        return val;
    }

    public final TwitterInteraction getTwitterInteraction() {
        return twitterInteraction;
    }

}
