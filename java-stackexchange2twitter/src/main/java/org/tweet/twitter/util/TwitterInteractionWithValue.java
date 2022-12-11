package org.tweet.twitter.util;

public final class TwitterInteractionWithValue {
    private final float val;
    private final TwitterInteraction twitterInteraction;

    public TwitterInteractionWithValue(final TwitterInteraction twitterInteraction, final float val) {
        super();

        this.twitterInteraction = twitterInteraction;
        this.val = val;
    }

    // API

    public final float getVal() {
        return val;
    }

    public final TwitterInteraction getTwitterInteraction() {
        return twitterInteraction;
    }

    @Override
    public final String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("TwitterInteractionWithValue [val=").append(val).append(", twitterInteraction=").append(twitterInteraction).append("]");
        return builder.toString();
    }

}
