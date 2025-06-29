package com.riftco.userprofiledataserv.config.logging;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Central logging configuration for the application.
 * Configures various aspects of logging behavior and registers common log context.
 */
@Configuration
public class LoggingConfiguration {
    
    private static final Logger log = LoggerFactory.getLogger(LoggingConfiguration.class);
    
    private final Environment env;
    private final Optional<BuildProperties> buildProperties;
    
    /**
     * Constructor for LoggingConfiguration.
     * 
     * @param env The Spring environment
     * @param buildProperties Build properties (optional)
     */
    public LoggingConfiguration(Environment env, Optional<BuildProperties> buildProperties) {
        this.env = env;
        this.buildProperties = buildProperties != null ? buildProperties : Optional.empty();
    }
    
    /**
     * Initialize logging configuration and log application startup information.
     */
    @PostConstruct
    public void init() {
        String appName = env.getProperty("spring.application.name");
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path", "/");
        
        String hostAddress;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("Could not determine host address", e);
            hostAddress = "unknown";
        }
        
        String activeProfiles = Arrays.toString(env.getActiveProfiles());
        
        log.info("\n----------------------------------------------------------\n" +
                "Application '{}' is running! Access URLs:\n" +
                "Local:      http://localhost:{}{}\n" +
                "External:   http://{}:{}{}\n" +
                "Profile(s): {}\n" +
                "Version:    {}\n" +
                "----------------------------------------------------------",
            appName,
            serverPort,
            contextPath,
            hostAddress,
            serverPort,
            contextPath,
            activeProfiles,
            buildProperties.map(BuildProperties::getVersion).orElse("unknown"));
    }
    
    /**
     * Create a bean for MDC (Mapped Diagnostic Context) cleanup filter.
     * This ensures MDC is properly cleared after each request.
     * 
     * @return The MDC cleanup filter
     */
    @Bean
    public MDCCleanupFilter mdcCleanupFilter() {
        return new MDCCleanupFilter();
    }
    
    /**
     * MDC cleanup filter ensures thread-local MDC data is cleared after each request.
     */
    public static class MDCCleanupFilter extends LoggingFilter {
        @Override
        protected boolean shouldNotFilterAsyncDispatch() {
            return false;
        }
    }
}
