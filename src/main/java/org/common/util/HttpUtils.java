package org.common.util;

import java.util.List;

import com.google.common.collect.Lists;

public final class HttpUtils {

    final static List<String> bannedDomains = Lists.newArrayList(// @formatter:off
        "http://wp-plugin-archive.de",
        "http://www.blogging-inside.de"
    );// @formatter:on

    private HttpUtils() {
        throw new AssertionError();
    }

    // API

    /**
     * - note: simplistic implementation to be improved when needed
     */
    public static boolean belongsToBannedDomain(final String url) {
        for (final String bannedDomain : bannedDomains) {
            if (url.startsWith(bannedDomain)) {
                return true;
            }
        }

        return false;
    }

}
