package com.riftco.userprofiledataserv.domain.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Instant;
import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name = UserCreatedEvent.TYPE, value = UserCreatedEvent.class),
        @JsonSubTypes.Type(name = UserNameChangedEvent.TYPE, value = UserNameChangedEvent.class)
})
public interface DomainEvent {
    String type();
    Instant getOccurredAt();
    UUID uuid();
}
