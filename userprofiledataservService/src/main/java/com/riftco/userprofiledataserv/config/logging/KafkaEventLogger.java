package com.riftco.userprofiledataserv.config.logging;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.event.ConsumerStartingEvent;
import org.springframework.kafka.event.ConsumerStoppedEvent;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import org.springframework.kafka.support.SendResult;

/**
 * Kafka event logging for monitoring Kafka producers and consumers.
 */
@Slf4j
@Component
public class KafkaEventLogger {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Tracer tracer;

    public KafkaEventLogger(KafkaTemplate<String, Object> kafkaTemplate, Tracer tracer) {
        this.kafkaTemplate = kafkaTemplate;
        this.tracer = tracer;
    }

    /**
     * Log Kafka consumer start events.
     * 
     * @param event The consumer starting event
     */
    @EventListener
    public void handleConsumerStart(ConsumerStartingEvent event) {
        log.info("Kafka consumer starting: {}", event.getSource());
    }

    /**
     * Log Kafka consumer stop events.
     * 
     * @param event The consumer stopped event
     */
    @EventListener
    public void handleConsumerStop(ConsumerStoppedEvent event) {
        log.info("Kafka consumer stopped: {}", event.getSource());
    }

    /**
     * Log Kafka consumer idle events.
     * 
     * @param event The consumer idle event
     */
    @EventListener
    public void handleConsumerIdle(ListenerContainerIdleEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Kafka consumer idle: {} for {} ms", 
                    event.getListenerId(), event.getIdleTime());
        }
    }

    /**
     * Wrap the Kafka send operation with logging.
     * 
     * @param topic The topic to send to
     * @param key The message key
     * @param payload The message payload
     * @return CompletableFuture with send result
     */
    public <T> CompletableFuture<SendResult<String, Object>> sendWithLogging(String topic, String key, T payload) {
        log.info("Sending Kafka message to topic {} with key {}: {}", 
                topic, key, LoggingUtils.sanitizePayload(String.valueOf(payload)));
        
        // Get current span for trace context propagation
        Span currentSpan = tracer.currentSpan();
        
        // Create a producer record with headers for trace context propagation
        ProducerRecord<String, Object> record = new ProducerRecord<>(topic, null, key, payload);
        
        // Add trace context to headers if available
        if (currentSpan != null) {
            String traceId = currentSpan.context().traceId();
            String spanId = currentSpan.context().spanId();
            
            // Add W3C trace context headers
            record.headers().add("traceparent", 
                ("00-" + traceId + "-" + spanId + "-01").getBytes());
            
            log.debug("Added trace context to Kafka message: traceId={}, spanId={}", traceId, spanId);
        }
        
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(record);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Kafka message sent successfully to topic {} partition {} offset {}", 
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send Kafka message to topic {}", topic, ex);
            }
        });
        
        return future;
    }
    
    /**
     * Log received Kafka messages.
     * This is a demonstration method. In a real application, you would add this functionality
     * to your actual Kafka listeners.
     *
     * @param record The consumer record received
     */
    @KafkaListener(id = "loggingListener", topics = "example-topic", autoStartup = "false")
    public void logReceivedMessage(ConsumerRecord<String, String> record) {
        log.info("Received Kafka message from topic {} partition {} offset {} with key {}: {}", 
                record.topic(),
                record.partition(),
                record.offset(),
                record.key(),
                LoggingUtils.sanitizePayload(record.value()));
    }
}
