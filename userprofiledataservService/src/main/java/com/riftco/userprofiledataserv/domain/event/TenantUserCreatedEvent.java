package com.riftco.userprofiledataserv.domain.event;

import com.riftco.userprofiledataserv.domain.UserRole;
import com.riftco.userprofiledataserv.domain.vo.TenantId;
import com.riftco.userprofiledataserv.domain.vo.UserId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantUserCreatedEvent implements DomainEvent {

    public static final String TYPE = "tenant.user.created";

    private UUID uuid;
    private TenantId tenantId;
    private UserId userId;
    private UserRole role;
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
