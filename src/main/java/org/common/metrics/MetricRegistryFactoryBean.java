package org.common.metrics;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import com.codahale.metrics.MetricRegistry;

@Component
public final class MetricRegistryFactoryBean implements FactoryBean<MetricRegistry> {

    public MetricRegistryFactoryBean() {
        super();
    }

    // API

    @Override
    public final MetricRegistry getObject() throws Exception {
        return new MetricRegistry();
    }

    @Override
    public final Class<MetricRegistry> getObjectType() {
        return MetricRegistry.class;
    }

    @Override
    public final boolean isSingleton() {
        return true;
    }

}
