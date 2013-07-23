package org.tweet.spring.util;

public final class SpringProfileUtil {

    // these 3 profiles are modeling the environments - deployed is active for any deployment, dev and production for these specific environments
    public static final String DEPLOYED = "deployed";
    public static final String LIVE = "live";
    public static final String DEV = "dev";
    public static final String PRODUCTION = "production";
    public static final String WRITE = "write";

    // common
    public static final String TEST = "test";
    public static final String CLIENT = "client";

    private SpringProfileUtil() {
        throw new AssertionError();
    }

}
