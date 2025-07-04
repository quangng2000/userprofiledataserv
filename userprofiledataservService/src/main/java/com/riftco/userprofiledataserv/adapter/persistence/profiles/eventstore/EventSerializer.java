package com.riftco.userprofiledataserv.adapter.persistence.profiles.eventstore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.riftco.userprofiledataserv.adapter.persistence.users.eventstore.EnumModule;
import com.riftco.userprofiledataserv.domain.common.ValueObject;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import com.riftco.userprofiledataserv.domain.vo.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Component("profileEventSerializer")
public class EventSerializer {
    private final ObjectMapper objectMapper;

    EventSerializer() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Register value object serializers and deserializers
        SimpleModule valueObjectModule = new SimpleModule("ValueObjectModule");
        
        // User profile value objects
        registerValueObject(valueObjectModule, UserId.class);
        registerValueObject(valueObjectModule, TenantId.class);
        registerValueObject(valueObjectModule, DisplayName.class);
        registerValueObject(valueObjectModule, AvatarUrl.class);
        registerValueObject(valueObjectModule, Biography.class);
        registerValueObject(valueObjectModule, JobTitle.class);
        registerValueObject(valueObjectModule, Department.class);
        registerValueObject(valueObjectModule, Location.class);
        registerValueObject(valueObjectModule, LinkedInUrl.class);
        registerValueObject(valueObjectModule, TwitterUrl.class);
        registerValueObject(valueObjectModule, GitHubUrl.class);
        
        // Register value object module
        objectMapper.registerModule(valueObjectModule);
        
        // Register enum module for handling domain enums
        objectMapper.registerModule(new EnumModule());
    }
    
    /**
     * Helper method to register a value object with its serializer and deserializer
     */
    private <T extends ValueObject> void registerValueObject(SimpleModule module, Class<T> valueObjectClass) {
        // Re-using the ValueObjectSerializer and ValueObjectDeserializer from users package
        module.addSerializer(valueObjectClass, new com.riftco.userprofiledataserv.adapter.persistence.users.eventstore.ValueObjectSerializer<>());
        module.addDeserializer(valueObjectClass, new com.riftco.userprofiledataserv.adapter.persistence.users.eventstore.ValueObjectDeserializer<>(valueObjectClass));
    }

    EventDescriptor serialize(DomainEvent event) {
        try {
            return new EventDescriptor(objectMapper.writeValueAsString(event), event.getOccurredAt(), event.type());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    DomainEvent deserialize(EventDescriptor eventDescriptor) {
        try {
            return objectMapper.readValue(eventDescriptor.getBody(), DomainEvent.class);
        } catch (IOException e) {
            // Log the error with the raw JSON payload for debugging
            System.err.println("Error deserializing event: " + e.getMessage());
            System.err.println("Event JSON: " + eventDescriptor.getBody());
            System.err.println("Event type from descriptor: " + eventDescriptor.getType());
            e.printStackTrace();
            
            // Try an alternative approach if standard deserialization fails
            try {
                // First read as JsonNode to inspect the structure
                JsonNode root = objectMapper.readTree(eventDescriptor.getBody());
                
                // Get the event type from the JSON
                String eventType = root.has("type") ? root.get("type").asText() : eventDescriptor.getType();
                
                System.out.println("Attempting alternative deserialization for event type: " + eventType);
                
                // Use the type information to find the correct class and deserialize
                return objectMapper.treeToValue(root, DomainEvent.class);
            } catch (Exception fallbackException) {
                fallbackException.printStackTrace();
                throw new RuntimeException("Failed to deserialize event after multiple attempts: " + fallbackException.getMessage(), fallbackException);
            }
        }
    }
}
