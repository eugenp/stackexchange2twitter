package org.tweet.spring.util;

public final class SpringProfileUtil {

    public static final class Live {
        public static final String ANALYSIS = "analysis";
    }

    // these 3 profiles are modeling the environments - deployed is active for any deployment, dev and production for these specific environments
    public static final String DEPLOYED = "deployed";
    public static final String DEPLOYED_POLLER = "deployed_poller";
    /**
     * < WRITE < WRITE_PRODUCTION
     */
    public static final String LIVE = "live";
    public static final String DEV = "dev";
    /** > WRITE > LIVE */
    public static final String WRITE_PRODUCTION = "production";
    /** > LIVE */
    public static final String WRITE = "write";

    // common
    public static final String TEST = "test";
    public static final String CLIENT = "client";

    private SpringProfileUtil() {
        throw new AssertionError();
    }

}
