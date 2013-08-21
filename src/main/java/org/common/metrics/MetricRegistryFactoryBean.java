package org.common.metrics;

import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;

@Component
public final class MetricRegistryFactoryBean implements FactoryBean<MetricRegistry> {

    @Autowired
    private Environment env;

    public MetricRegistryFactoryBean() {
        super();
    }

    // API

    @Override
    public final MetricRegistry getObject() {
        final MetricRegistry metricRegistry = new MetricRegistry();

        final Slf4jReporter reporter = Slf4jReporter.forRegistry(metricRegistry).outputTo(LoggerFactory.getLogger("org.common.metrics")).convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS).build();

        final Integer metricsLogRate = env.getProperty("metrics.log.rate", Integer.class);
        reporter.start(metricsLogRate, TimeUnit.MINUTES);

        return metricRegistry;
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
