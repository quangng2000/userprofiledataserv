package com.riftco.userprofiledataserv.adapter.persistence.tenantusers.eventstore;

import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
class EventDescriptor {

    @Getter
    private String body;

    @Getter
    private Instant occurredAt = Instant.now();

    @Getter
    private String type;

    @PersistenceCreator
    EventDescriptor(String body, Instant occurredAt, String type) {
        this.body = body;
        this.occurredAt = occurredAt;
        this.type = type;
    }
}
