package com.riftco.userprofiledataserv.adapter.persistence.users.eventstore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.riftco.userprofiledataserv.domain.common.ValueObject;
import com.riftco.userprofiledataserv.domain.event.DomainEvent;
import com.riftco.userprofiledataserv.domain.vo.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Component("userEventSerializer")
public class EventSerializer {
    private final ObjectMapper objectMapper;

    EventSerializer() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Register value object serializers and deserializers
        SimpleModule valueObjectModule = new SimpleModule("ValueObjectModule");
        
        // User value objects
        registerValueObject(valueObjectModule, Email.class);
        registerValueObject(valueObjectModule, PhoneNumber.class);
        registerValueObject(valueObjectModule, UserId.class);
        
        // Tenant value objects
        registerValueObject(valueObjectModule, TenantId.class);
        registerValueObject(valueObjectModule, TenantName.class);
        
        // Profile value objects
        registerValueObject(valueObjectModule, DisplayName.class);
        registerValueObject(valueObjectModule, AvatarUrl.class);
        registerValueObject(valueObjectModule, Biography.class);
        registerValueObject(valueObjectModule, JobTitle.class);
        registerValueObject(valueObjectModule, Department.class);
        registerValueObject(valueObjectModule, Location.class);
        registerValueObject(valueObjectModule, LinkedInUrl.class);
        registerValueObject(valueObjectModule, TwitterUrl.class);
        registerValueObject(valueObjectModule, GitHubUrl.class);
        
        // Other value objects
        registerValueObject(valueObjectModule, SubscriptionPlan.class);
        
        // Register value object module
        objectMapper.registerModule(valueObjectModule);
        
        // Register enum module for handling domain enums
        objectMapper.registerModule(new EnumModule());
    }
    
    /**
     * Helper method to register a value object with its serializer and deserializer
     */
    private <T extends ValueObject> void registerValueObject(SimpleModule module, Class<T> valueObjectClass) {
        module.addSerializer(valueObjectClass, new ValueObjectSerializer<>());
        module.addDeserializer(valueObjectClass, new ValueObjectDeserializer<>(valueObjectClass));
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
            throw new RuntimeException(e);
        }
    }
}
