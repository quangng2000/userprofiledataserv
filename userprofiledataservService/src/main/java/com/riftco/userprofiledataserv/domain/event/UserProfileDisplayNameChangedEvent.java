package com.riftco.userprofiledataserv.domain.event;

import com.riftco.userprofiledataserv.domain.vo.DisplayName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Event that captures when a user's profile display name is changed.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDisplayNameChangedEvent implements DomainEvent {

    public static final String TYPE = "user.profile.displayname.changed";

    private UUID uuid;
    private DisplayName displayName;
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
