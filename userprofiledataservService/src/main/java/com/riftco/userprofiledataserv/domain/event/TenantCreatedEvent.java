package com.riftco.userprofiledataserv.domain.event;

import com.riftco.userprofiledataserv.domain.vo.TenantId;
import com.riftco.userprofiledataserv.domain.vo.TenantName;
import com.riftco.userprofiledataserv.domain.vo.TenantStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantCreatedEvent implements DomainEvent {

    public static final String TYPE = "tenant.created";

    private UUID uuid;
    private TenantId tenantId;
    private TenantName name;
    private TenantStatus status;
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
