package org.tweet.stackexchange.util;

import java.util.List;
import java.util.Random;

import org.stackexchange.api.constants.StackSite;

public final class StackexchangeUtil {

    private StackexchangeUtil() {
        throw new AssertionError();
    }

    // API

    public static StackSite pickOne(final StackSite... sites) {
        return sites[new Random().nextInt(sites.length)];
    }

    public static StackSite pickOne(final List<StackSite> sites) {
        return sites.get(new Random().nextInt(sites.size()));
    }

}
