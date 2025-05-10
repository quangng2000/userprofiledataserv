package com.riftco.userprofiledataserv.domain.event;

import com.riftco.userprofiledataserv.domain.UserState;
import com.riftco.userprofiledataserv.domain.vo.Email;
import com.riftco.userprofiledataserv.domain.vo.PhoneNumber;
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
public class UserCreatedEvent implements DomainEvent {

    public static final String TYPE = "user.created";

    private UUID uuid;
    private UserId userId;
    private TenantId tenantId;
    private UserState state;
    private String name;
    private Email email;
    private PhoneNumber contactNumber;
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
