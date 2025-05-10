package com.riftco.userprofiledataserv.adapter.broker;

import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Slf4j
public abstract class BaseEventToKafkaAdapter {
    private final StreamBridge streamBridge;
    private final String topicName;

    protected BaseEventToKafkaAdapter(StreamBridge streamBridge, String topicName) {
        this.streamBridge = streamBridge;
        this.topicName = topicName;
    }

    protected void send(DomainEvent event) {
        Message<DomainEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.KEY, event.uuid().toString().getBytes())
                .build();
        try {
            // Send directly to the specific topic using StreamBridge
            boolean result = streamBridge.send(topicName, message);
            if (result) {
                log.info("KAFKA SUCCESS: Event {} with ID {} successfully sent to topic: {}", 
                         event.getClass().getSimpleName(), event.uuid(), topicName);
            } else {
                log.warn("KAFKA WARNING: Event {} with ID {} may not have been sent to topic: {}", 
                          event.getClass().getSimpleName(), event.uuid(), topicName);
            }
        } catch (Exception e) {
            log.error("KAFKA ERROR: Failed to send event {} with ID {} to topic {}: {}", 
                      event.getClass().getSimpleName(), event.uuid(), topicName, e.getMessage(), e);
        }
    }
}
