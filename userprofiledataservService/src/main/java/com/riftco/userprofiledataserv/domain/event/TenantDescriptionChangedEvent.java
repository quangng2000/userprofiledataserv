package com.riftco.userprofiledataserv.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Event that captures when a tenant's description is changed.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantDescriptionChangedEvent implements DomainEvent {

    public static final String TYPE = "tenant.description.changed";

    private UUID uuid;
    private String description;
    private Instant occurredAt;

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Instant getOccurredAt() {
        return this.occurredAt;
    }

    @Override
    public UUID uuid() {
        return this.uuid;
    }
}
