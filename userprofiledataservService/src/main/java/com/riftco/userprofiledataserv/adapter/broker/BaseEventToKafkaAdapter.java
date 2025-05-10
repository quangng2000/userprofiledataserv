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
            streamBridge.send(topicName, message);
            log.info("Sent event {} to topic: {}", event.getClass().getSimpleName(), topicName);
        } catch (Exception e) {
            log.error("Failed to send event to topic {}: {}", topicName, e.getMessage(), e);
        }
    }
}
