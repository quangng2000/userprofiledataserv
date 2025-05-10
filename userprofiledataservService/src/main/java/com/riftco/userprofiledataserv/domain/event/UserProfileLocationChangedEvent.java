package com.riftco.userprofiledataserv.domain.event;

import com.riftco.userprofiledataserv.domain.vo.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Event that captures when a user's profile location is changed.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileLocationChangedEvent implements DomainEvent {

    public static final String TYPE = "user.profile.location.changed";

    private UUID uuid;
    private Location location;
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
