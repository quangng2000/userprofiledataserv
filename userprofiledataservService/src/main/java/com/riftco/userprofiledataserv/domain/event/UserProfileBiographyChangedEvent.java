package com.riftco.userprofiledataserv.domain.event;

import com.riftco.userprofiledataserv.domain.vo.Biography;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Event that captures when a user's profile biography is changed.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileBiographyChangedEvent implements DomainEvent {

    public static final String TYPE = "user.profile.biography.changed";

    private UUID uuid;
    private Biography biography;
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
