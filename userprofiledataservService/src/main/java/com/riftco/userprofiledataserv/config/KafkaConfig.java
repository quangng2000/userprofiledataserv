package com.riftco.userprofiledataserv.config;

import com.riftco.userprofiledataserv.adapter.broker.SourceBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ExecutorSubscribableChannel;

/**
 * Kafka configuration for Spring Cloud Stream
 * 
 * Note: This configuration has been simplified since we now use StreamBridge directly in adapters.
 * The SourceBinding interface is kept for backward compatibility and topic name constants.
 */
@Configuration
public class KafkaConfig {
    
    /**
     * Provides a bean for the SourceBinding interface.
     * This implementation is primarily used for maintaining backward compatibility 
     * and providing access to topic constants.
     */
    @Bean
    public SourceBinding sourceBinding() {
        return new SourceBindingImpl();
    }
    
    private static class SourceBindingImpl implements SourceBinding {
        
        // Single shared channel for all methods to maintain compatibility
        private final MessageChannel messageChannel = new ExecutorSubscribableChannel();
        
        @Override
        public MessageChannel usersOut() {
            return messageChannel;
        }
        
        @Override
        public MessageChannel tenantsOut() {
            return messageChannel;
        }
        
        @Override
        public MessageChannel userProfilesOut() {
            return messageChannel;
        }
        
        @Override
        public MessageChannel tenantUsersOut() {
            return messageChannel;
        }
    }
}
