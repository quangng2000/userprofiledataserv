package com.riftco.userprofiledataserv.domain.event;

import com.riftco.userprofiledataserv.domain.vo.AvatarUrl;
import com.riftco.userprofiledataserv.domain.vo.DisplayName;
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
public class UserProfileCreatedEvent implements DomainEvent {

    public static final String TYPE = "user.profile.created";

    private UUID uuid;
    private UserId userId;
    private TenantId tenantId;
    private DisplayName displayName;
    private AvatarUrl avatarUrl;
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
