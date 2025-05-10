package com.riftco.userprofiledataserv.domain.event;

import com.riftco.userprofiledataserv.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantUserRoleChangedEvent implements DomainEvent {

    public static final String TYPE = "tenant.user.role.changed";

    private UUID uuid;
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
