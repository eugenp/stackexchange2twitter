package org.tweet.stackexchange.util;

import java.util.Random;

import org.stackexchange.api.constants.Site;

public final class StackexchangeUtil {

    private StackexchangeUtil() {
        throw new AssertionError();
    }

    // API

    public static Site pickOne(final Site... sites) {
        return sites[new Random().nextInt(sites.length)];
    }

}
