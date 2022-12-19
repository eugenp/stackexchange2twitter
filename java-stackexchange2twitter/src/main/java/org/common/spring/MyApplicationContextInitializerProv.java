package org.common.spring;

import java.util.List;

import com.google.common.collect.Lists;

public class MyApplicationContextInitializerProv extends MyBaseApplicationContextInitializer {

    private static final String ENV_TARGET_KEY = "envTarget";
    public static final String PERSISTENCE_TARGET_KEY = "persistenceTarget";
    private static final String DEFAULT_VALUE = "production";

    private final List<String> validEnvTargets = Lists.newArrayList("dev", "production");

    // template

    @Override
    protected String getEnvTargetKey() {
        return ENV_TARGET_KEY;
    }

    @Override
    protected String getEnvTargetVal() {
        return DEFAULT_VALUE;
    }

    @Override
    protected String getPersistenceTargetKey() {
        return PERSISTENCE_TARGET_KEY;
    }

    @Override
    protected List<String> getValidEnvTargetValues() {
        return validEnvTargets;
    }

}
