package com.riftco.userprofiledataserv.domain.event;

import com.riftco.userprofiledataserv.domain.vo.TenantName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Event that captures when a tenant's name is changed.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantNameChangedEvent implements DomainEvent {

    public static final String TYPE = "tenant.name.changed";

    private UUID uuid;
    private TenantName name;
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
