package com.riftco.userprofiledataserv.domain.event;

import com.riftco.userprofiledataserv.domain.vo.TenantStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Event that captures when a tenant's status is changed (e.g., active to inactive).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantStatusChangedEvent implements DomainEvent {

    public static final String TYPE = "tenant.status.changed";

    private UUID uuid;
    private TenantStatus status;
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
