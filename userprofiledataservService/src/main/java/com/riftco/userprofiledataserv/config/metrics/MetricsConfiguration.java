package com.riftco.userprofiledataserv.config.metrics;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Configuration for Metrics collection and exposure.
 * Sets up various JVM and system metrics binders.
 */
@Configuration
public class MetricsConfiguration {

    /**
     * Customizes the meter registry with application name and other common tags.
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(Environment environment) {
        return registry -> registry.config()
                .commonTags("application", environment.getProperty("spring.application.name", "userprofiledataserv"));
    }

    /**
     * Registers JVM memory metrics (heap, non-heap, pools).
     */
    @Bean
    public JvmMemoryMetrics jvmMemoryMetrics() {
        JvmMemoryMetrics memoryMetrics = new JvmMemoryMetrics();
        memoryMetrics.bindTo(Metrics.globalRegistry);
        return memoryMetrics;
    }

    /**
     * Registers JVM garbage collection metrics.
     */
    @Bean
    public JvmGcMetrics jvmGcMetrics() {
        JvmGcMetrics gcMetrics = new JvmGcMetrics();
        gcMetrics.bindTo(Metrics.globalRegistry);
        return gcMetrics;
    }

    /**
     * Registers JVM thread metrics (count, states, etc).
     */
    @Bean
    public JvmThreadMetrics jvmThreadMetrics() {
        JvmThreadMetrics threadMetrics = new JvmThreadMetrics();
        threadMetrics.bindTo(Metrics.globalRegistry);
        return threadMetrics;
    }

    /**
     * Registers JVM classloader metrics.
     */
    @Bean
    public ClassLoaderMetrics classLoaderMetrics() {
        ClassLoaderMetrics classLoaderMetrics = new ClassLoaderMetrics();
        classLoaderMetrics.bindTo(Metrics.globalRegistry);
        return classLoaderMetrics;
    }

    /**
     * Registers system processor metrics (CPU usage, load).
     */
    @Bean
    public ProcessorMetrics processorMetrics() {
        ProcessorMetrics processorMetrics = new ProcessorMetrics();
        processorMetrics.bindTo(Metrics.globalRegistry);
        return processorMetrics;
    }

    /**
     * Registers file descriptor usage metrics.
     */
    @Bean
    public FileDescriptorMetrics fileDescriptorMetrics() {
        FileDescriptorMetrics fileDescriptorMetrics = new FileDescriptorMetrics();
        fileDescriptorMetrics.bindTo(Metrics.globalRegistry);
        return fileDescriptorMetrics;
    }

    /**
     * Registers uptime metrics for the application.
     */
    @Bean
    public UptimeMetrics uptimeMetrics() {
        UptimeMetrics uptimeMetrics = new UptimeMetrics();
        uptimeMetrics.bindTo(Metrics.globalRegistry);
        return uptimeMetrics;
    }

    /**
     * Sets up the @Timed aspect for method-level timing metrics.
     * This allows using the @Timed annotation on methods to track their execution time.
     */
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
