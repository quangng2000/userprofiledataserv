package com.riftco.userprofiledataserv.config;

import com.riftco.userprofiledataserv.adapter.broker.SourceBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.stream.function.StreamBridge;

@Configuration
public class KafkaConfig {
    
    /**
     * The StreamBridge bean is automatically created by Spring Cloud Stream
     * and used to send messages to outputs. This replaces the old
     * @EnableBinding approach which is deprecated in Spring Cloud Stream 3.x
     */
    @Bean
    public SourceBinding sourceBinding(StreamBridge streamBridge) {
        return new SourceBindingImpl(streamBridge);
    }
    
    private static class SourceBindingImpl implements SourceBinding {
        private final StreamBridge streamBridge;
        
        public SourceBindingImpl(StreamBridge streamBridge) {
            this.streamBridge = streamBridge;
        }
        
        // This implementation will be used internally by StreamBridge when sending messages
        // Users will call streamBridge.send(SourceBinding.USERS_OUT, message) directly
        private final org.springframework.messaging.MessageChannel messageChannel = new org.springframework.messaging.support.ExecutorSubscribableChannel();
        
        /**
         * Helper method to send messages using the StreamBridge
         * @param payload the message payload to send
         */
        public void sendUserMessage(Object payload) {
            streamBridge.send(USERS_OUT, payload);
        }
        
        @Override
        public org.springframework.messaging.MessageChannel usersOut() {
            return messageChannel;
        }
    }
}
